package com.tencent.wxcloudrun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.config.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: zero
 * @Data: 2022/12/7 14:36
 * @Motto: Nothing is impossible
 */
@RestController
@RequestMapping("/wx/pay")
public class WxPayController {

    @GetMapping("/unifiedorder")
    public ApiResponse unifiedorder() {
        // 请求参数
        Map<String, Object> requestBody = MapUtil.newHashMap();
        requestBody.put("body", "测试微信支付！");
        requestBody.put("openid", "");
        requestBody.put("out_trade_no", "gbb20221207001");
        requestBody.put("spbill_create_ip", "127.0.0.1");
        requestBody.put("env_id", "prod-9gdfw13rcabb4e9a");
        requestBody.put("sub_mch_id", "1633720711");
        requestBody.put("total_fee", 100);
        requestBody.put("callback_type", 2);
        Map<String, Object> container = MapUtil.newHashMap();
        container.put("service", "pay");
        container.put("path", "/");
        requestBody.put("container", container);

        // 请求上传文件链接接口
        HttpRequest httpRequest = HttpRequest.post("http://api.weixin.qq.com/_/pay/unifiedorder");
        httpRequest.contentType("application/json");
        httpRequest.body(JSONUtil.toJsonStr(requestBody));
        httpRequest.setConnectionTimeout(18000);
        httpRequest.setReadTimeout(20000);
        HttpResponse execute = null;

        try {
            execute = httpRequest.execute();
        } catch (HttpException e) {
            e.printStackTrace();
        }
        if (execute == null) {
            return ApiResponse.error("提示：请求失败！");
        } else {
            if (execute.isOk()) {
                String respBody = execute.body();

                return ApiResponse.ok(respBody);
            }
        }
        return ApiResponse.error("提示：请求失败！");
    }
}
