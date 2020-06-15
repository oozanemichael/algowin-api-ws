package org.mh.exchange.huobi;

import com.fasterxml.jackson.databind.JsonNode;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.mh.stream.exchange.core.*;

@Log4j2
public class HuobiExchangeTest {

    public static void main(String[] args) {


        StreamingExchange exchange =
                StreamingExchangeFactory.INSTANCE.createExchange(HuobiStreamingExchange.class.getName(),TradingArea.SPOT);
        exchange.connect().blockingAwait();
        StreamingParsingCurrencyPair parsing= exchange.getStreamingParsingCurrencyPair();
        Observable<OrderBook> observable=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.BTC_USDT));
        observable.subscribe(o -> {
            log.warn("BTC_USDT");
/*            o.getAsks().forEach(e->{
                log.warn("ask: :"+e.getLimitPrice()+" amount:"+e.getOriginalAmount());
            });*/
        });
        Observable<OrderBook> observables=exchange.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.ETH_USDT));
        Disposable depth_ETH_USDT=observables.subscribe(o -> {
            log.warn("ETH_USDT");
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //取消订阅ETH USDT
        depth_ETH_USDT.dispose();
        //取消连接
        //exchange.disconnect().blockingAwait();

    }

}
