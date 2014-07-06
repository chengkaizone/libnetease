package org.libnetease.service;

import org.libnetease.activity.LibApplication;
import org.libnetease.util.NetworkMonitor;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;

/**
 * 网络监听服务
 * @author lance
 *
 */
public class NetworkMonitorService extends Service{
    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            	int netStatus=NetworkMonitor.netStatus(context);
            	((LibApplication)getApplication()).setNetStatus(netStatus);
            }
        }
    };
    
	public class MonitorBinder extends Binder{
		//为服务设置通知监听器这里要回调一个事件通知service组件
		public void onNetStatus(int netStatus){
			
		}
	}
}

