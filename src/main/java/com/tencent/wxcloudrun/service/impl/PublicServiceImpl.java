package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.PublicMapper;
import com.tencent.wxcloudrun.dto.EmployeeVO;
import com.tencent.wxcloudrun.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zero
 * @Data: 2022/10/31 15:20
 * @Motto: Nothing is impossible
 */
@Service
public class PublicServiceImpl implements PublicService {

    @Autowired
    private PublicMapper publicMapper;

    @Override
    public EmployeeVO getEmployeeById(Integer id) {
        return publicMapper.getEmployeeById(id);
    }
}
