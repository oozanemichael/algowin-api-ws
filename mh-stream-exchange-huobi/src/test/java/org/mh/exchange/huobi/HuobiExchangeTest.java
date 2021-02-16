package org.mh.exchange.huobi;


import io.reactivex.Observable;

import io.reactivex.disposables.Disposable;
import org.junit.Test;
import org.market.hedge.core.TradingArea;
import org.market.hedge.service.StreamingParsingCurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.mh.stream.exchange.core.*;

public class HuobiExchangeTest {

    private static final Logger log = LoggerFactory.getLogger(HuobiExchangeTest.class);

    @Test
    public void subOrderBook() {

        log.debug("sss");
        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(HuobiStreamingExchange.class.getName(), TradingArea.PerpetualSwap);
        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing= exchange.getStreamingParsingCurrencyPair();
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.BTC_USD));
        Disposable disposable=observable.subscribe(o -> {
            log.info("Asks: {},amount:{}", o.getAsks().get(0).getLimitPrice(),o.getAsks().get(0).getOriginalAmount());
            log.info("Bids: {}amount:{}", o.getBids().get(0).getLimitPrice(),o.getBids().get(0).getOriginalAmount());
            //Thread.sleep(2000);
            log.warn("_");
        });

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


    public static void main(String[] args) {
        HuobiStreamingExchange exchange =
                (HuobiStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(HuobiStreamingExchange.class.getName(), TradingArea.PerpetualSwap);
        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing= exchange.getStreamingParsingCurrencyPair();
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.BTC_USD));
        Disposable disposable=observable.subscribe(o -> {
            log.info("Asks: {},amount:{}", o.getAsks().get(0).getLimitPrice(),o.getAsks().get(0).getOriginalAmount());
            log.info("Bids: {}amount:{}", o.getBids().get(0).getLimitPrice(),o.getBids().get(0).getOriginalAmount());
            //Thread.sleep(2000);
            log.warn("_");
        });
    }

}
