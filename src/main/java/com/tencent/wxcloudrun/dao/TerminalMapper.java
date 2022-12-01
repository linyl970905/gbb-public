package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.vo.EmployeePageVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/7 16:32
 * @Motto: Nothing is impossible
 */
public interface TerminalMapper {

    DeviceManage getDeviceBySnCode(String snCode);

    MerchantManage getMerchantByCloudId(String cloudId);

    List<MerchantManage> getMerchantByEmpId(Integer empId);

    Integer checkRelation(@Param("merId") Integer merId, @Param("empId") Integer empId);

    EmployeeManage getEmployeeByFaceId(String faceId);

    Integer addEmployeeManage(EmployeeManage employee);

    void addMerEmpRelation(@Param("merId") Integer merId, @Param("empId") Integer empId);

    Integer updateEmployeeManage(EmployeeManage manage);

    EmployeePageVO getEmployeePage(String openId);

    InsDayRecord getRecordByIdCard(String idCard);

    List<PunchAttendRecord> getRecordByTime(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("yearMonthDay") Date yearMonthDay);
}