package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.DeviceManage;
import com.tencent.wxcloudrun.model.EmployeeManage;
import com.tencent.wxcloudrun.model.MerchantManage;
import com.tencent.wxcloudrun.model.Order;
import com.tencent.wxcloudrun.vo.ProvinceCityList;

import javax.servlet.http.HttpServletRequest;
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

    ApiResponse aliveDevice(String cloudId, String snCode, String name);

    ApiResponse getMerchantByCloudId(String cloudId);

    ApiResponse updateMerchantInfo(MerchantManage merchant);

    List<EmployeeManage> getEmployeeList(String cloudId);

    void closeEmpPunch(Integer id, Integer isPunch);

    void closeEmpInsure(Integer id, Integer isInsure);

    void delMerEmpRelation(String cloudId, Integer empId);

    List<DeviceManage> getDeviceList(String cloudId);

    void closeDevicePunch(Integer id, Integer isPunch);

    void closeDeviceInsure(Integer id, Integer isInsure);

    void delDevice(Integer id);
}
