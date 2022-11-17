package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.DeviceManage;
import com.tencent.wxcloudrun.model.EmployeeManage;
import com.tencent.wxcloudrun.model.MerchantManage;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: zero
 * @Data: 2022/11/7 16:32
 * @Motto: Nothing is impossible
 */
public interface TerminalMapper {

    DeviceManage getDeviceBySnCode(String snCode);

    MerchantManage getMerchantById(Integer id);

    EmployeeManage getEmployeeByFaceId(String faceId);

    Integer addEmployeeManage(EmployeeManage employee);

    void addDeviceEmployee(@Param("deviceId") Integer deviceId, @Param("employeeId") Integer employeeId);

    Integer updateEmployeeManage(EmployeeManage manage);
}
