package com.tencent.wxcloudrun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.vo.uploadFile.UploadFileVO;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
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


    @PostMapping("/batchdownloadfile")
    public ApiResponse batchdownloadfile(@RequestBody Array[] fileList) {
        // 请求参数
        Map<String, Object> requestBody = MapUtil.newHashMap();
        requestBody.put("env", "prod-9gdfw13rcabb4e9a");

        List<Map<String, Object>> fileArrayList = new ArrayList<>();
        for (int i = 0; i <= fileList.length; i++) {
            Map<String, Object> list = MapUtil.newHashMap();
            list.put("fileid", fileList[i]);
            list.put("max_age", 7200);
            fileArrayList.add(list);
        }
        requestBody.put("file_list", fileArrayList);

        // 请求上传文件链接接口
        HttpRequest httpRequest = HttpRequest.post("https://api.weixin.qq.com/tcb/batchdownloadfile");
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
                JSONArray fileArray = responseJson.getJSONArray("file_list");

                return ApiResponse.ok(fileArray);
            }
        }
        return ApiResponse.error("提示：请求失败！");
    }

}
