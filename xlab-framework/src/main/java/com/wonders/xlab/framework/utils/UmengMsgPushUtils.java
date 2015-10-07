package com.wonders.xlab.framework.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonders.xlab.framework.entity.umengpush.MessagePush;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by wangqiang on 15/9/24.
 */
public final class UmengMsgPushUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmengMsgPushUtils.class);

    /**
     * 应用唯一标识。友盟消息推送服务提供的appkey和友盟统计分析平台使用的同一套appkey。
     */
    private static final String APP_KEY;

    /**
     * 服务器秘钥，用于服务器端调用API请求时对发送内容做签名验证。
     */
    private static final String APP_MASTER_SECRET;

    private static final String UMENG_MSG_API_URL = "http://msg.umeng.com/api/send";
    private static final ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
    private static final RestTemplate restTemplate = new RestTemplate();

    static {
        Properties props = new Properties();
        try (InputStream stream = UmengMsgPushUtils.class.getResourceAsStream("/umeng-msg-push.properties")) {
            if (stream == null) {
                throw new RuntimeException("找不到相应的配置文件！[classpath:/umeng-msg-push.properties]");
            }
            props.load(stream);
            APP_KEY = props.getProperty("appKey");
            APP_MASTER_SECRET = props.getProperty("app_master_secret");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private UmengMsgPushUtils() {

    }

    public static boolean unicastNotification(String deviceToken, String ticker, String title, String text) {

        MessagePush msgPush = new MessagePush();
        msgPush.setAppKey(APP_KEY);
        msgPush.setType("unicast");
        msgPush.setDeviceTokens(deviceToken);

        MessagePush.Payload.Body body = new MessagePush.Payload.Body(ticker, title, text);
        MessagePush.Payload payload = new MessagePush.Payload("notification", body);
        msgPush.setPayload(payload);

        String response = restTemplate.postForObject(UMENG_MSG_API_URL + "?sign=" + generateSign(msgPush),
                msgPush, String.class);
        try {
            Map respRet = objectMapper.readValue(response, Map.class);
            return StringUtils.equalsIgnoreCase("SUCCESS", (String) respRet.get("ret"));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static String generateSign(MessagePush messagePush) {
        try {
            String postBody = objectMapper.writeValueAsString(messagePush);
            return DigestUtils.md5Hex("POST" + UMENG_MSG_API_URL + postBody + APP_MASTER_SECRET);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        boolean result = unicastNotification("AmXXGYDMC6yynJAFa8Z5Wb0CP6n4lBiJ9sljd8Q1wH5T",
                "Hello World!", "你好，世界!!!!", "我们的筛选条件非常灵活，支持逻辑上的and(与), or(或), not(非)操作, 以及这些操作的组合。具体请参照下面的示例。");
        System.out.println("result = " + result);
    }

}