package org.libnetease.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * ʵ�ֿ������������������¼�
 * @author lance
 * 
 */
public class LaunchReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent paramIntent) {
		Log.i("LaunchReceiver","��������!�Զ�����Ҫ�򿪵�ҳ��");
//		Intent intent = new Intent(context, StartAct.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(intent);
	}
}