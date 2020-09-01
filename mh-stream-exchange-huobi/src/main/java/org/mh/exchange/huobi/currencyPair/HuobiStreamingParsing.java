package org.mh.exchange.huobi.currencyPair;

import org.mh.stream.exchange.core.TradingArea;
import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingParsing;

public class HuobiStreamingParsing extends StreamingParsing {

    public HuobiStreamingParsing(TradingArea tradingArea) {
        super(tradingArea);
    }

    @Override
    public ParsingCurrencyPair instanceMargin(CurrencyPair currencyPair, Object... args) {
        return new ParsingCurrencyPair(currencyPair.base.toString().toLowerCase()+currencyPair.counter.toString().toLowerCase(),currencyPair);
    }


}
