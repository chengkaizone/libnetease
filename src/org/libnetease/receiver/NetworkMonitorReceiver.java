package org.libnetease.receiver;

import org.libnetease.util.NetworkMonitor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 网络监听接收器--该类不能实现交互---只能写成内部类实现交互
 * @author lance
 *
 */
public class NetworkMonitorReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent it) {
		int netStatus=NetworkMonitor.netStatus(context);
		System.out.println("net state--->"+netStatus);
	}
	
}
