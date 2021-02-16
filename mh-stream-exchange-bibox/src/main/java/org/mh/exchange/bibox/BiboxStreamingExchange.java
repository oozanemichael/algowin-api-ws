package org.mh.exchange.bibox;

import io.reactivex.Completable;
import org.knowm.xchange.bibox.BiboxExchange;
import org.market.hedge.core.TradingArea;
import org.market.hedge.service.StreamingParsingCurrencyPair;
import org.mh.exchange.bibox.parsing.BiboxStreamingParsing;
import org.mh.stream.exchange.core.*;


public class BiboxStreamingExchange extends BiboxExchange implements StreamingExchange {

    private static final String API_URI = "wss://push.bibox.com/";

    BiboxStreamingService streamingService;

    StreamingParsingCurrencyPair parsingCurrencyPair;

    BiboxStreamingMarketDataService marketDataService;

    public BiboxStreamingExchange(){
        streamingService=new BiboxStreamingService(API_URI);
        marketDataService=new BiboxStreamingMarketDataService(streamingService);
    }

    public BiboxStreamingService getStreamingService() {
        return streamingService;
    }


    @Override
    public void instance(TradingArea area) {
        parsingCurrencyPair=new BiboxStreamingParsing(area);
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
    public StreamingMarketDataService getStreamingMarketDataService() {
        return marketDataService;
    }

    @Override
    public StreamingParsingCurrencyPair getStreamingParsingCurrencyPair() {
        return parsingCurrencyPair;
    }

    @Override
    public void useCompressedMessages(boolean compressedMessages) {

    }
}
