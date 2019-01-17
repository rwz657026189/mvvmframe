package com.rwz.basemodule.entity.area;

import com.rwz.ui.IPickerEntity;

/**
 * Created by rwz on 2017/8/3.
 * 国家代码
 */
public class CountryCodeEntity implements IPickerEntity {
    /**
     * code : 110101
     * name : 东城区
     * des :
     */

    private String code;
    private String name;


    public CountryCodeEntity() {
    }

    public CountryCodeEntity(String name) {
        this.name = name;
        // -1 表示不限
        code = "-1";
    }

    public CountryCodeEntity(String name, String code) {
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
}
