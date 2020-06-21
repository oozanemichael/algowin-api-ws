package org.mh.exchange.huobi;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import io.reactivex.Observable;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.huobi.HuobiExchange;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingMarketDataService;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;

import java.util.*;

@Log4j2
public class HuobiStreamingMarketDataService implements StreamingMarketDataService {

    private final HuobiStreamingService streamingService;

    private final HuobiExchange huobiExchange;

    public HuobiStreamingMarketDataService(HuobiStreamingService streamingService, HuobiExchange huobiExchange) {
        this.streamingService = streamingService;
        this.huobiExchange = huobiExchange;
    }




    @Override
    public Observable<OrderBook> getOrderBook(ParsingCurrencyPair currencyPair, Object... args) {
        JSONObject json=new JSONObject();
        json.put("sub","market."+currencyPair.getParsing()+".depth.step0");
        json.put("id", UUID.randomUUID().toString());
        return streamingService.subscribeChannel("market."+currencyPair.getParsing()+".depth.step0",json)
                .map(
                o->{
                    Date date=new Date(o.get("ts").asLong());
                    List<LimitOrder> listBids = new ArrayList<>();
                    List<LimitOrder> listAsks = new ArrayList<>();
                    JsonNode tick=o.get("tick");
                    tick.get("bids").forEach(e->{
                        listBids.add(new LimitOrder(Order.OrderType.BID,e.get(1).decimalValue(),currencyPair.getCurrencyPair(),null,date,e.get(0).decimalValue()));
                    });
                    tick.get("asks").forEach(e->{
                        listAsks.add(new LimitOrder(Order.OrderType.ASK,e.get(1).decimalValue(),currencyPair.getCurrencyPair(),null,date,e.get(0).decimalValue()));
                    });
                    return new OrderBook(date,listAsks,listBids);
                }
        );
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
