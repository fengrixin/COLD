# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\StudySoftware\Android\adt-bundle-windows-x86_64-20130219\sdk/tools/proguard/proguard-android.txt
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

# 四大组件及基本类
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.support.v4.**
    -keep public class * extends android.support.annotation.**
    -keep public class * extends android.support.v7.**
    -keep public class android.app.Notification
    -keep public class android.webkit.**

#保护WebView对HTML页面的API不被混淆
    -keep class **.Webview2JsInterface {*; }
    -keep public class * extends android.app.Dialog
    -keep public class * extends android.view

# 所有枚举类型不要混淆
    -keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    }

# 保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
    native <methods>;
    }

#保持R文件不被混淆，否则，你的反射是获取不到资源id的
    -keep class **.R*{*;}

# parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
    public static finalandroid.os.ParcelableCreator *;
    }

#保持实现"Serializable"接口的类不被混淆
    -keepnames class * implements java.io.Serializable

#保护实现接口Serializable的类中，指定规则的类成员不被混淆
    -keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <methods>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    }