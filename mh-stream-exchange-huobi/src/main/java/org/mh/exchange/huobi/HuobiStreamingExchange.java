package org.mh.exchange.huobi;

import io.netty.channel.ChannelHandlerContext;
import io.reactivex.Completable;

import io.reactivex.Observable;
import org.market.hedge.core.TradingArea;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.huobi.HuobiExchange;
import org.market.hedge.exception.NullTradingAreaException;
import org.market.hedge.service.StreamingParsingCurrencyPair;
import org.mh.exchange.huobi.currencyPair.HuobiStreamingParsing;
import org.mh.stream.exchange.core.*;
import si.mazi.rescu.SynchronizedValueFactory;

import java.io.IOException;

public class HuobiStreamingExchange extends HuobiExchange implements StreamingExchange {

    /**
     * 行情请求地址
     * */
    private static final String API_URI = "wss://api.huobi.pro/ws";
    private static final String AWS_Spot_API_URI = "wss://api-aws.huobi.pro/ws";
    private static final String Swap_API_URI ="wss://api.hbdm.com/swap-ws";
    private static final String Swap_API_URI_btcgateway ="wss://api.btcgateway.pro/swap-ws";

    private HuobiStreamingService streamingService;

    private HuobiStreamingMarketDataService streamingMarketDataService;

    private StreamingParsingCurrencyPair parsingCurrencyPair;


    @Override
    public void instance(TradingArea tradingArea) {
        this.parsingCurrencyPair = new HuobiStreamingParsing(tradingArea);
        switch (tradingArea){
            case Margin:
                this.streamingService=new HuobiStreamingService(AWS_Spot_API_URI);
                this.streamingMarketDataService = new HuobiStreamingMarketDataService(streamingService, this);
                break;
            case PerpetualSwap:
                this.streamingService=new HuobiStreamingService("wss://api.btcgateway.pro/swap-ws");
                this.streamingMarketDataService = new HuobiStreamingMarketDataService(streamingService, this);
                break;
            default:
                throw new NullTradingAreaException(tradingArea);
        }

    }

    @Override
    public Completable connect(ProductSubscription... args) {
        return streamingService.connect();
    }

    @Override
    public Completable disconnect() {
        return streamingService.disconnect();
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public Observable<Throwable> reconnectFailure() {
        return null;
    }

    @Override
    public Observable<Object> connectionSuccess() {
        return null;
    }

    @Override
    public Observable<ChannelHandlerContext> disconnectObservable() {
        return null;
    }

    @Override
    public Observable<Long> messageDelay() {
        return null;
    }

    @Override
    public StreamingMarketDataService getStreamingMarketDataService() {
        return streamingMarketDataService;
    }

    @Override
    public StreamingParsingCurrencyPair getStreamingParsingCurrencyPair() {
        return parsingCurrencyPair;
    }

    @Override
    public void useCompressedMessages(boolean compressedMessages) {

    }


    @Override
    public SynchronizedValueFactory<Long> getNonceFactory() {
        return null;
    }


    @Override
    public void remoteInit() throws IOException, ExchangeException {

    }
}
