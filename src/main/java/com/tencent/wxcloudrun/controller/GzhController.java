package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.service.GzhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/11/8 15:55
 * @Motto: Nothing is impossible
 */
@RestController
@RequestMapping("/gzh")
public class GzhController implements Serializable {

    @Autowired
    private GzhService gzhService;

    /**
     * 公众号授权登录
     * @param code
     * @return
     * @throws IOException
     */
    @GetMapping("/gzhAuthorizedLogin")
    public ApiResponse gzhAuthorizedLogin(@RequestParam String code) throws IOException {
        return ApiResponse.ok(gzhService.gzhAuthorizedLogin(code));
    }

    /**
     * 获取省-市-区信息
     * @return
     */
    @GetMapping("/getAllArea")
    public ApiResponse getAllArea() {
        return ApiResponse.ok(gzhService.getAllArea());
    }
}
