package org.libnetease.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * ����װ����
 * @author lance
 *
 */
public class PkInstallReceiver extends BroadcastReceiver {
	
	private static final String TAG = "PkInstallReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String packageName = intent.getDataString().substring(8);
			// ��һ�ΰ�װʱִ��--����ֻ�ܼ�⵽����Ӧ�õ��״ΰ�װ---���ܼ�⵽�Լ�
			if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
				Log.i(TAG, "install:" + packageName + " application");
				
			} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {//ж��Ӧ��
				Log.i(TAG, "uninstall:" + packageName + " application");
			} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
				Log.i(TAG, packageName + " replace!");
				//����뵱ǰӦ���޹� ����ǵ�ǰӦ��,������ҳ
//				if (packageName.equals(appPackageName)) {
//					launchTaskActivity(context,packageName,StartAct.TAG);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** �����Զ������ */
	private void launchTaskActivity(Context context,String packageName,String className){
		Intent newIntent = new Intent();
		newIntent.setClassName(packageName, "com.shuocheng.ilexue.moblie." + className);
		newIntent.setAction(Intent.ACTION_MAIN);
		newIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(newIntent);
	}

}
