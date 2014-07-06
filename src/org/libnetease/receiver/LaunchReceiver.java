package org.libnetease.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 实现开机自启；监听开机事件
 * @author lance
 * 
 */
public class LaunchReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent paramIntent) {
		Log.i("LaunchReceiver","开机启动!自定义需要打开的页面");
//		Intent intent = new Intent(context, StartAct.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(intent);
	}
}