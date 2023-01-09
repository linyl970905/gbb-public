package com.tencent.wxcloudrun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/11/29 10:21
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class InsDayRecord implements Serializable {

    // 主键id
    private Integer id;

    // 商户id
    private Integer merId;

    // 雇员id
    private Integer empId;

    // 终端设备编码
    private String snCode;

    // 打卡人姓名
    private String name;

    // 打卡人身份证号码
    private String idCard;

    // 手机号码
    private String phone;

    // 联系地址
    private String address;

    // 工种
    private String jobCode;

    // 方案 1-50+5 2-80+8
    private Integer plan;

    // 起保时间
    private Date startTime;

    // 止保时间
    private Date endTime;

    // 三方接口返回体
    private String responseMsg;

    // 创建时间
    private Date createTime;
}
