package com.tencent.wxcloudrun.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/11/15 14:42
 * @Motto: Nothing is impossible
 */
@Data
@Accessors(chain = true)
public class MerEmpRelation implements Serializable {

    // 主键id
    private Integer id;

    // 商户id
    private Integer merId;

    // 雇员id
    private Integer empId;
}
