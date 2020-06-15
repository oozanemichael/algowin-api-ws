package org.mh.exchange.deribit.parsing;

import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.mh.stream.exchange.core.ParsingCurrencyPair;
import org.mh.stream.exchange.core.StreamingParsingCurrencyPair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * deribit 期权
 * */
@Log4j2
public class DeribitStreamingOptionParsing implements StreamingParsingCurrencyPair {

    /**
     * args[0] 期权到期日  true  date
     * args[1] Strike  true  integer
     * args[2] 看跌Put或看涨Call  true  integer  -1 = Put, 1 = Call
     * */
    @Override
    public ParsingCurrencyPair parsing(CurrencyPair currencyPair, Object... args) {
        DateFormat df1dd = new SimpleDateFormat("dd", Locale.ENGLISH);
        DateFormat df1MMM = new SimpleDateFormat("MMM", Locale.ENGLISH);
        DateFormat df1yyyy = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        Date date=(Date) args[0];
        String yyyy=df1yyyy.format(date);
        String dateS=df1dd.format(date)+df1MMM.format(date).toUpperCase()+yyyy.substring(yyyy.length()-2);
        Integer strike=(Integer)args[1];
        String direction=(Integer)args[2]==-1?"P":"C";
        return new ParsingCurrencyPair((currencyPair.base+"-"+dateS+"-"+strike+"-"+direction),currencyPair);
    }


}
