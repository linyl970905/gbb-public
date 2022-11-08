package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.EmployeeManage;
import com.tencent.wxcloudrun.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/11/7 16:26
 * @Motto: Nothing is impossible
 */
@RestController
@RequestMapping("/terminal")
public class TerminalController implements Serializable {

    @Autowired
    private TerminalService terminalService;

    /**
     * 终端扫码后保存人脸信息
     * @param faceId
     * @param faceUrl
     * @return
     */
    @GetMapping("/addEmployeeFace")
    public ApiResponse addEmployeeFace(@RequestParam Integer faceId, @RequestParam String faceUrl) {
        terminalService.addEmployeeFace(faceId, faceUrl);
        return ApiResponse.ok();
    }

    /**
     * 根据faceId获取雇员信息(脸部照片地址)
     * @param faceId
     * @return
     */
    @GetMapping("/getEmployeeByFaceId")
    public ApiResponse getEmployeeByFaceId(@RequestParam Integer faceId) {
        return ApiResponse.ok(terminalService.getEmployeeByFaceId(faceId));
    }

    /**
     * 小程序-员工登记
     * @param manage
     * @return
     */
    @PostMapping("/addEmployeeManage")
    public ApiResponse addEmployeeManage(@RequestBody EmployeeManage manage) {
        terminalService.addEmployeeManage(manage);
        return ApiResponse.ok();
    }
}
