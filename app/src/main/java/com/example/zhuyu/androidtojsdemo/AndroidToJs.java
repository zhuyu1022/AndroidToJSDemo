package com.example.zhuyu.androidtojsdemo;

import android.util.Log;
import android.webkit.JavascriptInterface;


public class AndroidToJs extends Object {


    @JavascriptInterface
    public void hello(String msg){
        System.out.println("JS调用了android的方法");
        Log.d("AndroidToJs", "JS调用了android的方法:hello() ");

    }
}
