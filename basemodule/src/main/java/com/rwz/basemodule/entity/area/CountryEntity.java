package com.rwz.basemodule.entity.area;

import com.rwz.ui.IPickerEntity;

import java.util.List;

/**
 * Created by rwz on 2017/8/3.
 * 国家
 */
public class CountryEntity implements IPickerEntity {
    /**
     * code : 110101
     * name : 东城区
     * des :
     */

    private String code;
    private String name;
    private List<ProvinceEntity> des;


    public CountryEntity() {
    }

    public CountryEntity(String name) {
        this.name = name;
        // -1 表示不限
        code = "-1";
    }

    public CountryEntity(String name, String code) {
        this.code = code;
        this.name = name;
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

    public List<ProvinceEntity> getDes() {
        return des;
    }

    public void setDes(List<ProvinceEntity> des) {
        this.des = des;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getText() {
        return name;
    }

    @Override
    public String toString() {
        return "CountryEntity{" +
                "name='" + name + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        CountryEntity entity = (CountryEntity) object;

        return code != null ? code.equals(entity.code) : entity.code == null;

    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }
}
