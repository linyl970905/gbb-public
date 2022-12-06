package com.tencent.wxcloudrun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: zero
 * @Data: 2022/12/5 15:07
 * @Motto: Nothing is impossible
 */
@RestController
@RequestMapping("/file/upload")
public class FileUploadController {

    public static void main(String[] args) {

        Map<String, Object> requestBody = MapUtil.newHashMap();
        requestBody.put("env", "prod-9gdfw13rcabb4e9a");
        requestBody.put("path", "business/1.zip");


        HttpRequest httpRequest = HttpRequest.post("https://api.weixin.qq.com/tcb/uploadfile?cloudbase_access_token=ACCESS_TOKEN");
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
            System.out.println("响应数据为空！");
        } else {
            if (execute.isOk()) {
                String respBody = execute.body();

                System.out.println(requestBody);
            }
        }

        }

}
