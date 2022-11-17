package com.tencent.wxcloudrun.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/11/9 16:32
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class MerchantManage implements Serializable {

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

    // 创建时间
    private Date createTime;
}
