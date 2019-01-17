package com.rwz.basemodule.entity.area;

import com.rwz.ui.IPickerEntity;

import java.util.List;

/**
 * Created by rwz on 2017/8/3.
 * 市
 */
public class CityEntity implements IPickerEntity {
    /**
     * code : 110100
     * name : 北京市
     */

    private String code;
    private String name;
    private List<AreaEntity> des;

    public CityEntity() {
    }

    public CityEntity(String name) {
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

    public List<AreaEntity> getDes() {
        return des;
    }

    public void setDes(List<AreaEntity> des) {
        this.des = des;
    }

    @Override
    public String getText() {
        return name;
    }

    @Override
    public String toString() {
        return "CityEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
