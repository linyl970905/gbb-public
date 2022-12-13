package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/8 16:20
 * @Motto: Nothing is impossible
 */
public interface GzhMapper {

    MerchantManage getMerchantByCloudId(String cloudId);

    Integer addMerchantManage(MerchantManage merchant);

    List<Province> getProvince();

    List<City> getCityByPid(Integer pid);

    List<Area> getAreaByPid(Integer pid);

    Integer createOrder(Order order);

    DeviceManage getDeviceBySnCode(String snCode);

    Integer addDeviceManage(DeviceManage device);

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

    void delDevice(Integer id);

    Integer updateBalance(@Param("id") Integer id, @Param("rechargeAmount") BigDecimal rechargeAmount);

    void addRechargeRecord(RechargeRecord record);

    List<RechargeRecord> getRechargeRecordList(String cloudId);
}
