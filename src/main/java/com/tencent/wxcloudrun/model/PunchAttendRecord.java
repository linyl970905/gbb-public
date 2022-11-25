package com.tencent.wxcloudrun.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/11/23 16:04
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class PunchAttendRecord implements Serializable {

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

    // 状态 0-默认 1-按时 2-迟到 3-早退
    private Integer status;

    // 创建时间
    private Date createTime;
}
