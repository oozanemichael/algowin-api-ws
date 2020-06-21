package org.mh.exchange.coinex;

import io.reactivex.Observable;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.mh.stream.exchange.core.StreamingExchange;
import org.mh.stream.exchange.core.StreamingExchangeFactory;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;
import org.mh.stream.exchange.core.TradingArea;

@Log4j2
public class CoinexTest {

    public static void main(String[] args) {
        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(CoinexStreamingExchange.class.getName(), TradingArea.FUTURE);
        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing=exchange.getStreamingParsingCurrencyPair();
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.ETH_USD),1,"0");
        observable.subscribe(o -> {
            log.warn(o.getAsks());
        });

    }

}
