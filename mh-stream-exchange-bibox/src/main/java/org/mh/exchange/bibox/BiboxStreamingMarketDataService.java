package org.mh.exchange.bibox;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Observable;

import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.mh.service.netty.util.GZIPUtils;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingMarketDataService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BiboxStreamingMarketDataService implements StreamingMarketDataService {


    private final BiboxStreamingService streamingService;

    public BiboxStreamingMarketDataService(BiboxStreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @Override
    public Observable<OrderBook> getOrderBook(ParsingCurrencyPair currencyPair, Object... args) {
        JSONObject json=new JSONObject();
        json.put("event","addChannel");
        String channel="bibox_sub_spot_"+currencyPair.getParsing()+"_depth";
        json.put("channel",channel);
        return streamingService.subscribeChannel(channel,json.toJSONString())
                .map(e->{
                    JsonNode dataGZIP=e.get("data");
                    JsonNode data=new ObjectMapper().readTree(GZIPUtils.uncompress(dataGZIP.asText()));
                    List<LimitOrder> listBids = new ArrayList<>();
                    List<LimitOrder> listAsks = new ArrayList<>();
                    Date date=new Date(data.get("update_time").asLong());
                    data.get("bids").forEach(
                            bids->{
                                listBids.add(new LimitOrder(Order.OrderType.BID,
                                        new BigDecimal(bids.get("volume").asText()),
                                        currencyPair.getCurrencyPair(),
                                        null,
                                        date,
                                        new BigDecimal(bids.get("price").asText())));
                            }
                    );
                    data.get("asks").forEach(
                            asks->{
                                listAsks.add(new LimitOrder(Order.OrderType.ASK,
                                        new BigDecimal(asks.get("volume").asText()),
                                        currencyPair.getCurrencyPair(),
                                        null,
                                        date,
                                        new BigDecimal(asks.get("price").asText())));
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
