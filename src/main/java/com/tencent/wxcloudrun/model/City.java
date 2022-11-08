package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/11/8 15:56
 * @Motto: Nothing is impossible
 */
@Data
public class City implements Serializable {

    // 主键id
    private Integer id;

    // 市-名称
    private String name;

    // 省id
    private Integer pid;
}
