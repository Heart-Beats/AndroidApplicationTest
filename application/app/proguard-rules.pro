# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

#---------------------------------基本指令区-----------------------
# 设置混淆的压缩比率 0 ~ 7
-optimizationpasses 5
# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
# 指定不去忽略非公共库的成员
-dontskipnonpubliclibraryclassmembers
# 混淆时不做预校验
-dontpreverify
# 混淆时不记录日志
-verbose
# 代码优化
-dontshrink
# 不优化输入的类文件
-dontoptimize
# 保留注解不混淆
-keepattributes *Annotation*,InnerClasses
# 避免混淆泛型
-keepattributes Signature
# 保留代码行号，方便异常信息的追踪
-keepattributes SourceFile,LineNumberTable
# 混淆采用的算法
-optimizations !code/simplification/cast,!field/*,!class/merging/*
# dump.txt文件列出apk包内所有class的内部结构
-dump class_files.txt
# seeds.txt文件列出未混淆的类和成员
-printseeds seeds.txt
# usage.txt文件列出从apk中删除的代码
-printusage unused.txt
# mapping.txt文件列出混淆前后的映射
-printmapping mapping.txt


#不需混淆的Android类
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.preference.Preference
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View


#support下的所有类及其内部类
#support：
-keep class android.support.** {*;}
-dontwarn android.support.**
-keep interface android.support.** { *; }
#androidx：
-keep class androidx.** {*;}
-keep interface androidx.** {*;}
-keep public class * extends androidx.**
#support v4/7库
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**


#support design库
#support：
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
#androidx：
-keep class com.google.android.material.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**


#避免混淆自定义控件类的 get/set 方法和构造函数
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#避免资源混淆
-keep class **.R$* {*;}
-keepattributes InnerClasses

#避免layout中onclick方法（android:onclick="onClick"）混淆
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#避免混淆枚举类
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#避免Parcelable混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#避免Serializable接口的子类中指定的某些成员变量和方法混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}