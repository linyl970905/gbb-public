package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/10/31 15:06
 * @Motto: Nothing is impossible
 */
@RestController
@RequestMapping("/public")
public class PublicController implements Serializable {

    @Autowired
    private PublicService publicService;

    @GetMapping("/judgeInterface")
    public ApiResponse judgeInterface(@RequestParam Integer id) {
        return ApiResponse.ok(publicService.getEmployeeById(id));
    }
}
