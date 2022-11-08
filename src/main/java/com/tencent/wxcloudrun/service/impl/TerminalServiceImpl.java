package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.TerminalMapper;
import com.tencent.wxcloudrun.model.EmployeeManage;
import com.tencent.wxcloudrun.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zero
 * @Data: 2022/11/7 16:32
 * @Motto: Nothing is impossible
 */
@Service
public class TerminalServiceImpl implements TerminalService {

    @Autowired
    private TerminalMapper terminalMapper;

    @Override
    public void addEmployeeFace(Integer faceId, String faceUrl) {
        terminalMapper.addEmployeeFace(faceId, faceUrl);
    }

    @Override
    public EmployeeManage getEmployeeByFaceId(Integer faceId) {
        return terminalMapper.getEmployeeByFaceId(faceId);
    }

    @Override
    public void addEmployeeManage(EmployeeManage manage) {
        // 1.调用证面提供的实名认证接口，验证实名。
        // 验证通过，调用【员工登记】接口,登记成功后，跳转到“已登记首页”。
        // 接口如失败，弹出失败信息。

        // 2.验证通过，更新员工信息
        terminalMapper.updateEmployeeManage(manage);
    }
}
