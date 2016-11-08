package com.wuwang.utils;

import android.util.Log;

import java.util.Map;

/**
 * Description:
 */
public class LogUtils {

    public static boolean DEBUG = true;
    public static String TAG="LOG";

    public static void setDebug(boolean isDebug) {
        DEBUG = isDebug;
    }

    public static void d(String msg){
        if(DEBUG)
            Log.d(TAG,msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static void d(String tag, String msg,Throwable throwable) {
        if (DEBUG)
            Log.d(tag, msg,throwable);
    }

    public static void i(String msg){
        if(DEBUG)
            Log.i(TAG,msg);
    }

    public static void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG)
            Log.w(tag, msg);
    }

    public static void w(String msg){
        if(DEBUG)
            Log.w(TAG,msg);
    }

    public static void w(String tag, String msg,Throwable throwable) {
        if (DEBUG)
            Log.w(tag, msg,throwable);
    }

    public static void e(String msg){
        if(DEBUG)
            Log.e(TAG,msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static void e(String tag, String msg,Throwable throwable) {
        if (DEBUG)
            Log.e(tag, msg,throwable);
    }

    public static void v(String msg){
        if(DEBUG)
            Log.v(TAG,msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG)
            Log.v(tag, msg);
    }

    public static void v(String tag, String msg,Throwable throwable) {
        if (DEBUG)
            Log.e(tag, msg,throwable);
    }

    public static <T,B> void eLogMap(String tag,Map<T,B> map){
        if(DEBUG){
            for (Map.Entry entry:map.entrySet()) {
                if(entry!=null){
                    Log.e(tag,entry.getKey()+":"+entry.getValue());
                }
            }
        }
    }

}

