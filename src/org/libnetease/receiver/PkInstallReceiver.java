package org.libnetease.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 程序安装监听
 * @author lance
 *
 */
public class PkInstallReceiver extends BroadcastReceiver {
	
	private static final String TAG = "PkInstallReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String packageName = intent.getDataString().substring(8);
			// 第一次安装时执行--这里只能检测到其他应用的首次安装---不能检测到自己
			if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
				Log.i(TAG, "install:" + packageName + " application");
				
			} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {//卸载应用
				Log.i(TAG, "uninstall:" + packageName + " application");
			} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
				Log.i(TAG, packageName + " replace!");
				//如果与当前应用无关 如果是当前应用,启动首页
//				if (packageName.equals(appPackageName)) {
//					launchTaskActivity(context,packageName,StartAct.TAG);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 启动自定义界面 */
	private void launchTaskActivity(Context context,String packageName,String className){
		Intent newIntent = new Intent();
		newIntent.setClassName(packageName, "com.shuocheng.ilexue.moblie." + className);
		newIntent.setAction(Intent.ACTION_MAIN);
		newIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(newIntent);
	}

}
