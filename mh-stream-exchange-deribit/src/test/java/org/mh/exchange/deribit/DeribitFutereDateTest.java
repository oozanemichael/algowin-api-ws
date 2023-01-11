package org.mh.exchange.deribit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.market.hedge.core.TradingArea;
import org.market.hedge.service.StreamingParsingCurrencyPair;
import org.mh.stream.exchange.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeribitFutereDateTest {

    private static final Logger log = LoggerFactory.getLogger(DeribitFutereDateTest.class);

    public static void main(String[] args) {
        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(DeribitStreamingExchange.class.getName(), TradingArea.Futures);
        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing=exchange.getStreamingParsingCurrencyPair();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
        try {
            date = dateFormat1.parse("2020-12-25");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.BTC_USD,date),1);
        Disposable disposable=observable.subscribe(o->{log.info("{}",o);});

    }

    @Test
    public void futures() {
        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(DeribitStreamingExchange.class.getName(), TradingArea.Futures);
        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing=exchange.getStreamingParsingCurrencyPair();
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.BTC_USD),10);
        Disposable disposable=observable.subscribe(o -> {
            log.info("Asks: {},amount:{}", o.getAsks().get(0).getLimitPrice(),o.getAsks().get(0).getOriginalAmount());
            log.info("Bids: {}amount:{}", o.getBids().get(0).getLimitPrice(),o.getBids().get(0).getOriginalAmount());
         });
    }

}
