package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.PunchAttendRule;

/**
 * @Author: zero
 * @Data: 2022/11/22 16:51
 * @Motto: Nothing is impossible
 */
public interface PunchAttendMapper {

    PunchAttendRule getRuleByMerId(Integer merId);

    void addRule(PunchAttendRule rule);
}
