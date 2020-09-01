# mh-exchange-ws 交易所websocket对接
[toc]

## market data

| 交易所 | orderBook | ticker | trades |
| :-----| :----- | :----- | :----- |
| bibox | √ | × | × |
| coinex | √ | × | × |
| deribit | √ | × | × |
| huobi | √ | × | × |
| hbtc | √ | × | × |
| binance | √ | × | × |
<br></br>

eg:
```
//订阅Coinex 永续 BTC_USD
StreamingExchange exchange =
        StreamingExchangeFactory.INSTANCE.createExchange(CoinexStreamingExchange.class.getName(), TradingArea.Futures);
StreamingParsingCurrencyPair parsing=exchangess.getStreamingParsingCurrencyPair();
exchange.connect().blockingAwait();
Observable<OrderBook> observable=exchanges.getStreamingMarketDataService().getOrderBook(parsing.parsing(CurrencyPair.BTC_USD));
Disposable disposable=observabless.subscribe(o -> {
    log.info("ask: "+o.getAsks());
    log.warn("bid: "+o.getBids());
});

//取消订阅
disposable.dispose();
```

# coinex 
+ orderBook 
一次连接只能订阅一种币对
```
      {
        "method":"depth.subscribe",
        "params":[
          "BTCBCH",           #1.market: See<API invocation description·market>
          20,                 #2.limit: Count limit, Integer
          "0"                 #3.interval: Merge，String
        ],
        "id":15
      }
     
      @param args args[0] limit: Count limit, Integer
                  args[1] interval: Merge，String

```

# deribit

+ orderBook 

book.{instrument_name}.{group}.{depth}.{interval}
     
| Parameter | Required | Type | Enum | Description |
| :-----| :----- | :----- | :----- | :----- |
| instrument_name | true | string |  | Instrument name |
| group | true | string | 	none 1/ 2/ 5/ 10/ 25/ 100/ 250 | Allowed values for BTC - none, 1, 2, 5, 10 , Allowed values for ETH - none, 5, 10, 25, 100, 250 |
| depth | true | integer | 1,10,20 | Number of price levels to be included. |
| interval | true | string | 100ms | Frequency of notifications. Events will be aggregated over this interval. |
    
      channel eg: "book.ETH-PERPETUAL.100.1.100ms";

```
     @param args args[0] depth Integer	1/10/20	Number of price levels to be included.
```

# binance



