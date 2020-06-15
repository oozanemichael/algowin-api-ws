package org.mh.stream.exchange.core;

import lombok.extern.log4j.Log4j2;

/**
 * 交易类型
 * */
@Log4j2
public enum  TransactionType {

    //期权(option):0,期货(futures):1,现货(spot):2
    OPTION("option"),
    FUTURES("futures"),
    SPOT("spot");


    private final String value;
    TransactionType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }


}
