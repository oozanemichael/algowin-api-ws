package org.mh.exchange.bibox.parsing;

import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;

public class BiboxStreamingFuturesParsing implements StreamingParsingCurrencyPair {

    @Override
    public ParsingCurrencyPair parsing(CurrencyPair currencyPair, Object... args) {
        return new ParsingCurrencyPair(4+currencyPair.base.toString()+"_"+currencyPair.counter.toString(),currencyPair);
    }

}
