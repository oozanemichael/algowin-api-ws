package org.mh.exchange.bibox;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.market.hedge.core.TradingArea;
import org.market.hedge.service.StreamingParsingCurrencyPair;
import org.mh.stream.exchange.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BiboxTest {

    private static final Logger log = LoggerFactory.getLogger(BiboxTest.class);

    public static void main(String[] args) {
        StreamingExchange exchange =StreamingExchangeFactory.INSTANCE.createExchange(BiboxStreamingExchange.class.getName(), TradingArea.Futures);
        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing= exchange.getStreamingParsingCurrencyPair();
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.BTC_USDT));
        Disposable disposable=observable.subscribe(o -> {
            log.warn("{}",o);
            //log.warn("{}",o.getAsks());
        });

        //disposable.dispose();
    }

}
