package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.EmployeeManage;

/**
 * @Author: zero
 * @Data: 2022/11/7 16:31
 * @Motto: Nothing is impossible
 */
public interface TerminalService {

    void addEmployeeFace(Integer faceId, String faceUrl);

    EmployeeManage getEmployeeByFaceId(Integer faceId);

    void addEmployeeManage(EmployeeManage manage);
}
