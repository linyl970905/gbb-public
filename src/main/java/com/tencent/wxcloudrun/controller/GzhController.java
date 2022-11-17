package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.Order;
import com.tencent.wxcloudrun.service.GzhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 公众号注册/登录
     * @param cloudId
     * @return
     */
    @GetMapping("/registerLogin")
    public ApiResponse registerLogin(@RequestParam String cloudId) {
        return gzhService.registerLogin(cloudId);
    }

    /**
     * 获取省-市-区信息
     * @return
     */
    @GetMapping("/getAllArea")
    public ApiResponse getAllArea() {
        return ApiResponse.ok(gzhService.getAllArea());
    }

    /**
     * 公众号：购买考勤机
     * @param order
     * @return
     */
    @PostMapping("/createOrder")
    public ApiResponse createOrder(@RequestBody Order order) {
        return gzhService.createOrder(order);
    }

    /**
     * 根据商户id查找雇员列表
     * @param cloudId
     * @return
     */
    @GetMapping("/getEmployeeList")
    public ApiResponse getEmployeeList(@RequestParam String cloudId) {
        return ApiResponse.ok(gzhService.getEmployeeList(cloudId));
    }
}
