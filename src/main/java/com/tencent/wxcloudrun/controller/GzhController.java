package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.MerchantManage;
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
     * 激活设备-绑定商户
     * @param cloudId
     * @param snCode
     * @return
     */
    @GetMapping("/aliveDevice")
    public ApiResponse aliveDevice(@RequestParam String cloudId, @RequestParam String snCode) {
        return gzhService.aliveDevice(cloudId, snCode);
    }

    /**
     * 根据cloudId获取商户基本信息
     * @param cloudId
     * @return
     */
    @GetMapping("/getMerchantByCloudId")
    public ApiResponse getMerchantByCloudId(@RequestParam String cloudId) {
        return gzhService.getMerchantByCloudId(cloudId);
    }

    /**
     * 修改商户基本信息
     * @param merchant
     * @return
     */
    @PostMapping("/updateMerchantInfo")
    public ApiResponse updateMerchantInfo(@RequestBody MerchantManage merchant) {
        return gzhService.updateMerchantInfo(merchant);
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

    /**
     * 雇员-关闭考勤
     * @param id
     * @param isPunch
     * @return
     */
    @GetMapping("/closeEmpPunch")
    public ApiResponse closeEmpPunch(@RequestParam Integer id, @RequestParam Integer isPunch) {
        gzhService.closeEmpPunch(id, isPunch);
        return ApiResponse.ok();
    }

    /**
     * 雇员-关闭投保
     * @param id
     * @param isInsure
     * @return
     */
    @GetMapping("/closeEmpInsure")
    public ApiResponse closeEmpInsure(@RequestParam Integer id, @RequestParam Integer isInsure) {
        gzhService.closeEmpInsure(id, isInsure);
        return ApiResponse.ok();
    }

    /**
     * 雇员-删除
     * @param cloudId
     * @param empId
     * @return
     */
    @GetMapping("/delMerEmpRelation")
    public ApiResponse delMerEmpRelation(@RequestParam String cloudId, @RequestParam Integer empId) {
        gzhService.delMerEmpRelation(cloudId, empId);
        return ApiResponse.ok();
    }

    /**
     * 根据商户id查找设备列表
     * @param cloudId
     * @return
     */
    @GetMapping("/getDeviceList")
    public ApiResponse getDeviceList(@RequestParam String cloudId) {
        return ApiResponse.ok(gzhService.getDeviceList(cloudId));
    }

    /**
     * 设备-关闭考勤
     * @param id
     * @param isPunch
     * @return
     */
    @GetMapping("/closeDevicePunch")
    public ApiResponse closeDevicePunch(@RequestParam Integer id, @RequestParam Integer isPunch) {
        gzhService.closeDevicePunch(id, isPunch);
        return ApiResponse.ok();
    }

    /**
     * 设备-关闭投保
     * @param id
     * @param isInsure
     * @return
     */
    @GetMapping("/closeDeviceInsure")
    public ApiResponse closeDeviceInsure(@RequestParam Integer id, @RequestParam Integer isInsure) {
        gzhService.closeDeviceInsure(id, isInsure);
        return ApiResponse.ok();
    }
}
