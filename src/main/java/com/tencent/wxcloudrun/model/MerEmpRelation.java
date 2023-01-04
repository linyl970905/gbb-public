package com.tencent.wxcloudrun.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/11/15 14:42
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class MerEmpRelation implements Serializable {

    // 主键id
    private Integer id;

    // 商户id
    private Integer merId;

    // 雇员id
    private Integer empId;

    // 保险方案 1-默认50+5 2-80+8
    private Integer plan;

    // 工种
    private String jobCode;

    // 是否考勤 0-默认否 1-是
    private Integer isPunch;

    // 是否参保 0-默认否 1-是
    private Integer isInsure;

    // 状态 0-默认正常 1-删除
    private Integer status;

    // 创建时间
    private Date createTime;
}
