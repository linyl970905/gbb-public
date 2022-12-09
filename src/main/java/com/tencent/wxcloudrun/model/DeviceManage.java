package com.tencent.wxcloudrun.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/11/14 16:49
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class DeviceManage implements Serializable {

    // 主键id
    private Integer id;

    // 公众号用户唯一标识
    private String cloudId;

    // 设备编码
    private String snCode;

    // 设备名称
    private String name;

    // 是否开启考勤 0-默认关闭 1-开启
    private Integer isPunch;

    // 是否开启保险 0-默认关闭 1-开启
    private Integer isInsure;

    // 状态 0-未激活 1-已激活
    private Integer status;

    // 创建时间
    private Date createTime;
}
