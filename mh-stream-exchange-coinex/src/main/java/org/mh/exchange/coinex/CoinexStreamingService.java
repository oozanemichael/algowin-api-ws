package org.mh.exchange.coinex;

import com.fasterxml.jackson.databind.JsonNode;
import org.mh.service.netty.JsonNettyStreamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CoinexStreamingService extends JsonNettyStreamingService {

    private static final Logger log=LoggerFactory.getLogger(CoinexStreamingService.class);

    public CoinexStreamingService(String apiUrl) {
        super(apiUrl);
    }

    @Override
    protected String getChannelNameFromMessage(JsonNode message) throws IOException {
        //log.info("{}",message);
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
