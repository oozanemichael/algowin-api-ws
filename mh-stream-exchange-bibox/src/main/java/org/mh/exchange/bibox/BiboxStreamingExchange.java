package org.mh.exchange.bibox;

import io.reactivex.Completable;
import org.knowm.xchange.bibox.BiboxExchange;
import org.mh.exchange.bibox.parsing.BiboxStreamingFuturesParsing;
import org.mh.stream.exchange.core.ProductSubscription;
import org.mh.stream.exchange.core.StreamingExchange;
import org.mh.stream.exchange.core.StreamingMarketDataService;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;


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
    public void instanceAsFutures() {
        parsingCurrencyPair=new BiboxStreamingFuturesParsing();
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
