package org.mh.exchange.huobi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

public class JsonNodeTest {

    private static final Logger log = LoggerFactory.getLogger(JsonNodeTest.class);

    public static void main(String[] args) {
        String json=new String("{\n" +
                "  \"ch\": \"market.htusdt.depth.step0\",\n" +
                "  \"ts\": 1572362902027,\n" +
                "  \"tick\": {\n" +
                "    \"bids\": [\n" +
                "      [3.7721, 344.86],\n" +
                "      [3.7709, 46.66] \n" +
                "    ],\n" +
                "    \"asks\": [\n" +
                "      [3.7745, 15.44],\n" +
                "      [3.7746, 70.52]\n" +
                "    ],\n" +
                "    \"version\": 100434317651,\n" +
                "    \"ts\": 1572362902012 \n" +
                "  }\n" +
                "}");
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(json);
            jsonNode.get("tick").get("bids").forEach(e->{
                log.info("{}",e);
                log.info("{}",e.get(0));
                log.info("{}",e.get(1));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
