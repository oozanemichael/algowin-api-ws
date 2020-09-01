package org.mh.stream.exchange.core;

import org.knowm.xchange.currency.CurrencyPair;

public class ParsingCurrencyPair {

    /**
     * 解析后供交易所用的货币对名称
     * */
    String parsing;

    CurrencyPair currencyPair;

    Object[] objects;

    public String getParsing() {
        return parsing;
    }

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    public void setParsing(String parsing) {
        this.parsing = parsing;
    }

    public void setCurrencyPair(CurrencyPair currencyPair) {
        this.currencyPair = currencyPair;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }

    public ParsingCurrencyPair(String parsing, CurrencyPair currencyPair){
        this.parsing=parsing;
        this.currencyPair=currencyPair;
    }

    public ParsingCurrencyPair(String parsing, CurrencyPair currencyPair,Object[] objects){
        this.parsing=parsing;
        this.currencyPair=currencyPair;
        this.objects=objects;
    }

}