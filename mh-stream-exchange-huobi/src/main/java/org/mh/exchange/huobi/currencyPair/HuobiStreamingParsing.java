package org.mh.exchange.huobi.currencyPair;

import org.market.hedge.core.TradingArea;
import org.knowm.xchange.currency.CurrencyPair;
import org.market.hedge.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingParsing;

public class HuobiStreamingParsing extends StreamingParsing {

    public HuobiStreamingParsing(TradingArea tradingArea) {
        super(tradingArea);
    }

    @Override
    public ParsingCurrencyPair instanceMargin(CurrencyPair currencyPair, Object... args) {
        return new ParsingCurrencyPair(currencyPair.base.toString().toLowerCase()+currencyPair.counter.toString().toLowerCase(),currencyPair);
    }

    @Override
    public ParsingCurrencyPair instanceSpot(CurrencyPair currencyPair, Object... args) {
        return new ParsingCurrencyPair(null,currencyPair, args);
    }


    @Override
    public ParsingCurrencyPair instancePerpetualSwap(CurrencyPair currencyPair, Object... args) {
        return new ParsingCurrencyPair(currencyPair.base.toString()+"-"+currencyPair.counter.toString(),currencyPair, args);
    }




}
