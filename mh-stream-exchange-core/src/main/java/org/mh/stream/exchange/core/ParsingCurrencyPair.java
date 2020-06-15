package org.mh.stream.exchange.core;

import lombok.Data;
import org.knowm.xchange.currency.CurrencyPair;

@Data
public class ParsingCurrencyPair {

    /**
     * 解析后供交易所用的货币对名称
     * */
    String parsing;

    CurrencyPair currencyPair;


    public ParsingCurrencyPair(String parsing,CurrencyPair currencyPair){
        this.parsing=parsing;
        this.currencyPair=currencyPair;
    }

}