package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/11/21 17:28
 * @Motto: Nothing is impossible
 */
@Data
public class EmpRegisterDTO implements Serializable {

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


    // 设备编码
    private String snCode;
}
