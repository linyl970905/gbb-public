package com.tencent.wxcloudrun.config;

import lombok.Data;

import java.util.Map;

/**
 * @Author: zero
 * @Data: 2022/12/14 10:18
 * @Motto: Nothing is impossible
 */
@Data
public final class ResultMsg {

    // 接口返回值
    // 0-成功！-SUCCESS 999-接口异常！-FAIL
    // 101-未查询到设备信息！ 102-设备未激活！
    // 201-对比百度智能云人脸识别异常！ 202-用户未与商户进行绑定！ 203-用户未注册！
    private Integer code;

    // 提示信息
    private String returnMsg;

    // 响应数据
    private Map<String, String> mapData;

    public ResultMsg(Integer code, String returnMsg, Map<String, String> mapData){
        this.code = code;
        this.returnMsg = returnMsg;
        this.mapData = mapData;
    }

    public static ResultMsg respData(Integer code, String returnMsg, Map<String, String> mapData) {
        return new ResultMsg(code, returnMsg, mapData);
    }
}
