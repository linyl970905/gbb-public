package com.tencent.wxcloudrun.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
@AllArgsConstructor
@NoArgsConstructor
public class PunchDetailVO implements Serializable {

    // 时间 年-月-日
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date yearMonthDay;

    // 打卡状态 0-无打卡记录 1-打卡 2-异常 3-投保
    private Integer status;

    // 保险状态 0-未投保 1-已投保
    private Integer isInsured;

    // 当前具体打卡详情
    List<PunchArrayVO> arrayList;
}
