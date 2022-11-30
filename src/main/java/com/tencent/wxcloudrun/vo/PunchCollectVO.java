package com.tencent.wxcloudrun.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/11/28 14:37
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class PunchCollectVO implements Serializable {

    // 雇员id
    private Integer id;

    // 姓名
    private String name;

    // 考勤天数
    private Integer allDays;

    // 迟到天数
    private Integer lateDays;

    // 早退天数
    private Integer leaveEarlyDays;

    // 投保天数
    private Integer insureDays;
}
