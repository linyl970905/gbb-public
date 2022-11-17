package com.tencent.wxcloudrun.service;

/**
 * @Author: zero
 * @Data: 2022/11/16 17:23
 * @Motto: Nothing is impossible
 */
public interface FaceVerifyService {

    String searchFace(String faceUrl);

    String insertFace(String faceUrl, String userId);

    String updateFace(String faceUrl, String userId);

    String delGroup(String groupId);
}
