package org.mh.exchange.bibox;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.mh.stream.exchange.core.StreamingExchange;
import org.mh.stream.exchange.core.StreamingExchangeFactory;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;
import org.mh.stream.exchange.core.TradingArea;

@Log4j2
public class BiboxTest {

    public static void main(String[] args) {

        StreamingExchange exchange =StreamingExchangeFactory.INSTANCE.createExchange(BiboxStreamingExchange.class.getName(), TradingArea.FUTURE);
        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing= exchange.getStreamingParsingCurrencyPair();
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.BTC_USDT));
        Disposable disposable=observable.subscribe(o -> {
            log.warn(o.getAsks());
        });

        disposable.dispose();
    }

}
