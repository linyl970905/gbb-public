package com.tencent.wxcloudrun.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/11/21 11:06
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class MerchantDetailVO implements Serializable {

    // 主键id
    private Integer id;

    // 公众号用户唯一标识
    private String cloudId;

    // 商户名称
    private String name;

    // 联系人
    private String linkPerson;

    // 联系电话
    private String linkPhone;

    // 地址
    private String address;

    // 营业执照
    private String businessLicense;

    // 营业场所照片
    private String placeImage;

    // 人脸识别相似阀值
    private Integer faceScore;

    // 账户余额
    private BigDecimal balance;

    // 创建时间
    private Date createTime;



    // 设备数
    private Integer deviceNum;

    // 员工数
    private Integer employeeNum;
}
