package org.mh.exchange.bibox;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import org.mh.service.netty.JsonNettyStreamingService;

import java.io.IOException;
import java.util.Objects;

public class BiboxStreamingService extends JsonNettyStreamingService {


    public BiboxStreamingService(String apiUrl) {
        super(apiUrl);
    }

    @Override
    public void messageHandler(String message) {
        JsonNode jsonNode = null;
        try {
            jsonNode=objectMapper.readTree(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert jsonNode != null;
        if (jsonNode.hasNonNull("ping")){
            JSONObject rsp = new JSONObject();
            rsp.put("pong", jsonNode.findValue("ping").asLong());
            this.sendMessage(rsp.toJSONString());
        }
        super.messageHandler(message);

    }

    @Override
    protected String getChannelNameFromMessage(JsonNode message) throws IOException {
        if (Objects.nonNull(message.get("pong"))){
            return message.get("pong").asText();
        }else if (Objects.nonNull(message.get("ping"))){
            return message.get("ping").asText();
        }else{
            return message.get("channel").asText();
        }
    }

    @Override
    public String getSubscribeMessage(String channelName, Object... args) throws IOException {
        return (String) args[0];
    }

    @Override
    public String getUnsubscribeMessage(String channelName) throws IOException {
        JSONObject rsp = new JSONObject();
        rsp.put("event", "removeChannel");
        rsp.put("channel",channelName);
        return rsp.toJSONString();
    }
}
