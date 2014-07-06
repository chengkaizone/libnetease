package org.libnetease.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * home事件触发器
 * @author lance
 *
 */
public class HomeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("HomeReceiver", "home事件");
	}

}
