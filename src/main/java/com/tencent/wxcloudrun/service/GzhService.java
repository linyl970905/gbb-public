package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.vo.ProvinceCityList;

import java.io.IOException;
import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/8 15:59
 * @Motto: Nothing is impossible
 */
public interface GzhService {

    ApiResponse gzhAuthorizedLogin(String code) throws IOException;

    List<ProvinceCityList> getAllArea();
}
