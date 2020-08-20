package org.mh.exchange.huobi;


import io.reactivex.Observable;

import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.mh.stream.exchange.core.*;

public class HuobiExchangeTest {

    private static final Logger log = LoggerFactory.getLogger(HuobiExchangeTest.class);


    public static void main(String[] args) {


        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(HuobiStreamingExchange.class.getName(),TradingArea.Margin);
        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing= exchange.getStreamingParsingCurrencyPair();
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.BTC_USDT));
        Disposable disposable=observable.subscribe(o -> {
            log.info("Asks: {}", o.getAsks().get(0));
            log.info("Bids: {}", o.getBids().get(0));
            log.warn("_");
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        disposable.dispose();


/*
        Observable<OrderBook> observables=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.ETH_USDT));
        Disposable depth_ETH_USDT=observables.subscribe(o -> {
            log.warn("ETH_USDT");
        });
        log.info("sub ETH_USDT");
*/


        //取消订阅ETH USDT
        //depth_ETH_USDT.dispose();
        //取消连接
        //exchange.disconnect().blockingAwait();

    }

}
