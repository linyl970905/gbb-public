package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.PunchAttendRule;
import com.tencent.wxcloudrun.service.PunchAttendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/11/22 16:47
 * @Motto: Nothing is impossible
 */
@RestController
@RequestMapping("/punch/attend")
public class PunchAttendController implements Serializable {

    @Autowired
    private PunchAttendService punchAttendService;

    /**
     * 根据merId获取打卡考勤规则
     * @param merId
     * @return
     */
    @GetMapping("/getRuleByMerId")
    public ApiResponse getRuleByMerId(@RequestParam Integer merId) {
        return ApiResponse.ok(punchAttendService.getRuleByMerId(merId));
    }

    /**
     * 新增打卡考勤规则
     * @param rule
     * @return
     */
    @PostMapping("/addRule")
    public ApiResponse addRule(@RequestBody PunchAttendRule rule) {
        punchAttendService.addRule(rule);
        return ApiResponse.ok();
    }
}
