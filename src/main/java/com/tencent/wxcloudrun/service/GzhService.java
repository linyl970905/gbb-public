package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.vo.EmployeeManageInfo;
import com.tencent.wxcloudrun.vo.ProvinceCityList;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/8 15:59
 * @Motto: Nothing is impossible
 */
public interface GzhService {

    ApiResponse getRequestHeader(HttpServletRequest request);

    ApiResponse registerLogin(String cloudId);

    List<ProvinceCityList> getAllArea();

    ApiResponse createOrder(Order order);

    ApiResponse aliveDevice(String cloudId, String snCode, String name, String promotionCode);

    ApiResponse getMerchantByCloudId(String cloudId);

    ApiResponse updateMerchantInfo(MerchantManage merchant);

    List<EmployeeManageInfo> getEmployeeList(String cloudId);

    void closeEmpPunch(Integer merId, Integer empId, Integer isPunch);

    void closeEmpInsure(Integer merId, Integer empId, Integer isInsure);

    void updateInsureInfo(Integer merId, Integer empId, String jobCode, Integer plan);

    void delMerEmpRelation(String cloudId, Integer empId);

    List<DeviceManage> getDeviceList(String cloudId);

    void closeDevicePunch(Integer id, Integer isPunch);

    void closeDeviceInsure(Integer id, Integer isInsure);

    void delDevice(Integer id);

    void addRechargeBalance(String cloudId, BigDecimal rechargeAmount);

    List<RechargeRecord> getRechargeRecordList(String cloudId);
}
