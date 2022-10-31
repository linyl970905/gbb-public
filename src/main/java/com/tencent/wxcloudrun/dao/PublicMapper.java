package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.dto.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: zero
 * @Data: 2022/10/31 15:18
 * @Motto: Nothing is impossible
 */
@Mapper
public interface PublicMapper {

    EmployeeVO getEmployeeById(Integer id);
}
