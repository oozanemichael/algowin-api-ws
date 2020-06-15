package org.mh.exchange.huobi.currencyPair;

import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;

public class HuobiStreamingSpotParsing implements StreamingParsingCurrencyPair {

    @Override
    public ParsingCurrencyPair parsing(CurrencyPair currencyPair, Object... args) {
        return new ParsingCurrencyPair(currencyPair.base.toString().toLowerCase()+currencyPair.counter.toString().toLowerCase(),currencyPair);
    }
}
