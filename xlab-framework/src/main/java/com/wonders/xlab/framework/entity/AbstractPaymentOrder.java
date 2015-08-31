package com.wonders.xlab.framework.entity;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.persistence.*;
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
    @Column(nullable = false, unique = true)
    private String orderNo;

    /**
     * 订单标题
     */
    @Column(nullable = false)
    private String subject;

    /**
     * 订单内容
     */
    @Column(nullable = false)
    private String body;

    /**
     * 下单设备的ip地址
     */
    @Column(nullable = false)
    private String clientIp;

    /**
     * 下单用户
     */
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
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * 订单支付渠道
     */
    @Column(nullable = false)
    private String paymentChannel;

    public AbstractPaymentOrder() {
        this.orderNo = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")
                + RandomStringUtils.random(2);
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

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

}
