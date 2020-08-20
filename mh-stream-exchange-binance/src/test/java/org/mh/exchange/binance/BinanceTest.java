package org.mh.exchange.binance;

import io.reactivex.disposables.Disposable;
import org.knowm.xchange.currency.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.*;

public class BinanceTest {

    private static final Logger log = LoggerFactory.getLogger(BinanceTest.class);

    public static void main(String[] args) {
        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(BinanceStreamingExchange.class.getName(), TradingArea.Spot);

        ProductSubscription.ProductSubscriptionBuilder builder = ProductSubscription.create();
        builder.addOrderbook(new CurrencyPair(Currency.EOS,Currency.USDT));

        // 连接到Exchange WebSocket API。 阻塞等待连接。
        exchange.connect(builder.build()).blockingAwait();

        // 订阅带有订阅参考的订单簿数据。
        Disposable subscription =
                exchange
                        .getStreamingMarketDataService()
                        .getOrderBook(new ParsingCurrencyPair(null,new CurrencyPair(Currency.EOS,Currency.USDT)))
                        .subscribe(
                                orderBook -> {
                                    // 做点什么
                                    /*log.info("Asks: {}", orderBook.getAsks().get(0));
                                    log.info("Bids: {}", orderBook.getBids().get(0));
                                    log.warn("_");*/
                                    log.info("Asks: {}", orderBook.getAsks().get(0));
                                    log.info("Bids: {}", orderBook.getBids().get(0));
                                    log.warn("_");
                                });

    }

}
