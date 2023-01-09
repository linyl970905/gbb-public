package com.tencent.wxcloudrun.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/11/30 15:04
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class PunchArrayVO implements Serializable {

    // 打卡状态 0-默认 1-按时 2-迟到 3-早退
    private Integer status;

    // 打卡时间
    private Date createTime;
}
