
#====WebView + js====
#-keepclassmembers class com.aige.loveproduction.ui.webview.MyWebView {
#   public *;
#}
#-keepclassmembers class com.aige.loveproduction.ui.webview.MyWebChromeClient {
#   public *;
#}
#-keepclassmembers class com.aige.loveproduction.ui.webview.MyWebViewClient {
#   public *;
#}
## keep 使用 webview 的类
#-keepclassmembers class com.aige.loveproduction.ui.activity.WebviewActivity {
#  public *;
#}
#-keepattributes *Annotation*
##解决：android sdk api >= 17 时需要加@JavascriptInterface”所出现的问题。
#-keepattributes *JavascriptInterface*
#************************************************
#ProGuard（混淆）三大作用
#一、压缩，默认开启，减少应用体积，移除未被使用的类和成员
#-dontshrink 关闭压缩
#二、优化，默认开启在字节码级别执行优化，让应用运行的更快
#-dontoptimize 关闭优化
#三、混淆，默认开启，增大反编译难度，类和类成员会被随机命名，除非用keep保护
#-dontobfuscate 关闭混淆
#*********************示例****************************
#一星表示保持该包下的类名，而子包下的类名还是会被混淆，二星表示把本包和所包含之下子包的类名都保持
#如果还想保持类之内的东西不被混淆就需要{*;}
#-keep class cn.hadcn.test.*
#-keep class cn.hadcn.test.**
#-keep class cn.hadcn.test.**{*;}
#还可以使用java语法规则避免混淆
#-keep public class * extends android.app.Activity
#如果要保持TransportBean的内部类TransportBeans中所有public方法不被混淆
#-keep class com.aige.loveproduction.base.TransportBean$TransportBeans {public *;}
#如果一个类中不希望保持全部内容不被混淆，希望保持类中的特定内容，可以：
#<init>;     //匹配所有构造器
#<fields>;   //匹配所有域
#<methods>;  //匹配所有方法方法
#还可以加入参数
#-keep class cn.hadcn.test.One {
#   public <init>(org.json.JSONObject);
#}
#如果不需要保持类名，只需要把该类下的特定方法不被混淆就好，那就用-keepclassmembers，官网表述
#**************************************************************************************
#*保留                       防止被移除或者被重命名             防止被重命名                 *
#*类和类成员                  -keep	                      -keepnames                  *
#*仅类成员	                -keepclassmembers	          -keepclassmembernames       *
#*如果拥有某成员，保留类和类成员	-keepclasseswithmembers	      -keepclasseswithmembernames *
#**************************************************************************************



-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt #输出混淆规则，可用该文件反编译回源码
-optimizations !code/simplification/artithmetic,!field
-keepattributes *Annotation*
-keepattributes Signature

#************************特别注意不能被混淆*****************************
-keepclassmembers enum com.aige.loveproduction.enums.* {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#jni方法不可混淆，因为这个方法需要和native方法保持一致
-keepclasseswithmembernames class * { # 保持native方法不被混淆
    native <methods>;
}
#****************************OkHttp+Retrofit+RxJava************************************
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
################图表##################
-keep class com.github.mikephil.charting.** {*;}


################gson##################
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class com.google.gson.stream.** { *; }

###################Base下的类以及JSON对象类不能被混淆####################
-keep class com.aige.loveproduction.base.** {*;}
-keep class com.aige.loveproduction.bean.** {*;}
-keep class com.aige.loveproduction.bean.*$* {
    public <methods>;
}
#-keep class com.aige.loveproduction.net.** {*;}
#-keep class com.aige.loveproduction.ui.customui.view.** {*;}
#-keep class com.aige.loveproduction.ui.customui.viewgroup.** {*;}
-keep class com.aige.loveproduction.mvp.presenter.**{
    public *;
}