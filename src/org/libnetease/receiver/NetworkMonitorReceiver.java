package org.libnetease.receiver;

import org.libnetease.util.NetworkMonitor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * �������������--���಻��ʵ�ֽ���---ֻ��д���ڲ���ʵ�ֽ���
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
