package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.service.PunchAttendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/12/28 16:24
 * @Motto: Nothing is impossible
 */
@RestController
@RequestMapping("/test")
public class TestController implements Serializable {

    @Autowired
    private PunchAttendService punchAttendService;

    @GetMapping("/operaPunch")
    public ApiResponse operaPunch(@RequestParam String snCode, @RequestParam String userId) {
        return punchAttendService.punchAttend(snCode, userId);
    }

    @GetMapping("/insureApply")
    public ApiResponse insureApply(@RequestParam String snCode, @RequestParam String userId) throws Exception {
        return punchAttendService.insureApply(snCode, userId);
    }
}
