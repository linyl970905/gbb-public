package com.tencent.wxcloudrun.utils.baidu;

public class FaceAPIConstants {

    // 人脸识别API验证信息
    // (value = "百度人脸识别应用appid")
    public static final String APP_ID = "28439865";
    // (value = "百度人脸识别应用apikey")
    public static final String API_KEY = "WPo4eC3IYFYcWRZovypWWdbG";
    // (value = "百度人脸识别应用sercetkey")
    public static final String SECRET_KEY = "LRKnCExNyFN24gLRbIKkqYHQOfRjAsdU";
    // (value = "百度人脸识别token 有效期一个月，避免失效，采用实时获取")
    public static String TOKEN = "";
 
    // (value = "请求类型")
    public static final String CONTENT_TYPE = "application/json";
 
    // 人脸识别API
    // (value = "人脸添加API")
    public static final String ADD_URL = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
    // (value = "人脸更新API")
    public static final String UPDATE_URL = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/update";
    // (value = "人脸删除API")
    public static final String DELETE_URL = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/face/delete";
    // (value = "人脸搜索API")
    public static final String SEARCH_URL = "https://aip.baidubce.com/rest/2.0/face/v3/search";
    // (value = "人脸检测API")
    public static final String DETECT_URL = "https://aip.baidubce.com/rest/2.0/face/v3/detect";
}