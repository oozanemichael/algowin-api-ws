package org.mh.exchange.bibox.parsing;

import org.mh.stream.exchange.core.TradingArea;
import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingParsing;


public class BiboxStreamingParsing extends StreamingParsing {
    
    public BiboxStreamingParsing(TradingArea tradingArea) {
        super(tradingArea);
    }

    @Override
    public ParsingCurrencyPair instanceFutures(CurrencyPair currencyPair, Object... args) {
        return new ParsingCurrencyPair(4+currencyPair.base.toString()+"_"+currencyPair.counter.toString(),currencyPair);

    }
    

}
