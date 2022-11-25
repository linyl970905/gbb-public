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
    private String oneStartTime;

    // 两次打卡正常时间1
    private String oneNormalTime;

    // 两次打卡结束时间1
    private String oneEndTime;

    // 两次打卡开始时间2
    private String twoStartTime;

    // 两次打卡正常时间2
    private String twoNormalTime;

    // 两次打卡结束时间2
    private String twoEndTime;

    // 四次打卡开始时间1
    private String threeStartTime;

    // 四次打卡正常时间1
    private String threeNormalTime;

    // 四次打卡结束时间1
    private String threeEndTime;

    // 四次打卡开始时间2
    private String fourStartTime;

    // 四次打卡正常时间2
    private String fourNormalTime;

    // 四次打卡结束时间2
    private String fourEndTime;

    // 六次打卡开始时间1
    private String fiveStartTime;

    // 六次打卡正常时间1
    private String fiveNormalTime;

    // 六次打卡结束时间1
    private String fiveEndTime;

    // 六次打卡开始时间2
    private String sixStartTime;

    // 六次打卡正常时间2
    private String sixNormalTime;

    // 六次打卡结束时间2
    private String sixEndTime;

    // 创建时间
    private Date createTime;
}
