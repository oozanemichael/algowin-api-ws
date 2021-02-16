package org.mh.stream.exchange.core;


import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.market.hedge.core.ParsingCurrencyPair;
import org.market.hedge.core.TradingArea;
import org.market.hedge.service.StreamingParsingCurrencyPair;

public class StreamingParsing implements StreamingParsingCurrencyPair{

    public StreamingParsingCurrencyPair parsingCurrencyPair = null;

    public StreamingParsing(TradingArea tradingArea) {
        switch (tradingArea){
            case Futures:
                parsingCurrencyPair= this::instanceFutures;
                break;
            case PerpetualSwap:
                parsingCurrencyPair= this::instancePerpetualSwap;
                break;
            case Option:
                parsingCurrencyPair= this::instanceOptions;
                break;
            case Spot:
                parsingCurrencyPair= this::instanceSpot;
                break;
            case Margin:
                parsingCurrencyPair= this::instanceMargin;
                break;
            default:
                break;
        }
    }

    @Override
    public ParsingCurrencyPair parsing(CurrencyPair currencyPair, Object... args) {
        return parsingCurrencyPair.parsing(currencyPair,args);
    }

    public ParsingCurrencyPair instanceFutures(CurrencyPair currencyPair,Object... args) {
        throw new NotYetImplementedForExchangeException();
    }

    public ParsingCurrencyPair instancePerpetualSwap(CurrencyPair currencyPair,Object... args) {
        throw new NotYetImplementedForExchangeException();
    }

    public ParsingCurrencyPair instanceOptions(CurrencyPair currencyPair,Object... args) {
        throw new NotYetImplementedForExchangeException();
    }

    public ParsingCurrencyPair instanceSpot(CurrencyPair currencyPair,Object... args) {
        throw new NotYetImplementedForExchangeException();
    }

    public ParsingCurrencyPair instanceMargin(CurrencyPair currencyPair,Object... args) {
        throw new NotYetImplementedForExchangeException();
    }


}
