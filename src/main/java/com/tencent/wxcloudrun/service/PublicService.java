package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.EmployeeVO;

/**
 * @Author: zero
 * @Data: 2022/10/31 15:19
 * @Motto: Nothing is impossible
 */
public interface PublicService {

    EmployeeVO getEmployeeById(Integer id);
}
