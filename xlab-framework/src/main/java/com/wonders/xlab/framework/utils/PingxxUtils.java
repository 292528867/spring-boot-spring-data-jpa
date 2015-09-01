package com.wonders.xlab.framework.utils;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.wonders.xlab.framework.entity.AbstractPaymentOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by wangqiang on 15/9/1.
 */
public final class PingxxUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingxxUtils.class);

    private static final String PINGXX_LIVE_KEY;
    private static final String PINGXX_TEST_KEY;
    private static final String PINGXX_APP_ID;

    static {
        Properties props = new Properties();
        InputStream stream = PingxxUtils.class.getResourceAsStream("/pingxx.properties");
        try {
            props.load(stream);
            PINGXX_LIVE_KEY = props.getProperty("pingxx.live.key");
            PINGXX_TEST_KEY = props.getProperty("pingxx.test.key");
            PINGXX_APP_ID = props.getProperty("pingxx.app.id");

            // 设置Ping++ API-Key 分正试环境和测试环境
            Pingpp.apiKey = PINGXX_LIVE_KEY;

            stream.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private PingxxUtils() {

    }

    public static Charge payOrder(AbstractPaymentOrder order) {

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("order_no", order.getOrderNo());
        chargeParams.put("amount", order.getAmount());
        Map<String, String> app = new HashMap<>();
        app.put("id", PINGXX_APP_ID);
        chargeParams.put("app", app);
        chargeParams.put("channel", order.getPaymentChannel());
        chargeParams.put("currency", "cny"); // 人民币
        chargeParams.put("client_ip", order.getClientIp());
        chargeParams.put("subject", order.getSubject());
        chargeParams.put("body", order.getBody());

        Charge charge;
        try {
            charge = Charge.create(chargeParams);
        } catch (AuthenticationException | InvalidRequestException
                | APIConnectionException | APIException | ChannelException e) {

            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return charge;
    }

}
