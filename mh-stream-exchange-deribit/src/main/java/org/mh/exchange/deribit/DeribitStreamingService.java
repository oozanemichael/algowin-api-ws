package org.mh.exchange.deribit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import org.mh.service.netty.JsonNettyStreamingService;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class DeribitStreamingService extends JsonNettyStreamingService {

    public DeribitStreamingService(String apiUrl) {
        super(apiUrl);
    }


    @Override
    protected String getChannelNameFromMessage(JsonNode message) throws IOException {
        JsonNode id=message.findValue("id");
        if (Objects.nonNull(id)){
            return id.asText();
        }
        return message.get("params").get("channel").asText();
    }

    /**
     * args[0] (method  subscribe/unsubscribe)  true  String
     * */
    @Override
    public String getSubscribeMessage(String channelName, Object... args) throws IOException {
        JSONObject json=new JSONObject();
        json.put("jsonrpc","2.0");
        json.put("method",(String)args[0]);
        json.put("id",new Random().nextInt(1000000000));
        JSONObject params=new JSONObject();
        JSONArray channels=new JSONArray();
        channels.add(channelName);
        params.put("channels",channels);
        json.put("params", params);
        return json.toJSONString();
    }

    @Override
    public String getUnsubscribeMessage(String channelName) throws IOException {
        return null;
    }
}
