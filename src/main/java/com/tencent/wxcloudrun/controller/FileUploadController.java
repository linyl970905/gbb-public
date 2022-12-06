package com.tencent.wxcloudrun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.vo.uploadFile.UploadFileVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ApiResponse uploadFileParam(@RequestParam String path) {
        // 请求参数
        Map<String, Object> requestBody = MapUtil.newHashMap();
        requestBody.put("env", "prod-9gdfw13rcabb4e9a");
        requestBody.put("path", path);

        // 请求上传文件链接接口
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

                cn.hutool.json.JSONObject responseJson = JSONUtil.parseObj(respBody);
                Integer errcode = responseJson.getInt("errcode");
                String errmsg = responseJson.getStr("errmsg");
                String url = responseJson.getStr("url");
                String token = responseJson.getStr("token");
                String authorization = responseJson.getStr("authorization");
                String fileId = responseJson.getStr("file_id");
                String cosFileId = responseJson.getStr("cos_file_id");

                UploadFileVO fileVO = new UploadFileVO()
                        .setErrcode(errcode).setErrmsg(errmsg).setUrl(url)
                        .setToken(token).setAuthorization(authorization)
                        .setFileId(fileId).setCosFileId(cosFileId);

                return ApiResponse.ok(fileVO);
            }
        }
        return ApiResponse.error("提示：请求失败！");
    }
}
