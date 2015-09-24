package com.wonders.xlab.framework.utils;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.wonders.xlab.framework.entity.AbstractPaymentOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
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

    static {
        Properties props = new Properties();
        InputStream stream = PingxxUtils.class.getResourceAsStream("/pingxx.properties");
        if (stream == null) {
            throw new RuntimeException("找不到相应的配置文件！[classpath:/pingxx.properties]");
        }
        try {
            props.load(stream);
            PINGXX_LIVE_KEY = props.getProperty("pingxx.live.key");
            PINGXX_TEST_KEY = props.getProperty("pingxx.test.key");

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

    /**
     *
     * @param pingxxAppId ping++ 应用ID
     * @param order 抽象的支付定单
     * @return ping++ Charge 对象
     */
    public static Charge createCharge(String pingxxAppId, AbstractPaymentOrder order) {

        if (order == null) {
            throw new IllegalArgumentException("支付订单参数不能为空!");
        }

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("order_no", order.getOrderNo());
        chargeParams.put("amount", (int) (order.getAmount().doubleValue() * 100)); // 人民币以分为单位
        chargeParams.put("app", Collections.singletonMap("id", pingxxAppId));
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
