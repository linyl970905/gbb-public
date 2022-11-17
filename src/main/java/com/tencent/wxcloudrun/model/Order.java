package com.tencent.wxcloudrun.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/11/9 16:41
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class Order implements Serializable {

    // 主键id
    private Integer id;

    // 公众号用户唯一标识
    private String cloudId;

    // 商品名称
    private String name;

    // 商品规格
    private String spec;

    // 商品单价
    private BigDecimal price;

    // 购买数量
    private Integer buyNum;

    // 订单号
    private String orderNo;

    // 订单总金额
    private BigDecimal orderPrice;

    // 运营中心推广码
    private String promotionCode;

    // 创建时间
    private Date createTime;
}
