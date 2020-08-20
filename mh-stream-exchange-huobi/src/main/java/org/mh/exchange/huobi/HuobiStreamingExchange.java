package org.mh.exchange.huobi;

import io.reactivex.Completable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.huobi.HuobiExchange;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.mh.exchange.huobi.currencyPair.HuobiStreamingParsing;
import org.mh.stream.exchange.core.*;
import si.mazi.rescu.SynchronizedValueFactory;

import java.io.IOException;
import java.util.List;

public class HuobiStreamingExchange extends HuobiExchange implements StreamingExchange {

    /**
     * 行情请求地址
     * */
    private static final String API_URI = "wss://api.huobi.pro/ws";
    private static final String AWS_API_URI = "wss://api-aws.huobi.pro/ws";

    private HuobiStreamingService streamingService;

    private HuobiStreamingMarketDataService streamingMarketDataService;

    private StreamingParsingCurrencyPair parsingCurrencyPair;


    @Override
    public void instance(TradingArea tradingArea) {
        this.streamingService=new HuobiStreamingService(AWS_API_URI);
        this.streamingMarketDataService = new HuobiStreamingMarketDataService(streamingService, this);
        this.parsingCurrencyPair = new HuobiStreamingParsing(tradingArea);
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
    public ExchangeSpecification getExchangeSpecification() {
        return null;
    }

    @Override
    public ExchangeMetaData getExchangeMetaData() {
        return null;
    }

    @Override
    public List<CurrencyPair> getExchangeSymbols() {
        return null;
    }

    @Override
    public SynchronizedValueFactory<Long> getNonceFactory() {
        return null;
    }

    @Override
    public ExchangeSpecification getDefaultExchangeSpecification() {
        return null;
    }

    @Override
    public void applySpecification(ExchangeSpecification exchangeSpecification) {

    }

    @Override
    public MarketDataService getMarketDataService() {
        return null;
    }

    @Override
    public TradeService getTradeService() {
        return null;
    }

    @Override
    public AccountService getAccountService() {
        return null;
    }

    @Override
    public void remoteInit() throws IOException, ExchangeException {

    }
}
