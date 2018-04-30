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
 * Created by HYH on 2018/4/14.
 */

public class Hook implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.zjwh.android_wh_physicalfitness")){
            return;
        }
        //final Class clazz1 = lpparam.classLoader.loadClass("com.zjwh.android_wh_physicalfitness.application.MyApplication");
//        final Class clazz2 = lpparam.classLoader.loadClass("cn.jiguang.a.a.c.k");//InstalledApplication
//        final Class clazz3 = lpparam.classLoader.loadClass("cn.jiguang.a.a.c.d");

        XposedBridge.log("万恶之源com.zjwh.android_wh_physicalfitness被捕获");

        /*
        * 获取应用上下文,方便对PackageManager的hook
        * */


//        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "getInstalledPackages", int.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                List<PackageInfo> list = (List<PackageInfo>) param.getResult();
//                Iterator<PackageInfo> iterator = list.iterator();
//                while (iterator.hasNext()){
//                    String pkgname = ((PackageInfo) iterator.next()).packageName;
//                    if (pkgname != null && pkgname.toLowerCase().contains("xposed")){
//                        iterator.remove();
//                    }
//                }
//            }
//        });
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
//        XposedHelpers.findAndHookMethod(clazz3, "a", Context.class, boolean.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                XposedHelpers.findAndHookMethod(((Context) param.args[0]).getPackageManager().getClass(), "getInstalledPackages", int.class, new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        List<PackageInfo> list = (List<PackageInfo>) param.getResult();
//                        Iterator<PackageInfo> iterator = list.iterator();
//                        while (iterator.hasNext()){
//                            String pkgname = ((PackageInfo) iterator.next()).packageName;
//                            if (pkgname != null && pkgname.toLowerCase().contains("xposed")){
//                                iterator.remove();
//                            }
//                        }
//                    }
//                });
//            }
//        });
//        XposedHelpers.findAndHookMethod(clazz2, "a", ActivityManager.class, PackageManager.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                XposedHelpers.findAndHookMethod(param.args[1].getClass(), "getInstalledApplications", int.class, new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        List<ApplicationInfo> list = (List<ApplicationInfo>) param.getResult();
//                        Iterator<ApplicationInfo> iterator = list.iterator();
//                        while (iterator.hasNext()) {
//                            String pkgname = ((ApplicationInfo) iterator.next()).packageName;
//                            if (pkgname != null && pkgname.toLowerCase().contains("xposed")) {
//                                iterator.remove();
//                            }
//                        }
//                    }
//                });
//            }
//        });
//        /*
//        * 防止检测包名
//        * */
//        XposedHelpers.findAndHookMethod(JSONObject.class, "getString", String.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                if (param.getResult() == null)
//                    return;
//                if (param.getResult().toString().toLowerCase().contains("xposed")){
//                    XposedBridge.log("####remove pkg " + param.getResult().toString());
//                    param.setResult(null);
//                }
//            }
//        });
        /*
        * 防止检测正在运行的进程
        * */
        XposedHelpers.findAndHookMethod("android.app.ActivityManager", XposedBridge.BOOTCLASSLOADER, "getRunningAppProcesses", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<ActivityManager.RunningAppProcessInfo> list = (List<ActivityManager.RunningAppProcessInfo>) param.getResult();
                for (int i=0; i<list.size(); ++i){
                    if (list.get(i).processName.toLowerCase().contains("xposed")){
//                        XposedBridge.log("####remove process " + list.get(i).processName.toLowerCase().contains("xposed"));
                        list.remove(i);
                    }
                }

                param.setResult(list);
            }
        });
//        XposedHelpers.findAndHookMethod("android.app.ActivityManager", XposedBridge.BOOTCLASSLOADER, "getRunningServices", int.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                List<ActivityManager.RunningServiceInfo> list = (List<ActivityManager.RunningServiceInfo>) param.getResult();
//                for (int i=0; i<list.size(); ++i){
//                    if (list.get(i).service.getShortClassName().toLowerCase().contains("xposed")){
//                        XposedBridge.log("####remove services " + list.get(i).service.getShortClassName().toLowerCase().contains("xposed"));
//                        list.remove(i);
//                    }
//                }
//
//                param.setResult(list);
//            }
//        });

//        XposedHelpers.findAndHookMethod(clazz3, "a", Context.class, boolean.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//
//                XposedHelpers.findAndHookMethod(Throwable.class, "getStackTrace", new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        StackTraceElement[] stackTraceElements = (StackTraceElement[]) param.getResult();
//                        List arrayList = new ArrayList();
//                        for (StackTraceElement stackTraceElement : stackTraceElements){
//                            if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge") && stackTraceElement.getMethodName().equals("handleHookedMethod")) {
//                            } else {
//                                arrayList.add(stackTraceElement);
//                            }
//                        }
//                        Object obj = new StackTraceElement[arrayList.size()];
//                        arrayList.toArray((Object[]) obj);
//                        param.setResult(obj);
//                    }
//                });
//            }
//        });
        //(stackTraceElement.getClassName().equals("com.saurik.substrate.MS$2") && stackTraceElement.getMethodName().equals("invoked")) ||
        /*
        * 核心部分
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
        });//str.contains("com.saurik.substrate") ||


//        XposedHelpers.findAndHookMethod(clazz2, "a", ActivityManager.class, PackageManager.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                JSONArray jsonArray = (JSONArray) param.getResult();
//                for (int i=0; i<jsonArray.length(); ++i){
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    if (jsonObject.getString("app_name").toLowerCase().contains("xposed") || jsonObject.getString("pkg_name").toLowerCase().contains("xposed")) {
//                        jsonArray.remove(i);
//                    }
//                    JSONObject jsonObject1  = jsonObject.getJSONObject("service_list");
//                    if (jsonObject1 != null && jsonObject1.getString("class_name").toLowerCase().contains("xposed")){
//                        jsonArray.remove(i);
//                    }
//                }
//                XposedBridge.log("####数据清除完毕");
//                param.setResult(jsonArray);
////                XposedHelpers.findAndHookMethod(param.args[1].getClass(), "getInstalledApplications", int.class, new XC_MethodHook() {
////                    @Override
////                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
////                        List<ApplicationInfo> list = (List<ApplicationInfo>) param.getResult();
////                        list.clear();
////                        XposedBridge.log("####应用列表信息已清空");
////                        param.setResult(list);
//////                        XposedHelpers.findAndHookMethod(param.getResult().getClass(), "loadLabel", new XC_MethodHook() {
//////                            @Override
//////                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//////                                if (param.getResult().toString().contains("Xposed")  || param.getResult().toString().contains("xposed"))
//////                                {
//////                                    param.setResult(null);
//////                                }
//////                            }
//////                        });
////                    }
////                });
////                XposedHelpers.findAndHookMethod(param.args[0].getClass(), "getRunningServices", int.class, new XC_MethodHook() {
////                    @Override
////                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
////                        List<ActivityManager.RunningServiceInfo> list = (List<ActivityManager.RunningServiceInfo>) param.getResult();
////                        list.clear();
////                        XposedBridge.log("####服务列表信息已清空");
////                        param.setResult(list);
////                    }
////                });
//            }
//        });


//            XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    if (param.getResult() != null && param.getResult().equals("de.robv.android.xposed.XposedBridge"))
//                    {
//
//                        param.setResult(null);
//                    }
//                }
//            });
    }
}
