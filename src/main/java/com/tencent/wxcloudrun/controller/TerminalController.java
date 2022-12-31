package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.config.ResultMsg;
import com.tencent.wxcloudrun.dto.EmpRegisterDTO;
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
     * @param requestJson
     * @return
     */
    @PostMapping("/getDeviceBySnCode")
    public ResultMsg getDeviceBySnCode(@RequestBody String requestJson) {
        return terminalService.getDeviceBySnCode(requestJson);
    }

    /**
     * 终端扫码后：打卡/登记
     * @param requestJson
     * @return
     */
    @PostMapping("/operaPunch")
    public ResultMsg operaPunch(@RequestBody String requestJson) throws Exception {
        return terminalService.operaPunch(requestJson);
    }

    /**
     * 小程序-员工注册接口
     * @param requestJson
     * @return
     */
    @PostMapping("/employeeRegister")
    public ResultMsg employeeRegister(@RequestBody String requestJson) {
        return terminalService.employeeRegister(requestJson);
    }

    /**
     * 小程序-设备心跳接口
     * @param requestJson
     * @return
     */
    @PostMapping("/deviceHeartBeat")
    public ResultMsg deviceHeartBeat(@RequestBody String requestJson) {
        return terminalService.deviceHeartBeat(requestJson);
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
     * 登记时，更换清晰的人脸照
     * @param faceId
     * @param faceUrl
     * @return
     */
    @GetMapping("/updateEmployeeFace")
    public ApiResponse updateEmployeeFace(@RequestParam String faceId, @RequestParam String faceUrl) {
        return terminalService.updateEmployeeFace(faceId, faceUrl);
    }

    /**
     * 小程序-员工登记
     * @param registerDTO
     * @return
     */
    @PostMapping("/addEmployeeManage")
    public ApiResponse addEmployeeManage(@RequestBody EmpRegisterDTO registerDTO) throws Exception {
        return terminalService.addEmployeeManage(registerDTO);
    }

    /**
     * 小程序-员工首页(基本信息、保险信息)
     * @param phone
     * @return
     */
    @GetMapping("/getEmployeePage")
    public ApiResponse getEmployeePage(@RequestParam String phone) {
        return ApiResponse.ok(terminalService.getEmployeePage(phone));
    }

    /**
     * 小程序-获取雇员下的商家列表
     * @param empId
     * @return
     */
    @GetMapping("/getMerchantList")
    public ApiResponse getMerchantList(@RequestParam Integer empId) {
        return ApiResponse.ok(terminalService.getMerchantList(empId));
    }

    /**
     * 小程序-首页获取具体某一天的打卡详情
     * @param merId
     * @param empId
     * @param yearMonthDay
     * @return
     */
    @GetMapping("/getRecordByTime")
    public ApiResponse getRecordByTime(@RequestParam Integer merId, @RequestParam Integer empId, @RequestParam String yearMonthDay) {
        return ApiResponse.ok(terminalService.getRecordByTime(merId, empId, yearMonthDay));
    }

    /**
     * 小程序-雇员的打卡详情
     * @param merId
     * @param empId
     * @param yearMonth
     * @return
     */
    @GetMapping("/getPunchDetail")
    public ApiResponse getPunchDetail(@RequestParam Integer merId, @RequestParam Integer empId, @RequestParam String yearMonth) {
        return ApiResponse.ok(terminalService.getPunchDetail(merId, empId, yearMonth));
    }
}
