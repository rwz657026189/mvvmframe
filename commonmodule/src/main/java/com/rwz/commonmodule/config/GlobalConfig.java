package com.rwz.commonmodule.config;

/**
 * Created by rwz on 2017/3/9.
 * 全局属性 F:\13001-1.apk
 */

public interface GlobalConfig {

    //是否debug模式 很重要【发布一定置为false】否则影响性能
    boolean isDebug = true;
    //本地
    String LOCAL_HTTP = "https://www.baidu.com/";
    //线上
    String ONLINE_HTTP = "https://www.baidu.com/";
    //主机地址
    String MAIN_HOST = ONLINE_HTTP;
    
    //*****************************************************************************************************//
    //是否显示log日志
    boolean showLog = isDebug;
    //是否允许开启开发者选框
    boolean ALLOW_OPEN_DEV_DIALOG = isDebug;
    //关闭/开启统计(所有统计及其相关都将关闭/开启， 新项目、新版本建议关闭)
    boolean OPEN_STATISTICS = true;
    //显示错误网络请求信息
    boolean SHOW_NET_ERROR = isDebug;
    //双击退出程序时间间隔
    int EXIT_APP_DOUBLE_CLICK_TIME = 2000;
    //欢迎界面展示时间
    int LAUNCH_TIME = 3000 ;
    //一键回顶,是否平滑滚动列表
    boolean isSmoothScrollList = false;
    //头信息
    String APP_ID = "";
    //默认渠道
    String DEFAULT_CHANNEL = "play.google.com";
    //头信息
    String APP_KEY = "";
    //本地加密
    String SEED = "@!*&$123456789";
    //TalkingData appID
    String TALKING_DATA_APP_ID = "";

}
