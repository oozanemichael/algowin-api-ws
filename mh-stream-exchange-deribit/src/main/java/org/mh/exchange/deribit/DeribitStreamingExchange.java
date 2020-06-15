package org.mh.exchange.deribit;

import io.reactivex.Completable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.deribit.v2.DeribitExchange;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.mh.exchange.deribit.parsing.DeribitStreamingFuturesParsing;
import org.mh.exchange.deribit.parsing.DeribitStreamingOptionParsing;
import org.mh.stream.exchange.core.ProductSubscription;
import org.mh.stream.exchange.core.StreamingExchange;
import org.mh.stream.exchange.core.StreamingMarketDataService;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;
import si.mazi.rescu.SynchronizedValueFactory;

import java.io.IOException;
import java.util.List;

public class DeribitStreamingExchange extends DeribitExchange implements StreamingExchange {

    private static final String API_URI = "wss://www.deribit.com/ws/api/v2";

    private static final String TESTNET_API_URI = "wss://test.deribit.com/ws/api/v2";

    DeribitStreamingService streamingService;

    StreamingParsingCurrencyPair parsingCurrencyPair;

    DeribitStreamingMarketDataService marketDataService;


    public DeribitStreamingExchange(){
        this.streamingService=new DeribitStreamingService(API_URI);
        this.marketDataService=new DeribitStreamingMarketDataService(this.streamingService);
    }

    @Override
    protected void initServices() {
        super.initServices();
        this.streamingService=createStreamingService();
        this.marketDataService=new DeribitStreamingMarketDataService(this.streamingService);
    }

    private DeribitStreamingService createStreamingService() {
        ExchangeSpecification exchangeSpec = getExchangeSpecification();
        Boolean useSandbox = (Boolean) exchangeSpec.getExchangeSpecificParametersItem(USE_SANDBOX);
        String uri = useSandbox == null || !useSandbox ? API_URI : TESTNET_API_URI;
        DeribitStreamingService streamingService =
                new DeribitStreamingService(uri);
        applyStreamingSpecification(exchangeSpec, streamingService);
        return streamingService;
    }


    @Override
    public void instanceAsFutures() {
        parsingCurrencyPair=new DeribitStreamingFuturesParsing();
    }

    @Override
    public void instanceAsOption() {
        parsingCurrencyPair=new DeribitStreamingOptionParsing();
    }

    @Override
    public Completable connect(ProductSubscription... args) {
        return streamingService.connect();
    }

    @Override
    public Completable disconnect() {
        return streamingService.disconnect();
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public StreamingMarketDataService getStreamingMarketDataService() {
        return marketDataService;
    }

    @Override
    public StreamingParsingCurrencyPair getStreamingParsingCurrencyPair() {
        return parsingCurrencyPair;
    }

    @Override
    public void useCompressedMessages(boolean compressedMessages) {

    }

    @Override
    public ExchangeSpecification getExchangeSpecification() {
        return this.exchangeSpecification;
    }

    @Override
    public ExchangeMetaData getExchangeMetaData() {
        return null;
    }

    @Override
    public List<CurrencyPair> getExchangeSymbols() {
        return null;
    }

    @Override
    public SynchronizedValueFactory<Long> getNonceFactory() {
        return null;
    }

    @Override
    public ExchangeSpecification getDefaultExchangeSpecification() {
        ExchangeSpecification spec = super.getDefaultExchangeSpecification();
        spec.setShouldLoadRemoteMetaData(false);
        return spec;
    }

    @Override
    public void applySpecification(ExchangeSpecification exchangeSpecification) {

    }

    @Override
    public MarketDataService getMarketDataService() {
        return null;
    }

    @Override
    public TradeService getTradeService() {
        return null;
    }

    @Override
    public AccountService getAccountService() {
        return null;
    }

    @Override
    public void remoteInit() throws IOException, ExchangeException {

    }
}
