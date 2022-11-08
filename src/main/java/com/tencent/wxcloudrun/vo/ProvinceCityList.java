package com.tencent.wxcloudrun.vo;

import com.tencent.wxcloudrun.model.City;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zero
 * @Data: 2022/11/8 16:30
 * @Motto: Nothing is impossible
 */
@Data
public class ProvinceCityList implements Serializable {

    // 主键id
    private Integer id;

    // 省-名称
    private String name;

    // 区集合
    private List<CityAreaList> cityAreaList;
}
