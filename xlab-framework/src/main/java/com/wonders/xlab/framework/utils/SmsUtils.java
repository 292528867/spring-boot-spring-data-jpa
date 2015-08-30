package com.wonders.xlab.framework.utils;

import com.bcloud.msg.http.HttpSender;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by wangqiang on 15/8/30.
 */
public final class SmsUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsUtils.class);

    private static final String BCLOUD_SMS_SRV_URI;
    private static final String BCLOUD_SMS_ACCOUNT;
    private static final String BCLOUD_SMS_PSWD;

    static {
        Properties props = new Properties();
        InputStream stream = SmsUtils.class.getResourceAsStream("/sms.properties");
        try {
            props.load(stream);
            BCLOUD_SMS_SRV_URI = props.getProperty("bcloud.sms.srv.url", "http://222.73.117.158/msg/");
            BCLOUD_SMS_ACCOUNT = props.getProperty("bcloud.sms.account", "wan-vip01");
            BCLOUD_SMS_PSWD = props.getProperty("bcloud.sms.pswd", "Tch123456");
            stream.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static boolean sendMsg(String[] mobiles, String content) {
        return sendMsg(StringUtils.join(mobiles, ','), content);
    }

    public static boolean sendMsg(String mobiles, String content) {
        return sendMsg(BCLOUD_SMS_ACCOUNT, BCLOUD_SMS_PSWD, mobiles, content);
    }

    public static boolean sendMsg(String account, String pswd, String mobiles, String content) {
        try {
            String resultString = HttpSender.batchSend(BCLOUD_SMS_SRV_URI, account, pswd, mobiles, content, true, null, null);
            LOGGER.info("短信发送后返回的响应内容为: {}", resultString);
            String status = StringUtils.substringBetween(resultString, ",", "\n");
            return StringUtils.equals(status, "0");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
