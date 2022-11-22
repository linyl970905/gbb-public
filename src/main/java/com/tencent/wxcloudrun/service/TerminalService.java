package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.EmpRegisterDTO;
import com.tencent.wxcloudrun.model.EmployeeManage;

/**
 * @Author: zero
 * @Data: 2022/11/7 16:31
 * @Motto: Nothing is impossible
 */
public interface TerminalService {

    ApiResponse getDeviceBySnCode(String snCode);

    ApiResponse operaPunch(String snCode, String faceUrl) throws Exception;

    EmployeeManage getEmployeeByFaceId(String faceId);

    ApiResponse updateEmployeeFace(String faceId, String faceUrl);

    ApiResponse addEmployeeManage(EmpRegisterDTO registerDTO) throws Exception;
}
