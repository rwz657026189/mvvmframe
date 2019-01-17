package com.rwz.basemodule.entity.area;

import com.rwz.ui.IPickerEntity;

import java.util.List;

/**
 * Created by rwz on 2017/8/3.
 * 省份
 */

public class ProvinceEntity implements IPickerEntity {

    /**
     * code : 110000
     * name : 北京市
     */

    private String code;
    private String name;
    private List<CityEntity> des;

    public ProvinceEntity() {
    }

    public ProvinceEntity(String name) {
        this.name = name;
        // -1 表示不限
        code = "-1";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityEntity> getDes() {
        return des;
    }

    public void setDes(List<CityEntity> des) {
        this.des = des;
    }

    @Override
    public String getText() {
        return name;
    }

    @Override
    public String toString() {
        return "ProvinceEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
