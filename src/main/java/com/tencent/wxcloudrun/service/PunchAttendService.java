package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.PunchAttendRule;

/**
 * @Author: zero
 * @Data: 2022/11/22 17:00
 * @Motto: Nothing is impossible
 */
public interface PunchAttendService {

    PunchAttendRule getRuleByMerId(Integer merId);

    void addRule(PunchAttendRule rule);
}
