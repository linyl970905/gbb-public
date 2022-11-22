package com.tencent.wxcloudrun.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/11/22 16:35
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class PunchAttendRule implements Serializable {

    // 主键id
    private Integer id;

    // 商户id
    private Integer merId;

    // 打卡规则类型 1-两次打卡 2-四次打卡 3-六次打卡
    private Integer punchType;

    // 两次打卡开始时间1
    private Date oneStartTime;

    // 两次打卡结束时间1
    private Date oneEndTime;

    // 两次打卡开始时间2
    private Date twoStartTime;

    // 两次打卡结束时间2
    private Date twoEndTime;

    // 四次打卡开始时间1
    private Date threeStartTime;

    // 四次打卡结束时间1
    private Date threeEndTime;

    // 四次打卡开始时间2
    private Date fourStartTime;

    // 四次打卡结束时间2
    private Date fourEndTime;

    // 六次打卡开始时间1
    private Date fiveStartTime;

    // 六次打卡结束时间1
    private Date fiveEndTime;

    // 六次打卡开始时间2
    private Date sixStartTime;

    // 六次打卡结束时间2
    private Date sixEndTime;

    // 创建时间
    private Date createTime;
}
