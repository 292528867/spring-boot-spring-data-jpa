package com.wonders.xlab.framework.entity;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * 支付订单
 * <p>
 * Created by wangqiang on 15/8/31.
 */
@MappedSuperclass
public abstract class AbstractPaymentOrder<U, P> extends AbstractBaseEntity<Long> {

    /**
     * 订单号
     */
    @NotNull
    @Size(min = 16, max = 16)
    @Column(nullable = false, unique = true)
    private String orderNo;

    /**
     * 订单标题
     */
    @NotNull
    @Column(nullable = false)
    private String subject;

    /**
     * 订单内容
     */
    @NotNull
    @Column(nullable = false)
    private String body;

    /**
     * 下单设备的ip地址
     */
    @NotNull
    @Column(nullable = false)
    private String clientIp;

    /**
     * 下单用户
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    private U user;

    /**
     * 订单关联的产品
     */
    @OneToMany
    @JoinTable(name = "order_production",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "prod_id")
    )
    private Set<P> productions;

    /**
     * 订单金额
     */
    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * 订单支付渠道
     */
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentChannel paymentChannel = PaymentChannel.wx;

    /**
     * 订单支付状态
     */
    @NotNull
    @Column(nullable = false)
    @Enumerated
    private PaymentStatus paymentStatus = PaymentStatus.NON_PAYMENT;

    public AbstractPaymentOrder() {
        this.orderNo = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")
                + RandomStringUtils.randomNumeric(2);
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public U getUser() {
        return user;
    }

    public void setUser(U user) {
        this.user = user;
    }

    public Set<P> getProductions() {
        return productions;
    }

    public void setProductions(Set<P> productions) {
        this.productions = productions;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentChannel getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(PaymentChannel paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public enum PaymentChannel {
        alipay, alipay_wap, alipay_qr, wx, wx_pub, wx_pub_qr
    }

    public enum PaymentStatus {

        /**
         * 待支付
         */
        NON_PAYMENT,

        /**
         * 取消支付
         */
        PAYMENT_CANCELLED,

        /**
         * 支付超时
         */
        PAYMENT_TIMEOUT,

        /**
         * 支付成功
         */
        PAYMENT_SUCCEEDED,


        /**
         * 支付失败
         */
        PAYMENT_FAILED

    }


}
