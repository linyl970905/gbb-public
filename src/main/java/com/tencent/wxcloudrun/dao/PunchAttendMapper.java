package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.vo.PunchArrayVO;
import com.tencent.wxcloudrun.vo.PunchCollectVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

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

    InsDayRecord getInsDayRecordByIdCard(String idCard);

    Integer getInsDayToday(String idCard);

    Integer addInsDayRecord(InsDayRecord record);

    MerchantManage getMerchantByCloudId(String cloudId);

    List<PunchCollectVO> getPunchEmpList(Integer merId);

    void addPunchAttendRule(PunchAttendRule rule);

    void updatePunchAttendRule(PunchAttendRule rule);

    Integer getAllDays(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("yearMonth") String yearMonth);

    Integer getLateDays(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("yearMonth") String yearMonth);

    Integer getLeaveEarlyDays(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("yearMonth") String yearMonth);

    Integer getInsureDays(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("yearMonth") String yearMonth);

    List<Date> getExistPunchDate(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("yearMonth") String yearMonth);

    List<PunchArrayVO> getPunchArray(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("createTime") Date createTime);
}
