package org.mh.exchange.coinex;

import io.reactivex.Completable;
import org.knowm.xchange.coinex.CoinexExchange;
import org.mh.exchange.coinex.parsing.CoinexStreamingParsing;
import org.mh.stream.exchange.core.*;

public class CoinexStreamingExchange extends CoinexExchange implements StreamingExchange {

    private static final String API_URI = "wss://socket.coinex.com/";
    private static final String PERPETUAL_API_URI = "wss://perpetual.coinex.com/";

    CoinexStreamingService streamingService;

    StreamingParsingCurrencyPair parsingCurrencyPair;

    CoinexStreamingMarketDataService marketDataService;

    public CoinexStreamingExchange(){

    }

    @Override
    public void instance(TradingArea tradingArea) {
        streamingService=new CoinexStreamingService(PERPETUAL_API_URI);
        marketDataService=new CoinexStreamingMarketDataService(streamingService);
        parsingCurrencyPair=new CoinexStreamingParsing(tradingArea);
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
