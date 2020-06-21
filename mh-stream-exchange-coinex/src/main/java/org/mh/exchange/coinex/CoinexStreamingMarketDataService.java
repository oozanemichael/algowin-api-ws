package org.mh.exchange.coinex;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import io.reactivex.Observable;
import lombok.SneakyThrows;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingMarketDataService;
import org.mh.stream.exchange.exception.NumberOfSubscriptionsException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CoinexStreamingMarketDataService implements StreamingMarketDataService {

    private final CoinexStreamingService streamingService;

    public CoinexStreamingMarketDataService(CoinexStreamingService streamingService) {
        this.streamingService = streamingService;
    }

    /**
     * 一次连接只能订阅一个
     * {
     *   "method":"depth.subscribe",
     *   "params":[
     *     "BTCBCH",           #1.market: See<API invocation description·market>
     *     20,                 #2.limit: Count limit, Integer
     *     "0"                 #3.interval: Merge，String
     *   ],
     *   "id":15
     * }
     *
     * @param args args[0] limit: Count limit, Integer
     *             args[1] interval: Merge，String
     *
     * */
    @SneakyThrows
    @Override
    public Observable<OrderBook> getOrderBook(ParsingCurrencyPair currencyPair, Object... args) {
        if (streamingService.channels.containsKey("depth.update")){
            throw new NumberOfSubscriptionsException(1,
                    "Please unsubscribe of the previous currency pair first");
        }
        JSONObject json=new JSONObject();
        json.put("method","depth.subscribe");
        JSONArray jsonArray=new JSONArray();
        jsonArray.add(currencyPair.getParsing());
        jsonArray.add(args[0]);
        jsonArray.add(args[1]);
        json.put("params",jsonArray);
        json.put("id",new Random().nextInt(1000000000));
        return streamingService.subscribeChannel("depth.update",json.toJSONString()).map(
                e->{
                    JsonNode param=e.get("params").get(1);
                    List<LimitOrder> listBids = new ArrayList<>();
                    List<LimitOrder> listAsks = new ArrayList<>();
                    Date date=new Date(param.get("time").asLong());
                    if (param.hasNonNull("bids")) {
                        param.get("bids").forEach(
                                bids -> {
                                    listBids.add(new LimitOrder(Order.OrderType.BID, new BigDecimal(bids.get(1).asText()), currencyPair.getCurrencyPair(), null, date, new BigDecimal(bids.get(0).asText())));
                                }
                        );
                    }
                    if (param.hasNonNull("asks")){
                        param.get("asks").forEach(
                                asks->{
                                    listAsks.add(new LimitOrder(Order.OrderType.ASK,new BigDecimal(asks.get(1).asText()),currencyPair.getCurrencyPair(),null,date,new BigDecimal(asks.get(0).asText())));
                                }
                        );
                    }
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
