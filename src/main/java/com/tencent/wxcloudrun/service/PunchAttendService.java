package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.PunchAttendRule;
import com.tencent.wxcloudrun.vo.PunchCollectVO;
import com.tencent.wxcloudrun.vo.PunchDetailVO;

import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/22 17:00
 * @Motto: Nothing is impossible
 */
public interface PunchAttendService {

    PunchAttendRule getRuleByMerId(Integer merId);

    void addRule(PunchAttendRule rule);

    ApiResponse punchAttend(String snCode, String userId);

    ApiResponse insureApply(String snCode, String userId) throws Exception;

    List<PunchCollectVO> getPunchCollect(String cloudId, String yearMonth);

    List<PunchDetailVO> getPunchDetail(String cloudId, Integer empId, String yearMonth);
}
