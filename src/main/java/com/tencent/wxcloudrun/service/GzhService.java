package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.EmployeeManage;
import com.tencent.wxcloudrun.model.Order;
import com.tencent.wxcloudrun.vo.ProvinceCityList;

import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/8 15:59
 * @Motto: Nothing is impossible
 */
public interface GzhService {

    ApiResponse registerLogin(String cloudId);

    List<ProvinceCityList> getAllArea();

    ApiResponse createOrder(Order order);

    List<EmployeeManage> getEmployeeList(String cloudId);
}
