package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.config.ResultMsg;
import com.tencent.wxcloudrun.dto.EmpRegisterDTO;
import com.tencent.wxcloudrun.model.EmployeeManage;
import com.tencent.wxcloudrun.model.MerchantManage;
import com.tencent.wxcloudrun.model.PunchAttendRecord;
import com.tencent.wxcloudrun.vo.EmployeePageVO;
import com.tencent.wxcloudrun.vo.PunchDetailVO;

import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/7 16:31
 * @Motto: Nothing is impossible
 */
public interface TerminalService {

    ResultMsg getDeviceBySnCode(String requestJson);

    ResultMsg operaPunch(String requestJson) throws Exception;

    ResultMsg employeeRegister(String requestJson);

    ResultMsg deviceHeartBeat(String requestJson);

    EmployeeManage getEmployeeByFaceId(String faceId);

    ApiResponse updateEmployeeFace(String faceId, String faceUrl);

    ApiResponse addEmployeeManage(EmpRegisterDTO registerDTO) throws Exception;

    ApiResponse getAppletsPhone(String code);

    ApiResponse checkEmployeeByPhone(String phone);

    ApiResponse checkEmployeeInfo(String name, String idCard);

    ApiResponse bindAppletsPhone(Integer id, String phone);

    EmployeePageVO getEmployeePage(String phone);

    List<MerchantManage> getMerchantList(Integer empId);

    List<PunchAttendRecord> getRecordByTime(Integer merId, Integer empId, String yearMonthDay);

    List<PunchDetailVO> getPunchDetail(Integer merId, Integer empId, String yearMonth);
}
