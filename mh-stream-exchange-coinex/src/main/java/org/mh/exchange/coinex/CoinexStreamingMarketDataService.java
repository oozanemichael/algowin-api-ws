package org.mh.exchange.coinex;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import io.reactivex.Observable;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingMarketDataService;
import org.mh.stream.exchange.exception.NumberOfSubscriptionsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

public class CoinexStreamingMarketDataService implements StreamingMarketDataService {

    private static final Logger log = LoggerFactory.getLogger(CoinexStreamingMarketDataService.class);

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
    @Override
    public Observable<OrderBook> getOrderBook(ParsingCurrencyPair currencyPair, Object... args) {
        if (streamingService.channels.containsKey("depth.update")){
            log.info("coinex order book : ",new NumberOfSubscriptionsException(1,"Please unsubscribe of the previous currency pair first"));
        }
        JSONObject json=new JSONObject();
        json.put("method","depth.subscribe");
        JSONArray jsonArray=new JSONArray();
        jsonArray.add(currencyPair.getParsing());
        jsonArray.add(args.length<1?5:args[0]);
        jsonArray.add(args.length<2?"0":args[1]);
        json.put("params",jsonArray);
        json.put("id",new Random().nextInt(1000000000));
        return streamingService.subscribeChannel("depth.update",json.toJSONString()).map(
                e->{
                    toDepthMap(JSONObject.parseObject(e.toString()));
                    JsonNode param=e.get("params").get(1);
                    Date date=new Date(param.get("time").asLong());
                    List<LimitOrder> listAsks = new ArrayList<>();
                    asksMap.forEach((k,v)->{
                        listAsks.add(new LimitOrder(Order.OrderType.ASK,v,currencyPair.getCurrencyPair(),null,date,k));

                    });
                    List<LimitOrder> listBids = new ArrayList<>();
                    bidsMap.forEach((k,v)->{
                        listBids.add(new LimitOrder(Order.OrderType.BID,v,currencyPair.getCurrencyPair(),null,date,k));
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


    /**
     * depth map格式的缓存 <price, quantity>
     */
    private final Map<BigDecimal, BigDecimal> asksMap = new TreeMap<>(new Comparator<BigDecimal>() {
        @Override
        public int compare(BigDecimal var1, BigDecimal var2) {
            if (var1.compareTo(var2) == 0) {
                return 0;
            }
            return var1.compareTo(var2) == -1 ? -1 : 1;
        }
    });

    private final Map<BigDecimal, BigDecimal> bidsMap = new TreeMap<>(new Comparator<BigDecimal>() {
        @Override
        public int compare(BigDecimal var1, BigDecimal var2) {
            if (var1.compareTo(var2) == 0) {
                return 0;
            }
            return var1.compareTo(var2) == -1 ? 1 : -1;
        }
    });


    private void toDepthMap(JSONObject depth) {
        JSONArray params = depth.getJSONArray("params");
        Boolean bComplete = params.getBoolean(0);
        JSONArray asks = params.getJSONObject(1).getJSONArray("asks");
        JSONArray bids = params.getJSONObject(1).getJSONArray("bids");

        if (bComplete) {
            asksMap.clear();
            bidsMap.clear();
        }

        int size = asks == null ? 0 : asks.size();
        for (int i = 0; i < size; i++) {
            JSONArray level = asks.getJSONArray(i);
            if (BigDecimal.ZERO.compareTo(level.getBigDecimal(1)) == 0) {
                asksMap.remove(level.getBigDecimal(0));
                continue;
            }
            asksMap.put(level.getBigDecimal(0), level.getBigDecimal(1));
        }
        size = bids == null ? 0 : bids.size();
        for (int i = 0; i < size; i++) {
            JSONArray level = bids.getJSONArray(i);
            if (BigDecimal.ZERO.compareTo(level.getBigDecimal(1)) == 0) {
                bidsMap.remove(level.getBigDecimal(0));
                continue;
            }
            bidsMap.put(level.getBigDecimal(0), level.getBigDecimal(1));
        }

//        log.info("ask1 price -> {} quantity -> {} ",
//                asksMap.entrySet().iterator().next().getKey(),
//                asksMap.entrySet().iterator().next().getValue());
//        log.info("bid1 price -> {} quantity -> {} ",
//                bidsMap.entrySet().iterator().next().getKey(),
//                bidsMap.entrySet().iterator().next().getValue());
    }



}
