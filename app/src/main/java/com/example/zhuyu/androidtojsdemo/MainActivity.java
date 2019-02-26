package com.example.zhuyu.androidtojsdemo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
private WebView mWebView;


//private String url="http://www.baidu.com/";
    private String url="file:///android_asset/javascript.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


   private void  initView(){
       mWebView=findViewById(R.id.webview);
       WebSettings webSettings = mWebView.getSettings();

       // 设置与Js交互的权限
       webSettings.setJavaScriptEnabled(true);

       // 通过addJavascriptInterface()将Java对象映射到JS对象
       //参数1：Javascript对象名
       //参数2：Java对象名
       mWebView.addJavascriptInterface(new AndroidToJs(), "test");//AndroidtoJS类对象映射到js的test对象

       // 加载JS代码
       // 格式规定为:file:///android_asset/文件名.html
      // mWebView.loadUrl("file:///android_asset/javascript.html");
       mWebView.loadUrl(url);
       //设置不用系统浏览器打开,直接显示在当前Webview
       mWebView.setWebViewClient(new WebViewClient() {
           @Override
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
               view.loadUrl(url);
               return true;
           }
       });
       // 由于设置了弹窗检验调用结果,所以需要支持js对话框
       // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
       // 通过设置WebChromeClient对象处理JavaScript的对话框
       //设置响应js 的Alert()函数
       mWebView.setWebChromeClient(new WebChromeClient() {
           @Override
           public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
               AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
               b.setTitle("Alert");
               b.setMessage(message);
               b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       result.confirm();
                   }
               });
               b.setCancelable(false);
               b.create().show();
               return true;
           }

       });

   }



    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

}
