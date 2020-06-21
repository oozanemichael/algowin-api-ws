package org.mh.exchange.deribit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.deribit.v2.DeribitExchange;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingMarketDataService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DeribitStreamingMarketDataService implements StreamingMarketDataService {

    private final DeribitStreamingService streamingService;

    public DeribitStreamingMarketDataService(DeribitStreamingService streamingService) {
        this.streamingService = streamingService;
    }

    /**
     * @param args args[0] depth integer	1/10/20	Number of price levels to be included.
     *  channel eg: "book.ETH-PERPETUAL.100.1.100ms";
     * */
    @Override
    public Observable<OrderBook> getOrderBook(ParsingCurrencyPair currencyPair, Object... args) {
        String channel="book."+currencyPair.getParsing()+".none."+(Integer)args[0]+".100ms";
        return streamingService.subscribeChannel(channel,"public/subscribe")
                .map(o->{
                    JsonNode data=o.get("params").get("data");
                    List<LimitOrder> listBids = new ArrayList<>();
                    List<LimitOrder> listAsks = new ArrayList<>();
                    Date date=new Date(data.get("timestamp").asLong());
                    data.get("bids").forEach(
                            bids->{
                                listBids.add(new LimitOrder(Order.OrderType.BID,bids.get(1).decimalValue(),currencyPair.getCurrencyPair(),null,date,bids.get(0).decimalValue()));
                            }
                    );
                    data.get("asks").forEach(
                            asks->{
                                listAsks.add(new LimitOrder(Order.OrderType.ASK,asks.get(1).decimalValue(),currencyPair.getCurrencyPair(),null,date,asks.get(0).decimalValue()));
                            }
                    );
                    return new OrderBook(date,listAsks,listBids);
                });
    }

    @Override
    public Observable<Ticker> getTicker(ParsingCurrencyPair currencyPair, Object... args) {
        return null;
    }

    @Override
    public Observable<Trade> getTrades(ParsingCurrencyPair currencyPair, Object... args) {
        return null;
    }
}
