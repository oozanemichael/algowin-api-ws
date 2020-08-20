package org.mh.service.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mh.service.netty.util.GZIPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

public abstract class JsonNettyStreamingService extends NettyStreamingService<JsonNode> {

  private static final Logger log = LoggerFactory.getLogger(JsonNettyStreamingService.class);

  protected final ObjectMapper objectMapper = StreamingObjectMapperHelper.getObjectMapper();

  public JsonNettyStreamingService(String apiUrl) {
    super(apiUrl);
  }

  public JsonNettyStreamingService(String apiUrl, int maxFramePayloadLength) {
    super(apiUrl, maxFramePayloadLength);
  }

  public JsonNettyStreamingService(
      String apiUrl,
      int maxFramePayloadLength,
      Duration connectionTimeout,
      Duration retryDuration,
      int idleTimeoutSeconds) {
    super(apiUrl, maxFramePayloadLength, connectionTimeout, retryDuration, idleTimeoutSeconds);
  }

  public boolean processArrayMassageSeparately() {
    return true;
  }

  @Override
  public void messageHandler(String message) {
    log.debug("Received message: {}", message);
    JsonNode jsonNode;

    // Parse incoming message to JSON
    try {
      jsonNode = objectMapper.readTree(message);
    } catch (IOException e) {
      log.error("Error parsing incoming message to JSON: {}", message);
      return;
    }

    if (processArrayMassageSeparately() && jsonNode.isArray()) {
      // 如果是数组，分别处理每条消息。
      for (JsonNode node : jsonNode) {
        handleMessage(node);
      }
    } else {
      handleMessage(jsonNode);
    }
  }

  @Override
  public void messageHandler(byte[] message) {
    String output = null;
    try {
      output = GZIPUtils.decompressGzip(message);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    log.debug("Received message: {}", output);
    JsonNode jsonNode = null;
    // 将传入消息解析为JSON
    try {
      jsonNode = objectMapper.readTree(output);
    } catch (IOException e) {
      log.error("Error parsing incoming message to JSON: {}", output);
    }
    if (processArrayMassageSeparately() && jsonNode.isArray()) {
      // 如果是数组，分别处理每条消息。
      for (JsonNode node : jsonNode) {
        handleMessage(node);
      }
    } else {
      handleMessage(jsonNode);
    }
  }

  protected void sendObjectMessage(Object message) {
    try {
      sendMessage(objectMapper.writeValueAsString(message));
    } catch (JsonProcessingException e) {
      log.error("Error creating json message: {}", e.getMessage());
    }
  }
}
