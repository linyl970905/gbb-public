package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.MerchantManage;
import com.tencent.wxcloudrun.model.Order;
import com.tencent.wxcloudrun.service.GzhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;

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
     * 获取请求头信息
     * @param request
     * @return
     */
    @GetMapping("/getRequestHeader")
    public ApiResponse getRequestHeader(HttpServletRequest request) {
        return gzhService.getRequestHeader(request);
    }

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
    public ApiResponse aliveDevice(@RequestParam String cloudId, @RequestParam String snCode,
                                   @RequestParam String name, @RequestParam String promotionCode) {
        return gzhService.aliveDevice(cloudId, snCode, name, promotionCode);
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
     * @param merId
     * @param empId
     * @param isPunch
     * @return
     */
    @GetMapping("/closeEmpPunch")
    public ApiResponse closeEmpPunch(@RequestParam Integer merId, @RequestParam Integer empId, @RequestParam Integer isPunch) {
        gzhService.closeEmpPunch(merId, empId, isPunch);
        return ApiResponse.ok();
    }

    /**
     * 雇员-关闭投保
     * @param merId
     * @param empId
     * @param isInsure
     * @return
     */
    @GetMapping("/closeEmpInsure")
    public ApiResponse closeEmpInsure(@RequestParam Integer merId, @RequestParam Integer empId, @RequestParam Integer isInsure) {
        gzhService.closeEmpInsure(merId, empId, isInsure);
        return ApiResponse.ok();
    }

    /**
     * 雇员-编辑投保信息(工种、方案)
     * @param merId
     * @param empId
     * @param jobCode
     * @param plan
     * @return
     */
    @GetMapping("/updateInsureInfo")
    public ApiResponse updateInsureInfo(@RequestParam Integer merId, @RequestParam Integer empId,
                                        @RequestParam String jobCode, @RequestParam Integer plan) {
        gzhService.updateInsureInfo(merId, empId, jobCode, plan);
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

    /**
     * 设备-删除
     * @param id
     * @return
     */
    @GetMapping("/delDevice")
    public ApiResponse delDevice(@RequestParam Integer id) {
        gzhService.delDevice(id);
        return ApiResponse.ok();
    }

    /**
     * 充值-账户余额
     * @param cloudId
     * @param rechargeAmount
     * @return
     */
    @GetMapping("/addRechargeBalance")
    public ApiResponse addRechargeBalance(@RequestParam String cloudId, @RequestParam BigDecimal rechargeAmount) {
        gzhService.addRechargeBalance(cloudId, rechargeAmount);
        return ApiResponse.ok();
    }

    /**
     * 充值-获取充值列表
     * @param cloudId
     * @return
     */
    @GetMapping("/getRechargeRecordList")
    public ApiResponse getRechargeRecordList(@RequestParam String cloudId) {
        return ApiResponse.ok(gzhService.getRechargeRecordList(cloudId));
    }
}
