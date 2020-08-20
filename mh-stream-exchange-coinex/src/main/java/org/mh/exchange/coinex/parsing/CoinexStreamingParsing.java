package org.mh.exchange.coinex.parsing;


import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingParsing;
import org.mh.stream.exchange.core.TradingArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinexStreamingParsing extends StreamingParsing {

    private static final Logger log = LoggerFactory.getLogger(CoinexStreamingParsing.class);

    public CoinexStreamingParsing(TradingArea tradingArea) {
        super(tradingArea);
    }

    @Override
    public ParsingCurrencyPair instanceFutures(CurrencyPair currencyPair,Object... args) {
        return new ParsingCurrencyPair(currencyPair.base.toString()+currencyPair.counter.toString(),currencyPair);
    }

    public static void main(String[] args) {
        CoinexStreamingParsing coinexStreamingParsing=new CoinexStreamingParsing(TradingArea.Futures);
        log.info("{}",coinexStreamingParsing.parsing(CurrencyPair.BTC_USDT));

    }

}
