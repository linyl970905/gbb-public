package com.tencent.wxcloudrun.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import com.tencent.wxcloudrun.service.FaceVerifyService;
import com.tencent.wxcloudrun.utils.baidu.FaceAPIConstants;
import com.tencent.wxcloudrun.utils.baidu.FaceAuthUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: zero
 * @Data: 2022/11/16 17:24
 * @Motto: Nothing is impossible
 */
@Service
public class FaceVerifyServiceImpl implements FaceVerifyService {

    @Override
    public String searchFace(String faceUrl) {

        // 获取access_token
        FaceAuthUtil.setFaceAuth();
        String accessToken = FaceAPIConstants.TOKEN;

        String path = "https://aip.baidubce.com/rest/2.0/face/v3/search?access_token=" + accessToken;
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);

        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");

        // 设置jsonBody参数
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("image", faceUrl);
        map.put("image_type", "URL");
        map.put("group_id_list", "gubaobao");

        String apiParams = JSONUtil.toJsonStr(map);
        request.setJsonBody(apiParams);

        ApiExplorerClient client = new ApiExplorerClient();

        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            return response.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String insertFace(String faceUrl, String userId) {

        // 获取access_token
        FaceAuthUtil.setFaceAuth();
        String accessToken = FaceAPIConstants.TOKEN;

        String path = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add?access_token=" + accessToken;
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);

        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");

        // 设置jsonBody参数
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("image", faceUrl);
        map.put("image_type", "URL");
        map.put("group_id", "gubaobao");
        map.put("user_id", userId);

        String apiParams = JSONUtil.toJsonStr(map);
        request.setJsonBody(apiParams);

        ApiExplorerClient client = new ApiExplorerClient();

        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            return response.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String updateFace(String faceUrl, String userId) {

        // 获取access_token
        FaceAuthUtil.setFaceAuth();
        String accessToken = FaceAPIConstants.TOKEN;

        String path = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/update?access_token=" + accessToken;
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);

        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");

        // 设置jsonBody参数
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("image", faceUrl);
        map.put("image_type", "URL");
        map.put("group_id", "gubaobao");
        map.put("user_id", userId);

        String apiParams = JSONUtil.toJsonStr(map);
        request.setJsonBody(apiParams);

        ApiExplorerClient client = new ApiExplorerClient();

        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            return response.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String delGroup(String groupId) {

        // 获取access_token
        FaceAuthUtil.setFaceAuth();
        String accessToken = FaceAPIConstants.TOKEN;

        String path = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/group/delete?access_token=" + accessToken;
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);

        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");

        // 设置jsonBody参数
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("group_id", groupId);

        String apiParams = JSONUtil.toJsonStr(map);
        request.setJsonBody(apiParams);

        ApiExplorerClient client = new ApiExplorerClient();

        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            return response.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
