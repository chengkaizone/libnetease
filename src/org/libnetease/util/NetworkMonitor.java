package org.libnetease.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * �������--��ȡ����״̬
 * @author lance
 *
 */
public class NetworkMonitor {
	final static String TAG="NetworkMonitor";
	public static final int NET_STATUS_NONE=-1;
	public static final int NET_STATUS_WIFI=0;
	public static final int NET_STATUS_UNKNOW=1;
	
	/**
	 * ��ȡ����״̬
	 * @param context
	 * @return -1û������ 0
	 */
	public static int netStatus(Context context){
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInf = manager.getActiveNetworkInfo();
		int status=-1;
		if(netInf==null||!netInf.isAvailable()){
			status=NET_STATUS_NONE;
			Log.i(TAG, "����û������");
		}else{
			switch (netInf.getType()) {
			case ConnectivityManager.TYPE_WIFI:
				status=NET_STATUS_WIFI;
				break;
			case ConnectivityManager.TYPE_MOBILE:
				status=NET_STATUS_UNKNOW;
				break;
			default:
				break;
			}
			Log.i(TAG, "������--->"+netInf.getTypeName());
		}
		return status;
	}
}
