package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.*;

import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/8 16:20
 * @Motto: Nothing is impossible
 */
public interface GzhMapper {

    MerchantManage getMerchantByCloudId(String cloudId);

    void addMerchantManage(String cloudId);

    List<Province> getProvince();

    List<City> getCityByPid(Integer pid);

    List<Area> getAreaByPid(Integer pid);

    Integer createOrder(Order order);

    List<EmployeeManage> getEmployeeList(String cloudId);
}
