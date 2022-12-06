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
 * @Data: 2022/12/5 15:07
 * @Motto: Nothing is impossible
 */
@RestController
@RequestMapping("/upload/file")
public class FileUploadController {

    @GetMapping("/uploadFileParam")
    public ApiResponse uploadFileParam() {
        // 请求参数
        Map<String, Object> requestBody = MapUtil.newHashMap();
        requestBody.put("env", "prod-9gdfw13rcabb4e9a");
        requestBody.put("path", "business/test.png");

        HttpRequest httpRequest = HttpRequest.post("https://api.weixin.qq.com/tcb/uploadfile");
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
                return ApiResponse.ok("提示：返回数据--" + respBody);
            }
        }
        return ApiResponse.ok();
    }
}
