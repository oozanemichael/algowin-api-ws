package org.mh.exchange.deribit.parsing;

import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;

public class DeribitStreamingFuturesParsing implements StreamingParsingCurrencyPair {

    @Override
    public ParsingCurrencyPair parsing(CurrencyPair currencyPair, Object... args) {
        if (currencyPair.counter.toString().equals("USD")) {
            return new ParsingCurrencyPair(currencyPair.base + "-PERPETUAL",currencyPair);
        }
        throw new IllegalArgumentException("There is no corresponding currency pair on the exchange");
    }
}
