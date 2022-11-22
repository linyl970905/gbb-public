package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.PunchAttendMapper;
import com.tencent.wxcloudrun.model.PunchAttendRule;
import com.tencent.wxcloudrun.service.PunchAttendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zero
 * @Data: 2022/11/22 17:01
 * @Motto: Nothing is impossible
 */
@Service
public class PunchAttendServiceImpl implements PunchAttendService {

    @Autowired
    private PunchAttendMapper punchAttendMapper;

    @Override
    public PunchAttendRule getRuleByMerId(Integer merId) {
        return punchAttendMapper.getRuleByMerId(merId);
    }

    @Override
    public void addRule(PunchAttendRule rule) {
        punchAttendMapper.addRule(rule);
    }
}
