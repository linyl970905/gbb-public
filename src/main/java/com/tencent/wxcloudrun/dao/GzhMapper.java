package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.*;
import org.apache.ibatis.annotations.Param;

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

    DeviceManage addDeviceManage(DeviceManage device);

    Integer getDeviceNumByCloudId(String cloudId);

    Integer getEmployeeNumByMerId(Integer merId);

    void updateMerchantInfo(MerchantManage merchant);

    List<EmployeeManage> getEmployeeList(String cloudId);

    void closeEmpPunch(@Param("id") Integer id, @Param("isPunch") Integer isPunch);

    void closeEmpInsure(@Param("id") Integer id, @Param("isInsure") Integer isInsure);

    void delMerEmpRelation(@Param("merId") Integer merId, @Param("empId") Integer empId);

    List<DeviceManage> getDeviceList(String cloudId);

    void closeDevicePunch(@Param("id") Integer id, @Param("isPunch") Integer isPunch);

    void closeDeviceInsure(@Param("id") Integer id, @Param("isInsure") Integer isInsure);
}
