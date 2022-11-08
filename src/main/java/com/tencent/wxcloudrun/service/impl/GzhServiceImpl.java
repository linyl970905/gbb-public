package com.tencent.wxcloudrun.service.impl;

import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dao.GzhMapper;
import com.tencent.wxcloudrun.model.Area;
import com.tencent.wxcloudrun.model.City;
import com.tencent.wxcloudrun.model.Province;
import com.tencent.wxcloudrun.service.GzhService;
import com.tencent.wxcloudrun.utils.HttpUtils;
import com.tencent.wxcloudrun.vo.CityAreaList;
import com.tencent.wxcloudrun.vo.ProvinceCityList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/8 16:00
 * @Motto: Nothing is impossible
 */
@Service
public class GzhServiceImpl implements GzhService {

    @Autowired
    private GzhMapper gzhMapper;

    @Override
    public ApiResponse gzhAuthorizedLogin(String code) throws IOException {
        // 雇保宝公众号参数
        String appId = "";
        String appSecret = "";

        // 用户选择授权登录，前端自动获取code传给后台，后台通过code换取网页授权access_token
        if (null == code) {
            return ApiResponse.error("公众号免授权登录：code异常！");
        } else {
            String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";

            String requestParams = "appid=" + appId + "&secret=" + appSecret
                    + "&code=" + code + "&grant_type=authorization_code";

            String result = HttpUtils.sendGet(requestUrl, requestParams);

            cn.hutool.json.JSONObject headJson = JSONUtil.parseObj(result);
            String openid = headJson.getStr("openid");

            if (openid == null) {
                return ApiResponse.error("公众号免授权登录：获取openid异常！");
            }

            return ApiResponse.ok(openid);
        }
    }

    @Override
    public List<ProvinceCityList> getAllArea() {
        // 集合：获取所有省-市-区
        List<ProvinceCityList> allArea = new ArrayList<>();

        // 1.获取所有省
        List<Province> provinceList = gzhMapper.getProvince();
        for(Province p : provinceList) {
            // 2.获取所有市
            List<City> cityList = gzhMapper.getCityByPid(p.getId());

            ProvinceCityList provinceCity = new ProvinceCityList();
            provinceCity.setId(p.getId());
            provinceCity.setName(p.getName());

            List<CityAreaList> cityAreaList = new ArrayList<>();

            for (City c : cityList) {
                // 3.获取所有区
                List<Area> areaList = gzhMapper.getAreaByPid(c.getId());

                CityAreaList cityArea = new CityAreaList();
                cityArea.setId(c.getId());
                cityArea.setName(c.getName());
                cityArea.setAreaList(areaList);

                cityAreaList.add(cityArea);
            }

            provinceCity.setCityAreaList(cityAreaList);

            allArea.add(provinceCity);
        }

        return allArea;
    }
}
