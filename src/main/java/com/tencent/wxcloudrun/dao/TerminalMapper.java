package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.EmployeeManage;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: zero
 * @Data: 2022/11/7 16:32
 * @Motto: Nothing is impossible
 */
public interface TerminalMapper {

    void addEmployeeFace(@Param("faceId") Integer faceId, @Param("faceUrl") String faceUrl);

    EmployeeManage getEmployeeByFaceId(Integer faceId);

    void updateEmployeeManage(EmployeeManage manage);
}
