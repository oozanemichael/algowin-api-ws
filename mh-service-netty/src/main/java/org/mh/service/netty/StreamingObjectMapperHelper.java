package org.mh.service.netty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class should be merged with ObjectMapperHelper from XStream..
 *
 * @author Nikita Belenkiy on 19/06/2018.
 */
public class StreamingObjectMapperHelper {

  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    //有个别请求参数不对应,也会将请求参数正常解析为相应对象，不会抛出400
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //Number反序列化BigDecimal
    objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    //为NULL或者为空不参加序列化
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    //属性为默认值不序列化
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
  }

  private StreamingObjectMapperHelper() {}

  public static ObjectMapper getObjectMapper() {
    return objectMapper;
  }
}
