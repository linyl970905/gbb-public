package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.utils.JsapiTicketUtil;
import com.tencent.wxcloudrun.utils.WxJsapiTicket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zero
 * @Data: 2022/12/13 14:55
 * @Motto: Nothing is impossible
 */
@RestController
@RequestMapping("/gbb/authorize")
public class AuthorizeController {

    @GetMapping("/getParams")
    public ApiResponse authorizeParams(@RequestParam String url) throws Exception {
        // 获取所有需要使用到的数据
        String appid = "wxdf2bfef7aaa15a33";	// appid
        String accessToken = JsapiTicketUtil.getAccessToken();	// accessToken
        String jsapiTicket = JsapiTicketUtil.getJSApiTicket(accessToken);	// jsapiTicket
        String nonceStr = JsapiTicketUtil.getNonceStr();    // nonceStr
        String timestamp = String.valueOf(JsapiTicketUtil.getTimestamp() / 1000);    // timestamp

        // 需要加密的字符串--sha1加密
        String strSha = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;

        String signature = JsapiTicketUtil.getSha1(strSha);

        WxJsapiTicket data = new WxJsapiTicket().setAppid(appid).setNonceStr(nonceStr).setSignature(signature).setTimestamp(timestamp);

        return ApiResponse.ok(data);
    }
}
