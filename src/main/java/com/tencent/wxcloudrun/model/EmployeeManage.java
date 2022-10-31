package com.tencent.wxcloudrun.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/10/31 15:09
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class EmployeeManage implements Serializable {

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

    // 是否管理员 0-默认否 1-是
    private Integer isManager;

    // 是否考勤 0-默认否 1-是
    private Integer isWork;

    // 是否参保 0-默认否 1-是
    private Integer isInsured;

    // 状态 0-默认正常 1-删除
    private Integer status;

    // 创建时间
    private Date createTime;
}
