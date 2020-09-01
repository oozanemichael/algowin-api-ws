package org.mh.exchange.binance;

import com.google.common.base.MoreObjects;
import org.knowm.xchange.binance.BinanceAuthenticated;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.service.BinanceMarketDataService;
import org.mh.stream.exchange.core.TradingArea;
import org.knowm.xchange.service.BaseExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mh.exchange.binance.BinanceUserDataChannel.NoActiveChannelException;

import io.reactivex.Completable;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.ProductSubscription;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;
import org.mh.stream.exchange.util.Events;
import si.mazi.rescu.RestProxyFactory;
import org.mh.stream.exchange.core.StreamingExchange;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BinanceStreamingExchange extends BinanceExchange implements StreamingExchange {

  private static final Logger log = LoggerFactory.getLogger(BinanceStreamingExchange.class);

  private static String API_BASE_URI=null;

  private static final String API_BASE_URI_SPOT = "wss://stream.binance.com:9443/";

  private static final String API_BASE_URI_USDT_CONTRACT = "wss://fstream.binance.com/";

  protected static final String USE_HIGHER_UPDATE_FREQUENCY =
      "Binance_Orderbook_Use_Higher_Frequency";

  private org.mh.exchange.binance.BinanceStreamingService streamingService;
  private org.mh.exchange.binance.BinanceUserDataStreamingService userDataStreamingService;

  private BinanceStreamingMarketDataService streamingMarketDataService;
  private org.mh.exchange.binance.BinanceStreamingAccountService streamingAccountService;
  private BinanceStreamingTradeService streamingTradeService;

  private org.mh.exchange.binance.BinanceUserDataChannel userDataChannel;
  private Runnable onApiCall;
  private String orderBookUpdateFrequencyParameter = "";

  @Override
  public void instance(TradingArea area) {
      switch (area){
        case Futures:
        case PerpetualSwap:
        case Margin:
          API_BASE_URI=API_BASE_URI_USDT_CONTRACT;
          break;
        case Spot:
          API_BASE_URI=API_BASE_URI_SPOT;
          break;
        default:
          throw new NotImplementedException();
      }
  }

  @Override
  protected void initServices() {
    super.initServices();
    this.onApiCall = Events.onApiCall(exchangeSpecification);
    Boolean userHigherFrequency =
        MoreObjects.firstNonNull(
            (Boolean)
                exchangeSpecification.getExchangeSpecificParametersItem(
                    USE_HIGHER_UPDATE_FREQUENCY),
            Boolean.FALSE);

    if (userHigherFrequency) {
      orderBookUpdateFrequencyParameter = "@100ms";
    }
  }

  /**
   * Binance streaming API expects connections to multiple channels to be defined at connection
   * time. To define the channels for this connection pass a `ProductSubscription` in at connection
   * time.
   *
   * @param args A single `ProductSubscription` to define the subscriptions required to be available
   *     during this connection.
   * @return A completable which fulfils once connection is complete.
   */
  @Override
  public Completable connect(ProductSubscription... args) {
    if (args == null || args.length == 0) {
      throw new IllegalArgumentException("Subscriptions must be made at connection time");
    }
    if (streamingService != null) {
      throw new UnsupportedOperationException(
          "Exchange only handles a single connection - disconnect the current connection.");
    }

    ProductSubscription subscriptions = args[0];
    streamingService = createStreamingService(subscriptions);

    List<Completable> completables = new ArrayList<>();

    if (subscriptions.hasUnauthenticated()) {
      completables.add(streamingService.connect());
    }

    if (subscriptions.hasAuthenticated()) {
      if (exchangeSpecification.getApiKey() == null) {
        throw new IllegalArgumentException("API key required for authenticated streams");
      }

      log.info("Connecting to authenticated web socket");
      BinanceAuthenticated binance =
          RestProxyFactory.createProxy(
              BinanceAuthenticated.class,
              getExchangeSpecification().getSslUri(),
              new BaseExchangeService<BinanceExchange>(this) {}.getClientConfig());
      userDataChannel =
          new org.mh.exchange.binance.BinanceUserDataChannel(binance, exchangeSpecification.getApiKey(), onApiCall);
      try {
        completables.add(createAndConnectUserDataService(userDataChannel.getListenKey()));
      } catch (NoActiveChannelException e) {
        throw new IllegalStateException("Failed to establish user data channel", e);
      }
    }

    streamingMarketDataService =
        new BinanceStreamingMarketDataService(
            streamingService,
            (BinanceMarketDataService) marketDataService,
            onApiCall,
            orderBookUpdateFrequencyParameter);
    streamingAccountService = new org.mh.exchange.binance.BinanceStreamingAccountService(userDataStreamingService);
    streamingTradeService = new BinanceStreamingTradeService(userDataStreamingService);

    return Completable.concat(completables)
        .doOnComplete(() -> streamingMarketDataService.openSubscriptions(subscriptions))
        .doOnComplete(() -> streamingAccountService.openSubscriptions())
        .doOnComplete(() -> streamingTradeService.openSubscriptions());
  }

  private Completable createAndConnectUserDataService(String listenKey) {
    userDataStreamingService = org.mh.exchange.binance.BinanceUserDataStreamingService.create(listenKey);
    return userDataStreamingService
        .connect()
        .doOnComplete(
            () -> {
              log.info("Connected to authenticated web socket");
              userDataChannel.onChangeListenKey(
                  newListenKey -> {
                    userDataStreamingService
                        .disconnect()
                        .doOnComplete(
                            () -> {
                              createAndConnectUserDataService(newListenKey)
                                  .doOnComplete(
                                      () -> {
                                        streamingAccountService.setUserDataStreamingService(
                                            userDataStreamingService);
                                        streamingTradeService.setUserDataStreamingService(
                                            userDataStreamingService);
                                      });
                            });
                  });
            });
  }

  @Override
  public Completable disconnect() {
    List<Completable> completables = new ArrayList<>();
    completables.add(streamingService.disconnect());
    streamingService = null;
    if (userDataStreamingService != null) {
      completables.add(userDataStreamingService.disconnect());
      userDataStreamingService = null;
    }
    if (userDataChannel != null) {
      userDataChannel.close();
      userDataChannel = null;
    }
    streamingMarketDataService = null;
    return Completable.concat(completables);
  }

  @Override
  public boolean isAlive() {
    return streamingService != null && streamingService.isSocketOpen();
  }

  @Override
  public Observable<Throwable> reconnectFailure() {
    return streamingService.subscribeReconnectFailure();
  }

  @Override
  public Observable<Object> connectionSuccess() {
    return streamingService.subscribeConnectionSuccess();
  }

  @Override
  public BinanceStreamingMarketDataService getStreamingMarketDataService() {
    return streamingMarketDataService;
  }

  @Override
  public StreamingParsingCurrencyPair getStreamingParsingCurrencyPair() {
    return null;
  }

  @Override
  public org.mh.exchange.binance.BinanceStreamingAccountService getStreamingAccountService() {
    return streamingAccountService;
  }

  @Override
  public BinanceStreamingTradeService getStreamingTradeService() {
    return streamingTradeService;
  }

  private org.mh.exchange.binance.BinanceStreamingService createStreamingService(ProductSubscription subscription) {
    String path = API_BASE_URI + "stream?streams=" + buildSubscriptionStreams(subscription);
    return new org.mh.exchange.binance.BinanceStreamingService(path, subscription);
  }

  public String buildSubscriptionStreams(ProductSubscription subscription) {
    return Stream.of(
            buildSubscriptionStrings(subscription.getTicker(), "ticker"),
            buildSubscriptionStrings(subscription.getOrderBook(), "depth"),
            buildSubscriptionStrings(subscription.getTrades(), "trade"))
        .filter(s -> !s.isEmpty())
        .collect(Collectors.joining("/"));
  }

  private String buildSubscriptionStrings(
          List<CurrencyPair> currencyPairs, String subscriptionType) {
    if ("depth".equals(subscriptionType)) {
      return subscriptionStrings(currencyPairs)
          .map(s -> s + "@" + subscriptionType + orderBookUpdateFrequencyParameter)
          .collect(Collectors.joining("/"));
    } else {
      return subscriptionStrings(currencyPairs)
          .map(s -> s + "@" + subscriptionType)
          .collect(Collectors.joining("/"));
    }
  }

  private static Stream<String> subscriptionStrings(List<CurrencyPair> currencyPairs) {
    return currencyPairs.stream()
        .map(pair -> String.join("", pair.toString().split("/")).toLowerCase());
  }

  @Override
  public void useCompressedMessages(boolean compressedMessages) {
    streamingService.useCompressedMessages(compressedMessages);
  }
}
