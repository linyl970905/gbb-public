package com.tencent.wxcloudrun.dto;

import com.tencent.wxcloudrun.model.EmployeeManage;
import lombok.Data;

/**
 * @Author: zero
 * @Data: 2022/11/21 17:28
 * @Motto: Nothing is impossible
 */
@Data
public class EmpRegisterDTO extends EmployeeManage {

    // 主键id
    private Integer id;

    // 小程序用户唯一标识
    private String openId;

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


    // 设备编码
    private String snCode;
}
