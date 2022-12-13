package com.tencent.wxcloudrun.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/12/13 15:31
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class RechargeRecord implements Serializable {

    // 主键id
    private Integer id;

    // 商户标识
    private String cloudId;

    // 充值金额
    private BigDecimal rechargeAmount;

    // 充值前余额
    private BigDecimal beforeBalance;

    // 充值后余额
    private BigDecimal afterBalance;

    // 创建时间
    private Date createTime;
}
