//package com.aige.loveproduction.mvp.ui.activity;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.aige.loveproduction.R;
//import com.aige.loveproduction.mvp.ui.webview.WebViewJSInterface;
//import com.aige.loveproduction.util.GetPathFromUri4kitkat;
//import com.aige.loveproduction.util.WebviewGlobals;
//import com.aige.loveproduction.mvp.ui.webview.MyWebView;
//import com.tencent.smtt.sdk.ValueCallback;
//
//import java.io.File;
//
///**
//
// * Used webview
// */
//
//public class WebviewActivity extends AppCompatActivity implements View.OnClickListener{
//	private static final String TAG = WebviewActivity.class.getSimpleName();
//	private MyWebView webView;
//	private EditText url_edit;
//	private Button go_btn;
//	private ImageButton back_btn,forward_btn,more_btn,home_btn,exit_btn;
//	//首页网址
//	private static String mHomeUrl = null;
//	//地址栏显示最大长度
//	private static final int MAX_LENGTH = 14;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_webview);
//		initViews();
//		initDatas();
//		initBtnListenser();
//	}
//
//	//退出界面时，防止内存溢出销毁webview
//	@Override
//	public void onDestroy()
//	{
//		webView.removeAllViews();
//		webView.destroy();
//		super.onDestroy();
//	}
//
//	/**
//	 * 初始化网页设置
//	 */
//	private void initViews() {
//		webView = findViewById(R.id.web_view);
//		webView.setCanBackPreviousPage(true,WebviewActivity.this);//可以返回上一页
//	}
//
//	/**
//	 * 打开网页
//	 */
//	private void initDatas() {
//		//获取网页地址
//		mHomeUrl = getIntent().getStringExtra("urlKey");
//		if(TextUtils.isEmpty(mHomeUrl)){
//			webView.loadLocalUrl("demo.html");
//		}else {
//			webView.loadWebUrl(mHomeUrl);
//		}
//	}
//
//
//	/*=========================================实现webview调用相机、打开文件管理器功能==============================================*/
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		Log.w(TAG, "{onActivityResult}resultCode="+resultCode);
//		Log.w(TAG, "{onActivityResult}requestCode="+requestCode);
//		Log.w(TAG, "{onActivityResult}data="+data);
//		if (resultCode == Activity.RESULT_OK) {
//			//webview界面调用打开本地文件管理器选择文件的回调
//			if (requestCode == WebviewGlobals.CHOOSE_FILE_REQUEST_CODE ) {
//				Uri result = data == null ? null : data.getData();
//				Log.w(TAG,"{onActivityResult}文件路径地址：" + result.toString());
//
//				//如果mUploadMessage或者mUploadCallbackAboveL不为空，代表是触发input[type]类型的标签
//				if (null != webView.getWebChromeClient().getmUploadMessage() || null != webView.getWebChromeClient().getmUploadCallbackAboveL()) {
//					if (webView.getWebChromeClient().getmUploadCallbackAboveL() != null) {
//						onActivityResultAboveL(requestCode, data);//5.0++
//					} else if (webView.getWebChromeClient().getmUploadMessage() != null) {
//						webView.getWebChromeClient().getmUploadMessage().onReceiveValue(result);//将文件路径返回去，填充到input中
//						webView.getWebChromeClient().setmUploadMessage(null);
//					}
//				}else{
//					//此处代码是处理通过js方法触发的情况
//					Log.w(TAG,"{onActivityResult}文件路径地址(js)：" + result.toString());
//					String filePath = GetPathFromUri4kitkat.getPath(WebviewActivity.this, Uri.parse(result.toString()));
//					setUrlPathInput(webView,"打开本地相册：" + filePath);//修改网页输入框文本
//				}
//			}
//			//因为拍照指定了路径，所以data值为null
//			if(requestCode == WebviewGlobals.CAMERA_REQUEST_CODE){
//				File pictureFile = new File(WebViewJSInterface.mCurrentPhotoPath);
//
//				Uri uri = Uri.fromFile(pictureFile);
//				Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//				intent.setData(uri);
//				WebviewActivity.this.sendBroadcast(intent);  // 这里我们发送广播让MediaScanner 扫描我们制定的文件
//				//这样在系统的相册中我们就可以找到我们拍摄的照片了【但是这样一来，就会执行MediaScanner服务中onLoadFinished方法，所以需要注意】
//
//				//拍照
//				//String fileName = FileUtils.getFileName(WebViewJSInterface.mCurrentPhotoPath);
//				Log.e(TAG,"WebViewJSInterface.mCurrentPhotoPath="+ WebViewJSInterface.mCurrentPhotoPath);
//				setUrlPathInput(webView,"打开相机：" + WebViewJSInterface.mCurrentPhotoPath);//修改网页输入框文本
//			}
//
//			//录音
//			if(requestCode == WebviewGlobals.RECORD_REQUEST_CODE){
//				Uri result = data == null ? null : data.getData();
//				Log.w(TAG,"录音文件路径地址：" + result.toString());//录音文件路径地址：content://media/external/audio/media/111
//
//				String filePath = GetPathFromUri4kitkat.getPath(WebviewActivity.this, Uri.parse(result.toString()));
//				Log.w(TAG,"录音文件路径地址：" + filePath);
//
//				setUrlPathInput(webView,"打开录音：" + filePath);//修改网页输入框文本
//			}
//		}else if(resultCode == RESULT_CANCELED){//resultCode == RESULT_CANCELED 解决不选择文件，直接返回后无法再次点击的问题
//			if (webView.getWebChromeClient().getmUploadMessage() != null) {
//				webView.getWebChromeClient().getmUploadMessage().onReceiveValue(null);
//				webView.getWebChromeClient().setmUploadMessage(null);
//			}
//			if (webView.getWebChromeClient().getmUploadCallbackAboveL() != null) {
//				webView.getWebChromeClient().getmUploadCallbackAboveL().onReceiveValue(null);
//				webView.getWebChromeClient().setmUploadCallbackAboveL(null);
//			}
//		}
//	}
//
//	//5.0以上版本，由于api不一样，要单独处理
//	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
//	private void onActivityResultAboveL(int requestCode, Intent data) {
//
//		if (webView.getWebChromeClient().getmUploadCallbackAboveL() == null) {
//			return;
//		}
//		Uri result = null;
//		if (requestCode == WebviewGlobals.CHOOSE_FILE_REQUEST_CODE) {//打开本地文件管理器选择图片
//			result = data == null ? null : data.getData();
//		} else if (requestCode == WebviewGlobals.CAMERA_REQUEST_CODE) {//调用相机拍照
//			File pictureFile = new File(WebViewJSInterface.mCurrentPhotoPath);
//
//			Uri uri = Uri.fromFile(pictureFile);
//			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//			intent.setData(uri);
//			WebviewActivity.this.sendBroadcast(intent);  // 这里我们发送广播让MediaScanner 扫描我们制定的文件
//			//这样在系统的相册中我们就可以找到我们拍摄的照片了【但是这样一来，就会执行MediaScanner服务中onLoadFinished方法，所以需要注意】
//			result = Uri.fromFile(pictureFile);
//		}
//		webView.getWebChromeClient().getmUploadCallbackAboveL().onReceiveValue(new Uri[]{result});//将文件路径返回去，填充到input中
//		webView.getWebChromeClient().setmUploadCallbackAboveL(null);
//	}
//
//
//	//设置网页上的文件路径输入框文本
//	private void setUrlPathInput(MyWebView webView, String urlPath) {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			//这里调用http页面的js代码设置文本
//			webView.evaluateJavascript("setInputText('" + urlPath + "')", new ValueCallback<String>() {
//				@Override
//				public void onReceiveValue(String s) {
//					Log.i(TAG, "onReceiveValue value=" + s);
//
//				}
//			});
//		}else{
//			Toast.makeText(WebviewActivity.this,"当前版本号小于19，无法支持evaluateJavascript，需要使用第三方库JSBridge", Toast.LENGTH_SHORT).show();
//		}
//	}
//	//底部按钮监听
//	private void initBtnListenser() {
//		back_btn = findViewById(R.id.back_btn);
//		forward_btn = findViewById(R.id.forward_btn);
//		more_btn = findViewById(R.id.more_btn);
//		home_btn = findViewById(R.id.home_btn);
//		exit_btn = findViewById(R.id.exit_btn);
//		url_edit = findViewById(R.id.url_edit);
//		go_btn = findViewById(R.id.go_btn);
//		home_btn.setEnabled(false);
//
//		//底部上一页
//		back_btn.setOnClickListener(this);
//		//底部下一页
//		forward_btn.setOnClickListener(this);
//		//底部中间菜单
//		more_btn.setOnClickListener(this);
//		//底部回到主页
//		home_btn.setOnClickListener(this);
//		//底部关闭浏览器
//		exit_btn.setOnClickListener(this);
//		//网址导航右边按钮
//		go_btn.setOnClickListener(this);
//		//网址导航输入框聚焦时
//		url_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//					go_btn.setVisibility(View.VISIBLE);
//					if (null == webView.getUrl())
//						return;
//					if (webView.getUrl().equalsIgnoreCase(mHomeUrl)) {
//						url_edit.setText("");
//						go_btn.setEnabled(false);
//						go_btn.setText("首页");
//						go_btn.setTextColor(0X6F0F0F0F);
//					} else {
//						url_edit.setText(webView.getUrl());
//						go_btn.setText("进入");
//						go_btn.setTextColor(0X6F0000CD);
//					}
//				} else {
//					go_btn.setVisibility(View.GONE);
//					String title = webView.getTitle();
//					if (title != null && title.length() > MAX_LENGTH)
//						url_edit.setText(title.subSequence(0, MAX_LENGTH) + "...");
//					else
//						url_edit.setText(title);
//					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//				}
//			}
//
//		});
//		//网址导航输入框文本改变时
//		url_edit.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void afterTextChanged(Editable s) {
//				String url = null;
//				if (url_edit.getText() != null) {
//					url = url_edit.getText().toString();
//				}
//				if (url == null
//						|| url_edit.getText().toString().equalsIgnoreCase("")) {
//					go_btn.setText("请输入网址");
//					go_btn.setTextColor(0X6F0F0F0F);
//				} else {
//					go_btn.setEnabled(true);
//					go_btn.setText("进入");
//					go_btn.setTextColor(0X6F0000CD);
//				}
//			}
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//										  int arg2, int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//									  int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View v) {
//		int id = v.getId();
//		if(id == R.id.back_btn) {
//			if (webView != null && webView.canGoBack())
//				webView.goBack();
//		} else if(id == R.id.forward_btn) {
//			if (webView != null && webView.canGoForward())
//				webView.goForward();
//		} else if(id == R.id.more_btn) {
//			Toast.makeText(WebviewActivity.this, "未开放",
//					Toast.LENGTH_LONG).show();
//		} else if(id == R.id.go_btn) {
//			String url = url_edit.getText().toString();
//			webView.loadUrl(url);
//			webView.requestFocus();
//		} else if(id == R.id.home_btn) {
//			if (webView != null)
//				webView.loadUrl(mHomeUrl);
//		} else if(id == R.id.exit_btn) {
//			//Process.killProcess(Process.myPid());
//			onBackPressed();
//		}
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		//监听返回事件
//		if(keyCode == KeyEvent.KEYCODE_BACK) {
//			if(webView.canGoBack()) {
//				webView.goBack();
//			} else{
//				onBackPressed();
//			}
//		}
//		return true;
//	}
//}
