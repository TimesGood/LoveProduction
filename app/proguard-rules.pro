# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
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


#====WebView + js====
#-keepclassmembers class com.aige.loveproduction.mvp.ui.webview.MyWebView {
#   public *;
#}
#-keepclassmembers class com.aige.loveproduction.mvp.ui.webview.MyWebChromeClient {
#   public *;
#}
#-keepclassmembers class com.aige.loveproduction.mvp.ui.webview.MyWebViewClient {
#   public *;
#}
## keep 使用 webview 的类
#-keepclassmembers class com.aige.loveproduction.mvp.ui.activity.WebviewActivity {
#  public *;
#}
#-keepattributes *Annotation*
##解决：android sdk api >= 17 时需要加@JavascriptInterface”所出现的问题。
#-keepattributes *JavascriptInterface*

-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/artithmetic,!field
-keepattributes *Annotation*
-keepattributes Signature


# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# OkHttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# Retrofit
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn sun.misc.**
-dontwarn sorg.codehaus.mojo.animal_sniffer.**
-dontwarn org.codehaus.**
-dontwarn java.nio.**
-dontwarn java.lang.invoke.**
# RxJava RxAndroid
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}


################gson##################
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class com.google.gson.stream.** { *; }

###################loveproduction####################
-keep class com.aige.loveproduction.base.** {*;}
-keep class com.aige.loveproduction.bean.** {*;}
-keep class com.aige.loveproduction.bean.TransportBean$TransportBeans {
    public <methods>;
}
-keep class com.aige.loveproduction.net.** {*;}
-keep class com.aige.loveproduction.mvp.ui.customui.view.** {*;}
-keep class com.aige.loveproduction.mvp.ui.customui.viewgroup.** {*;}
-keep class com.aige.loveproduction.mvp.presenter.**{
    public *;
}