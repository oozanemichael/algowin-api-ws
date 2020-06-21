package org.mh.exchange.coinex;

import com.fasterxml.jackson.databind.JsonNode;
import org.mh.service.netty.JsonNettyStreamingService;

import java.io.IOException;

public class CoinexStreamingService extends JsonNettyStreamingService {


    public CoinexStreamingService(String apiUrl) {
        super(apiUrl);
    }

    @Override
    protected String getChannelNameFromMessage(JsonNode message) throws IOException {
        if(message.hasNonNull("method")){
            return message.get("method").asText();
        }
        return message.get("id").asText();
    }

    @Override
    public String getSubscribeMessage(String channelName, Object... args) throws IOException {
        return (String) args[0];
    }

    @Override
    public String getUnsubscribeMessage(String channelName) throws IOException {
        return null;
    }
}
