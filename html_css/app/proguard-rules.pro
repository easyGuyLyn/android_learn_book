# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/fei/Android/Sdk/tools/proguard/proguard-android.txt
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
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-optimizationpasses 5
#->设置混淆的压缩比率 0 ~ 7
-dontusemixedcaseclassnames
#-> Aa aA
-dontskipnonpubliclibraryclasses
#->如果应用程序引入的有jar包,并且想混淆jar包里面的class
-dontpreverify
-verbose
#->混淆后生产映射文件 map 类名->转化后类名的映射
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#->混淆采用的算法.

###-----------基本配置-不能被混淆的------------
-keep public class * extends android.app.Activity
#->所有activity的子类不要去混淆
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-dontwarn android.support.v4.**
-dontwarn android.annotation


#-libraryjars libs/android-support-v4.jar


-keepclasseswithmembernames class * {
#-> 所有native的方法不能去混淆.
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    #-->某些构造方法不能去混淆
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keep class com.dawoo.gamebox.MainActivity$Inner{
    public <fields>;
    public <methods>;
}
-keep class com.dawoo.gamebox.activity.*$Inner{
    public <fields>;
    public <methods>;
}

#-libraryjars src/main/libs/commons-beanutils.jar
#-libraryjars src/main/libs/commons-lang.jar
#-libraryjars src/main/libs/commons-logging.jar
#-libraryjars src/main/libs/ezmorph-1.0.6.jar
#-libraryjars src/main/libs/json-lib-2.2.2-jdk15.jar

-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.zhy.http.okhttp.** { *; }
-keep class com.zhy.** { *; }
-keep class okhttp3.** { *; }
-keep class net.sf.json.xml.** { *; }
-dontwarn com.google.gson.stream.**
-dontwarn com.google.gson.examples.android.model.**
-dontwarn com.zhy.http.okhttp.**
-dontwarn com.zhy.**
-dontwarn okhttp3.**
-dontwarn net.sf.json.xml.**

-keep class com.jaeger.** { *; }
-dontwarn com.jaeger.**
-keep class com.google.code.** { *; }
-dontwarn  com.google.code.**
-keep class com.android.** { *; }
-dontwarn com.android.**
-keep class com.blankj.** { *; }
-dontwarn com.blankj.**
-keep class com.loopj.android.** { *; }
-dontwarn com.loopj.android.**
-keep class com.github.yaming116.** { *; }
-dontwarn com.github.yaming116.**
-keep class com.squareup.picasso.** { *; }
-dontwarn com.squareup.picasso.**
-keep class okio.** { *; }
-dontwarn okio.**
-keep class net.sf.json.** { *; }
-dontwarn net.sf.**
-dontwarn org.apache.commons.**
#support.v4/v7包不混淆
#-keep class android.support.** { *; }
#-keep class android.support.v4.** { *; }
#-keep public class * extends android.support.v4.**
#-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.**  # 忽略警告

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

