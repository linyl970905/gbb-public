package com.tencent.wxcloudrun.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2023/1/9 16:45
 * @Motto: Nothing is impossible
 */
@Data
public class PuncnVO implements Serializable {

    // 时间 年-月、年-月-日
    private Date time;

    // 对应的总数
    private Integer count;
}
