# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keep public class demo.Pinyin4jAppletDemo
-dontwarn demo.*
-dontwarn android.**

-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#---------------
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#libary
#-libraryjars libs/pinyin4j-2.5.0.jar;libs/ksoap2-android-assembly-3.3.0-jar-with-dependencies.jar;libs/greendao-1.3.7.jar;libs/achartengine-1.1.0.jar;libs/gson-2.2.4.jar;libs/universal-image-loader-1.9.1-with-sources.jar;libs/eventbus-2.2.1.jar
#-libraryjars libs/alipay.jar;libs/pkcs7.jar;libs/BaiduLBS_Android.jar;libs/locSDK_3.1.jar
#libraryjars libs/android-support-v4.jar
#libraryjars libs/android-support-v7-appcompat.jar

-dontwarn android.support.v7.**
-keep class android.support.v7.**{*;}
-keep interface android.support.v7.**{*;}

-dontwarn android.support.v4.**
-keep class android.support.v4.**{*;}
-keep interface android.support.v4.**{*;}

-dontwarn demo.*
-keep class demo.**{*;}

-dontwarn org.xmlpull.v1.**
-keep class org.ksoap2.**{*;}
-keep class org.kxml2.**{*;}
-keep class org.kobjects.**{*;}
-keep class org.xmlpull.**{*;}

-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}
 #removes such information by default, so configure it to keep all of it.
-keepattributes Signature


# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }


# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

-dontwarn de.greenrobot.dao.**
-keep class de.greenrobot.dao.**{*;}

-dontwarn de.greenrobot.event.**
-keep class de.greenrobot.event.**{*;}

-dontwarn org.achartengine.**
-keep class org.achartengine.**{*;}

-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.**{*;}

-dontwarn com.bocnet.common.security.**
-keep class com.bocnet.common.security.**{*;}

-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

#-dontwarn com.alipay.android.app.**
#-keep com.alipay.android.app.**{*;}


#end libary

#beans
-keep class com.gwi.selfplatform.module.net.beans.** {
    <fields>;
    <methods>;
}

-keep class com.gwi.selfplatform.module.net.request.** {
    <fields>;
    <methods>;
}

-keep class com.gwi.selfplatform.module.net.response.** {
    <fields>;
    <methods>;
}
#end beans

#db entity
-keep class com.gwi.selfplatform.db.gen.**{
    <fields>;
    <methods>;
}

-keep class com.gwi.selfplatform.db.**{
    <fields>;
    <methods>;
}
#end entity

# ProGuard configurations for NetworkBench Lens
#-keep class com.networkbench.** { *; }
#-dontwarn com.networkbench.**
#-keepattributes Exceptions, Signature, InnerClasses
# End NetworkBench Lens
#---------------

-keep class com.alipay.android.app.IAliPay{*;}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}

-dontwarn com.alipay.apmobilesecuritysdk.**
-dontwarn com.alipay.tscenter.biz.rpc.**

-dontwarn com.baidu.**
-keep class com.baidu.**{*;}
-keep class vi.com.gdi.bgl.android.** {*; }

# 百度PUSH
#-libraryjars libs/pushservice-4.2.0.63.jar
-dontwarn com.baidu.**

# Guava
-dontoptimize
-dontobfuscate
-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.collect.MinMaxPriorityQueue

#okhttp
-dontwarn okio.**

#jni
-keep class com.gwi.selfplatform.jni.**{*;}

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

#gradle-retrolambda
-dontwarn java.lang.invoke.*
