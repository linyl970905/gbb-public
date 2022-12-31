package com.tencent.wxcloudrun.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class WxJsapiTicket {

    // appid
    private String appid;

    // accessToken
    private String accessToken;

    // jsapiTicket
    private String jsapiTicket;

    // nonceStr
    private String nonceStr;

    // signature
    private String signature;

    // timestamp
    private String timestamp;

    // url
    private String url;
}
