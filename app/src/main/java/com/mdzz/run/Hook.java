package com.mdzz.run;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by TinyHai on 2018/4/14.
 */

public class Hook implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.zjwh.android_wh_physicalfitness")){
            return;
        }
        XposedBridge.log("万恶之源com.zjwh.android_wh_physicalfitness被捕获");

        /*
        * 防止检测已安装的xp管理
        * */
        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "getInstalledApplications", int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    List<ApplicationInfo> list = (List<ApplicationInfo>) param.getResult();
                    Iterator<ApplicationInfo> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        String pkgname = ((ApplicationInfo) iterator.next()).packageName;
                        if (pkgname != null && pkgname.toLowerCase().contains("xposed")) {
                            iterator.remove();
                        }
                    }
                }
        });

        /*
        * 防止检测正在运行的进程
        * */
        XposedHelpers.findAndHookMethod("android.app.ActivityManager", XposedBridge.BOOTCLASSLOADER, "getRunningAppProcesses", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<ActivityManager.RunningAppProcessInfo> list = (List<ActivityManager.RunningAppProcessInfo>) param.getResult();
                for (int i=0; i<list.size(); ++i){
                    if (list.get(i).processName.toLowerCase().contains("xposed")){
                        list.remove(i);
                    }
                }

                param.setResult(list);
            }
        });

        /*
        * 防止它通过读取栈内数据检测xp是否开启
        * */
        XposedHelpers.findAndHookMethod(BufferedReader.class, "readLine", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String str = (String) param.getResult();
                if (str == null) {
                    return;
                }
                if (str.toLowerCase().contains("xposed")) {
                    param.setResult(null);
                }
            }
        });
    }
}
