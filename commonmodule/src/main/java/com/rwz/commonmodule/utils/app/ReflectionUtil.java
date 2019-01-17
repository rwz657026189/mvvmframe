package com.rwz.commonmodule.utils.app;


import android.text.TextUtils;

import com.rwz.commonmodule.utils.show.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 反射工具类
 */
public class ReflectionUtil {

    private static final String TAG      = "ReflectionUtil";

    /**
     * 获取类里面的所在字段
     */
    public static Field[] getFields(Class clazz) {
        Field[] fields = null;
        fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            Class superClazz = clazz.getSuperclass();
            if (superClazz != null) {
                fields = getFields(superClazz);
            }
        }
        return fields;
    }

    /**
     * 获取类里面的指定对象，如果该类没有则从父类查询
     */
    public static Field getField(Class clazz, String name) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            try {
                field = clazz.getField(name);
            } catch (NoSuchFieldException e1) {
                if (clazz.getSuperclass() == null) {
                    return null;
                } else {
                    field = getField(clazz.getSuperclass(), name);
                }
            }
        }
        if (field != null) {
            field.setAccessible(true);
        }
        return field;
    }

    /**
     * 利用递归找一个类的指定方法，如果找不到，去父亲里面找直到最上层Object对象为止。
     * @param clazz      目标类
     * @param methodName 方法名
     * @param params     方法参数类型数组
     * @return 方法对象
     */
    public static Method getMethod(Class clazz, String methodName, final Class<?>... params) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, params);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getMethod(methodName, params);
            } catch (NoSuchMethodException ex) {
                if (clazz.getSuperclass() == null) {
                    LogUtil.e(TAG, "无法找到" + methodName + "方法");
                    return null;
                } else {
                    method = getMethod(clazz.getSuperclass(), methodName, params);
                }
            }
        }
        if (method != null) {
            method.setAccessible(true);
        }
        return method;
    }

    /**
     *  通过反射获取对象的指定属性（包括父类私有）
     */
    public static Object getDeclaredField(Object obj, String fieldName) {
        if(obj == null || TextUtils.isEmpty(fieldName))
            return null;
        Field field = getField(obj.getClass(), fieldName);
        try {
            return field == null ? null : field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  修改某对象的值（包括父类私有）
     */
    public static void modifyDeclaredField(Object obj, String fieldName, Object value) {
        if(obj == null || TextUtils.isEmpty(fieldName))
            return;
        Field field = getField(obj.getClass(), fieldName);
        try {
            if(field != null)
                field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改私有静态常量的值
     */
    public static void modifyStaticField(String className, String fieldName, Object value){
        try {
            modifyStaticField(Class.forName(className), fieldName, value);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void modifyStaticField(Class cla, String fieldName, Object value) {
        if(cla == null || TextUtils.isEmpty(fieldName))
            return;
        try {
            Field field = cla.getDeclaredField(fieldName);
            //忽略访问权限
            field.setAccessible(true);
            //忽略static修饰符
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers()&~Modifier.FINAL);
            field.set(null, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
