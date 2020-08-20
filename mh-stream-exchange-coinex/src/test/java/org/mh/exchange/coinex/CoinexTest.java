package org.mh.exchange.coinex;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.mh.stream.exchange.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinexTest {

    private static final Logger log = LoggerFactory.getLogger(CoinexTest.class);

    public static void main(String[] args) {

/*        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(CoinexStreamingExchange.class.getName(), TradingArea.FUTURE);
        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing=exchange.getStreamingParsingCurrencyPair();
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.ETH_USD),1,"0");
        Disposable disposable=observable.subscribe(o -> {
            log.warn("ETH_USD");
        });*/


        StreamingExchange exchangess =
                StreamingExchangeFactory.INSTANCE.createExchange(CoinexStreamingExchange.class.getName(), TradingArea.Futures);
        StreamingParsingCurrencyPair parsingss=exchangess.getStreamingParsingCurrencyPair();
        exchangess.connect().blockingAwait();
        Observable<OrderBook> observabless=exchangess.getStreamingMarketDataService().getOrderBook(parsingss.parsing(CurrencyPair.BTC_USD),20,"0");
        Disposable disposabless=observabless.subscribe(o -> {
            log.info("ask: "+o.getAsks());
            log.warn("bid: "+o.getBids());
        });


        /*try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        disposable.dispose();
        Observable<OrderBook> observablesss=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.BTC_USD),1,"0");
        observablesss.subscribe(o -> {
            log.warn(o.getAsks());
        });
*/

    }

}
