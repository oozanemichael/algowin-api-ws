package org.mh.exchange.deribit.parsing;

import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingParsing;
import org.mh.stream.exchange.core.TradingArea;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeribitStreamingParsing extends StreamingParsing {

    public DeribitStreamingParsing(TradingArea tradingArea) {
        super(tradingArea);
    }


    /**
     * @param args args[0] 期权到期日  true  date
     *             Dec 12月；Feb 2月，baiApr 4月，Jun 6月，Aug 8月，Oct 10月， Jul 7月，Jan 1月，May 5月，Mar 3月，Nov 11月， sep 9月！du
     * */
    @Override
    public ParsingCurrencyPair instanceFutures(CurrencyPair currencyPair, Object... args) {
        if (args.length==0){
            if (currencyPair.counter.toString().equals("USD")) {
                return new ParsingCurrencyPair(currencyPair.base + "-PERPETUAL",currencyPair);
            }
        }else {
            return new ParsingCurrencyPair((currencyPair.base+"-"+DateFormatUtil.format((Date) args[0])),currencyPair);
        }
        throw new IllegalArgumentException("There is no corresponding currency pair on the exchange");
    }


    /**
     * @param args args[0] 期权到期日  true  Date
     *             args[1] Strike  true  integer
     *             args[2] 看跌Put或看涨Call  true  integer  -1 = Put, 1 = Call
     * */
    @Override
    public ParsingCurrencyPair instanceOptions(CurrencyPair currencyPair, Object... args) {
        Integer strike=(Integer)args[1];
        String direction=(Integer)args[2]==-1?"P":"C";
        return new ParsingCurrencyPair((currencyPair.base+"-"+DateFormatUtil.format((Date) args[0])+"-"+strike+"-"+direction),currencyPair);
    }
}
