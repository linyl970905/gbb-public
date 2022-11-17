package com.tencent.wxcloudrun.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/11/17 14:41
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class FaceInfoVO implements Serializable {

    // 设备编码
    private String snCode;

    // 脸部id
    private Integer faceId;
}
