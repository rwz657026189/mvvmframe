# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#优化  不优化输入的类文件
-dontoptimize
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
-keep public class android.support.v4.app.FragmentManagerImpl
#忽略警告
-ignorewarning

#####################记录生成的日志数据,gradle build时在本项目根目录输出################
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#再次崩溃的时候就有源文件和行号的信息了
-keepattributes SourceFile,LineNumberTable
#####################记录生成的日志数据，gradle build时 在本项目根目录输出-end################

################混淆保护自己项目的部分代码以及引用的第三方jar包library#########################
#-libraryjars libs/nineoldandroids-2.4.0.jar
#点9动画
-keep class com.nineoldandroids.**{*;}
#gson
-keepattributes Signature
-keep class com.google.gson.**{*;}
-keep class sun.misc.Unsafe { *; }
#okhttp
-keep class com.squareup.okhttp.**{*;}
#picasso
-keep class com.squareup.picasso.**{*;}
#glide
-keep class com.github.bumptech.glide.**{*;}
#混淆glide配置
-keep public class * implements com.bumptech.glide.module.GlideModule
#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


#litepal
-dontwarn org.litepal.**
-keep class org.litepal.** { *; }
#友盟
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
 # 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# rxjava、rxAndroid
-dontwarn rx.**
-keep class rx.**{*;}
# RxJava 混淆代码
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

##保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

##不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#避免混淆泛型 如果混淆报错建议关掉，如果使用gson需要打开
-keepattributes Signature
-keepattributes EnclosingMethod

#如果引用了v4或者v7包
-dontwarn android.support.**
-keep class android.support.** {*;}
# webview + js
-keepattributes *JavascriptInterface*
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

#TalkingData
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}
-keep class dice.** {*; }
-dontwarn dice.**

#jcVideoPlayer视频播放框架
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**
-keep class fm.jiecao.jcvideoplayer_lib.** { *; }
-dontwarn fm.jiecao.jcvideoplayer_lib.**

##Eventbus混淆
-keepclassmembers class ** {
    public void onEvent*(***);
}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    public <init>(java.lang.Throwable);
}
# Don't warn for missing support classes
-dontwarn de.greenrobot.event.util.*$Support
-dontwarn de.greenrobot.event.util.*$SupportManagerFragment


#swipebacklayout右滑退出框架
-keep class me.imid.swipebacklayout.lib.** { *; }
-dontwarn me.imid.swipebacklayout.lib.**

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}


#okio
-dontwarn okio.**
-keep class okio.**{*;}

#图片选择框架
-keep class com.lzy.imagepicker.** { *; }
-dontwarn com.lzy.imagepicker.**

#圆角图片
-keep class com.makeramen.roundedimageview.** { *; }
-dontwarn com.makeramen.roundedimageview.**

#Retrofit
-dontwarn okio.**
-dontwarn javax.annotation.**

#友盟推送
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }

############混淆保护自己项目的部分代码以及引用的第三方jar包library-end##################



##########################  引入的module             ##########################

#保护basedatabinding
-keep class com.rwz.basedatabinding.**{*;}
-dontwarn com.rwz.basedatabinding.**

#保护baselist
-keep class com.rwz.baselist.**{*;}
-dontwarn com.rwz.baselist.**

#保护commonmodule
-keep class com.rwz.commonmodule.**{*;}
-dontwarn com.rwz.commonmodule.**

#保护libsdk
-keep class com.rwz.libsdk.**{*;}
-dontwarn com.rwz.libsdk.**

#保护network
-keep class com.rwz.network.**{*;}
-dontwarn com.rwz.network.**

#保护swipeback
-keep class com.rwz.swipeback.**{*;}
-dontwarn com.rwz.swipeback.**

#保护ui
-keep class com.rwz.ui.**{*;}
-dontwarn com.rwz.ui.**

#保护xrecyclerview
-keep class com.jcodecraeer.xrecyclerview.**{*;}
-dontwarn com.rwz.ui.**

##########################  引入的module END     ##########################



#-keepclasseswithmembers  class android.support.v4.app.FragmentManagerImpl   # 保持哪些类不被混淆
-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.msg.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.msg.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆


###################################混淆保护自己的代码#################################
#gson需要实体不能被混淆
-keep class com.rwz.basemodule.entity.**{*;}
-keep class com.rwz.basemodule.entity.sql.**{*;}
-keep class com.rwz.basemodule.widget.**{*;}
-keep class com.rwz.basemodule.widget.JumpBt{*;}
-keep public class * extends android.widget.EditText{*;}

-keepclasseswithmembers class * {
public <init>(android.msg.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
public <init>(android.msg.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}
###############################混淆保护自己的代码  end################################