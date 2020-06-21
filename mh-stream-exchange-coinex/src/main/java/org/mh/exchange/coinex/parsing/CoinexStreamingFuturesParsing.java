package org.mh.exchange.coinex.parsing;

import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;

@Log4j2
public class CoinexStreamingFuturesParsing implements StreamingParsingCurrencyPair {

    @Override
    public ParsingCurrencyPair parsing(CurrencyPair currencyPair, Object... args) {
        return new ParsingCurrencyPair(currencyPair.base.toString()+currencyPair.counter.toString(),currencyPair);
    }

    public static void main(String[] args) {
        CurrencyPair currencyPair=CurrencyPair.ETH_USD;
        log.warn(currencyPair.base.toString()+"___"+currencyPair.counter.toString());
        currencyPair=CurrencyPair.ETH_USDT;
        log.warn(currencyPair.base.toString()+"___"+currencyPair.counter.toString());
    }

}
