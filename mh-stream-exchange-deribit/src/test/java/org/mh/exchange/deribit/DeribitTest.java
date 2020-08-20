package org.mh.exchange.deribit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.mh.stream.exchange.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeribitTest {

    private static final Logger log = LoggerFactory.getLogger(DeribitTest.class);

    public static void main(String[] args) throws InterruptedException {

        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(DeribitStreamingExchange.class.getName(), TradingArea.Futures);

/*        ExchangeSpecification defaultExchangeSpecification = exchange.getDefaultExchangeSpecification();
        //defaultExchangeSpecification.setExchangeSpecificParametersItem("Use_Sandbox", true);
        defaultExchangeSpecification.setExchangeSpecificParametersItem(
                StreamingExchange.USE_SANDBOX, true);
        exchange.applySpecification(defaultExchangeSpecification);*/

        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing=exchange.getStreamingParsingCurrencyPair();
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.BTC_USD),10);
        Disposable disposable=observable.subscribe(o -> {
            log.warn("{}",o.getAsks());
            log.warn("{}",o.getBids());
        });


    }

}
