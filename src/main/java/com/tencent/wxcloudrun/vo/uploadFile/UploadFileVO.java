package com.tencent.wxcloudrun.vo.uploadFile;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/12/6 15:34
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class UploadFileVO implements Serializable {

    // 返回状态
    private Integer errcode;

    // 返回状态信息
    private String errmsg;

    // 上传url
    private String url;

    // token
    private String token;

    // authorization
    private String authorization;

    // 文件ID
    private String fileId;

    // cos文件ID
    private String cosFileId;
}
