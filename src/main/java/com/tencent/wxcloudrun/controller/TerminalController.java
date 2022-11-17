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
     * 查询设备是否激活
     * @param snCode
     * @return
     */
    @GetMapping("/getDeviceBySnCode")
    public ApiResponse getDeviceBySnCode(@RequestParam String snCode) {
        return terminalService.getDeviceBySnCode(snCode);
    }

    /**
     * 终端扫码后：打卡/登记
     * @param snCode
     * @param faceUrl
     * @return
     */
    @GetMapping("/operaPunch")
    public ApiResponse operaPunch(@RequestParam String snCode, @RequestParam String faceUrl) throws Exception {
        return terminalService.operaPunch(snCode, faceUrl);
    }

    /**
     * 根据faceId获取雇员信息(脸部照片地址)
     * @param faceId
     * @return
     */
    @GetMapping("/getEmployeeByFaceId")
    public ApiResponse getEmployeeByFaceId(@RequestParam String faceId) {
        return ApiResponse.ok(terminalService.getEmployeeByFaceId(faceId));
    }

    /**
     * 小程序-员工登记
     * @param manage
     * @return
     */
    @PostMapping("/addEmployeeManage")
    public ApiResponse addEmployeeManage(@RequestBody EmployeeManage manage) throws Exception {
        return terminalService.addEmployeeManage(manage);
    }
}
