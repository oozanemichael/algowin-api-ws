package org.mh.exchange.binance;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mh.exchange.binance.dto.BaseBinanceWebSocketTransaction.BinanceWebSocketTypes;
import io.reactivex.Observable;
import org.mh.service.netty.JsonNettyStreamingService;

import java.io.IOException;

public class BinanceUserDataStreamingService extends JsonNettyStreamingService {

  private static final Logger log = LoggerFactory.getLogger(BinanceUserDataStreamingService.class);

  private static final String USER_API_BASE_URI = "wss://stream.binance.com:9443/ws/";

  public static org.mh.exchange.binance.BinanceUserDataStreamingService create(String listenKey) {
    return new org.mh.exchange.binance.BinanceUserDataStreamingService(USER_API_BASE_URI + listenKey);
  }

  private BinanceUserDataStreamingService(String url) {
    super(url, Integer.MAX_VALUE);
  }

  public Observable<JsonNode> subscribeChannel(BinanceWebSocketTypes eventType) {
    return super.subscribeChannel(eventType.getSerializedValue());
  }

  @Override
  public void messageHandler(String message) {
    log.debug("Received message: {}", message);
    super.messageHandler(message);
  }

  @Override
  protected void handleMessage(JsonNode message) {
    try {
      super.handleMessage(message);
    } catch (Exception e) {
      log.error("Error handling message: " + message, e);
      return;
    }
  }

  @Override
  protected String getChannelNameFromMessage(JsonNode message) throws IOException {
    return message.get("e").asText();
  }

  @Override
  public String getSubscribeMessage(String channelName, Object... args) throws IOException {
    // No op. Disconnecting from the web socket will cancel subscriptions.
    return null;
  }

  @Override
  public String getUnsubscribeMessage(String channelName) throws IOException {
    // No op. Disconnecting from the web socket will cancel subscriptions.
    return null;
  }

  @Override
  public void sendMessage(String message) {
    // Subscriptions are made upon connection - no messages are sent.
  }
}
