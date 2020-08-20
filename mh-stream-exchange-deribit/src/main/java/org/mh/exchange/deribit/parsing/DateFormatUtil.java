package org.mh.exchange.deribit.parsing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtil {

    public static String format(Date date){
        DateFormat df1dd = new SimpleDateFormat("dd", Locale.ENGLISH);
        DateFormat df1MMM = new SimpleDateFormat("MMM", Locale.ENGLISH);
        DateFormat df1yyyy = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String yyyy=df1yyyy.format(date);
        return df1dd.format(date)+df1MMM.format(date).toUpperCase()+yyyy.substring(yyyy.length()-2);
    }

}
