package org.mh.service.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.time.Duration;

@Log4j2
public abstract class JsonNettyStreamingService extends NettyStreamingService<JsonNode> {

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
      // In case of array - handle every message separately.
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
