package com.tencent.wxcloudrun.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/30 10:18
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class PunchDetailVO implements Serializable {

    // 时间 年-月-日
    private Date yearMonthDay;

    // 状态 0-无打卡记录 1-打卡 2-异常 3-投保
    private Integer status;

    // 当前具体打卡详情
    List<PunchArrayVO> arrayList;
}
