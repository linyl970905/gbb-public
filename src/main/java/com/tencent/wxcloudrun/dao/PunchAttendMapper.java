package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @Author: zero
 * @Data: 2022/11/22 16:51
 * @Motto: Nothing is impossible
 */
public interface PunchAttendMapper {

    PunchAttendRule getRuleByMerId(Integer merId);

    void addRule(PunchAttendRule rule);

    MerchantManage getMerchantBySnCode(String snCode);

    DeviceManage getDeviceBySnCode(String snCode);

    EmployeeManage getEmployeeById(Integer id);

    Integer getNumByTime(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("status") Integer status, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    void syncPunchStatus(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("status") Integer status, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Integer addPunchAttendRecord(PunchAttendRecord record);
}
