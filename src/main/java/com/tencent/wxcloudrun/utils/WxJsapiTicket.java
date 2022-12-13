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

    // nonceStr
    private String nonceStr;

    // signature
    private String signature;

    // timestamp
    private String timestamp;
}
