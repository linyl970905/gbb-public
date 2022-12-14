package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.vo.PunchArrayVO;
import com.tencent.wxcloudrun.vo.PunchCollectVO;
import com.tencent.wxcloudrun.vo.PuncnVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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

    MerEmpRelation getMerEmpRelation(@Param("merId") Integer merId, @Param("empId") Integer empId);

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

    void subBalance(@Param("merId") Integer merId, @Param("balance") BigDecimal balance);

    List<PuncnVO> getAllDays(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("yearMonth") String yearMonth);

    List<PuncnVO> getLateDays(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("yearMonth") String yearMonth);

    List<PuncnVO> getLeaveEarlyDays(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("yearMonth") String yearMonth);

    List<PuncnVO>  getInsureDays(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("yearMonth") String yearMonth);

    List<Date> getExistPunchDate(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("yearMonth") String yearMonth);

    List<PunchArrayVO> getPunchArray(@Param("merId") Integer merId, @Param("empId") Integer empId, @Param("createTime") Date createTime);

    Integer getDayRecord(@Param("empId") Integer empId, @Param("createTime") Date createTime);
}
