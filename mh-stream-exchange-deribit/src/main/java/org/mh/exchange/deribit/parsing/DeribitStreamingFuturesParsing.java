package org.mh.exchange.deribit.parsing;

import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeribitStreamingFuturesParsing implements StreamingParsingCurrencyPair {

    /**
     * @param args args[0] 期权到期日  true  date
     * */
    @Override
    public ParsingCurrencyPair parsing(CurrencyPair currencyPair, Object... args) {
        if (args.length==0){
            if (currencyPair.counter.toString().equals("USD")) {
                return new ParsingCurrencyPair(currencyPair.base + "-PERPETUAL",currencyPair);
            }
        }else {
            DateFormat df1dd = new SimpleDateFormat("dd", Locale.ENGLISH);
            DateFormat df1MMM = new SimpleDateFormat("MMM", Locale.ENGLISH);
            DateFormat df1yyyy = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            Date date=(Date) args[0];
            String yyyy=df1yyyy.format(date);
            String dateS=df1dd.format(date)+df1MMM.format(date).toUpperCase()+yyyy.substring(yyyy.length()-2);
            return new ParsingCurrencyPair((currencyPair.base+"-"+dateS),currencyPair);
        }
        throw new IllegalArgumentException("There is no corresponding currency pair on the exchange");
    }
}
