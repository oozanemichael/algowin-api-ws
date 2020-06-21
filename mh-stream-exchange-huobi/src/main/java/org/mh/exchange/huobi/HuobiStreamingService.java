package org.mh.exchange.huobi;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.mh.service.netty.JsonNettyStreamingService;
import org.mh.service.netty.util.GZIPUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Log4j2
public class HuobiStreamingService extends JsonNettyStreamingService {

    public HuobiStreamingService(String apiUrl) {
        super(apiUrl,Integer.MAX_VALUE);
    }

    @Override
    public void messageHandler(String message) {
        super.messageHandler(message);
    }

    @Override
    public void messageHandler(byte[] message) {
        String output = null;
        try {
            output = GZIPUtils.decompressGzip(message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        JsonNode jsonNode = null;
        // 将传入消息解析为JSON
        try {
            jsonNode = objectMapper.readTree(output);
        } catch (IOException e) {
            log.error("Error parsing incoming message to JSON: {}", output);
        }
        assert jsonNode != null;
        JsonNode ping=jsonNode.findValue("ping");
        if (Objects.nonNull(ping)){
            JSONObject rsp = new JSONObject();
            rsp.put("pong", ping.asText());
            this.sendMessage(rsp.toJSONString());
        }
        super.messageHandler(message);
    }


    @Override
    protected String getChannelNameFromMessage(JsonNode message) throws IOException {
        if (Objects.nonNull(message.get("subbed"))){
            return message.get("status").asText();
        }if (Objects.nonNull(message.get("unsubbed"))){
            return message.get("status").asText();
        }if (Objects.nonNull(message.get("ping"))){
            return message.get("ping").asText();
        }else{
            return message.get("ch").asText();
        }
    }

    @Override
    public String getSubscribeMessage(String channelName, Object... args) throws IOException {
        JSONObject json= (JSONObject) args[0];
        return json.toJSONString();
    }

    @Override
    public String getUnsubscribeMessage(String channelName) throws IOException {
        JSONObject json=new JSONObject();
        json.put("unsub",channelName);
        json.put("id", UUID.randomUUID().toString());
        return json.toJSONString();
    }


}
