package com.tencent.wxcloudrun.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2023/1/4 15:08
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class EmployeeManageInfo implements Serializable {

    // 主键id
    private Integer id;

    // 脸部照片地址
    private String faceUrl;

    // 姓名
    private String name;

    // 身份证号码
    private String idCard;

    // 手机号码
    private String phone;

    // 联系地址
    private String address;

    // 工种
    private String jobCode;

    // 方案 1-50+5 2-80+8
    private Integer plan;

    // 是否考勤 0-否 1-是
    private Integer isPunch;

    // 是否参保 0-否 1-是
    private Integer isInsure;

    // 状态 0-正常 1-删除
    private Integer status;

    // 创建时间
    private Date createTime;

}
