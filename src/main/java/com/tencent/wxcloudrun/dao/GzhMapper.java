package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Area;
import com.tencent.wxcloudrun.model.City;
import com.tencent.wxcloudrun.model.Province;

import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/8 16:20
 * @Motto: Nothing is impossible
 */
public interface GzhMapper {

    List<Province> getProvince();

    List<City> getCityByPid(Integer pid);

    List<Area> getAreaByPid(Integer pid);
}
