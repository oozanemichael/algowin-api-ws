package org.mh.exchange.deribit;

import io.reactivex.Observable;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.mh.stream.exchange.core.StreamingExchange;
import org.mh.stream.exchange.core.StreamingExchangeFactory;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;
import org.mh.stream.exchange.core.TradingArea;
import org.mh.stream.exchange.util.LocalExchangeConfig;
import org.mh.stream.exchange.util.PropsLoader;

@Log4j2
public class DeribitTest {

    public static void main(String[] args) {

        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(DeribitStreamingExchange.class.getName(), TradingArea.FUTURE);

/*        ExchangeSpecification defaultExchangeSpecification = exchange.getDefaultExchangeSpecification();
        //defaultExchangeSpecification.setExchangeSpecificParametersItem("Use_Sandbox", true);
        defaultExchangeSpecification.setExchangeSpecificParametersItem(
                StreamingExchange.USE_SANDBOX, true);
        exchange.applySpecification(defaultExchangeSpecification);*/

        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing=exchange.getStreamingParsingCurrencyPair();
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.ETH_USD),1);
        observable.subscribe(o -> {
            log.warn("BTC_USDT");
/*            o.getAsks().forEach(e->{
                log.warn("ask: :"+e.getLimitPrice()+" amount:"+e.getOriginalAmount());
            });*/
        });



    }

}
