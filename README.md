# mh-exchange-ws 交易所websocket对接

## market data

| 交易所 | orderBook | ticker |
| :-----| :----- | :----- |
| bibox | √ | × |
| coinex | √ | × |
| deribit | √ | × |
| huobi | √ | × |
| hbtc | √ | × |

### coinex 
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
