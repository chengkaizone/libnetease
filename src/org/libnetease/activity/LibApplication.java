package org.libnetease.activity;

/**
 * ��Һ�,�ҽиʳɿ�,Ӣ����lance,�����Ĵ�,�ܸ��˺ʹ��һ���о�AndroidӦ�ÿ���,
 * ��д���������demo,����Ҫ��лslidingmenu�Ŷ�,PullToRefresh�Ŷӵ���˽����
 * �Լ�����ְҵ�����а������ҵ���
 * ϣ���л����ܺʹ��һ���� 
 * �ҵĲ��͵�ַ:http://blog.csdn.net/chengkaizone
 * ��ȻҲ���Ը����ʼ�(^_^):1094226429@qq.com
 */

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ThreadPoolExecutor;

import org.libnetease.service.NetworkMonitorService;
import org.libnetease.util.DebugUtil;
import org.libnetease.util.NetUtils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Ӧ����
 * @author lance
 *
 */
public class LibApplication extends Application{
	
	/** ����״̬ */
	public int netStatus;
	
	private static ThreadPoolExecutor threadExecutor;
	
	static {
		threadExecutor = NetUtils.createThreadPoolExecutor(5, 10, 30L);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		DebugUtil.enableStrictMode(this);
		Intent monitorService=new Intent(this,NetworkMonitorService.class);  
		startService(monitorService); 
	}

	public int getNetStatus() {
		return netStatus;
	}

	public void setNetStatus(int netStatus) {
		this.netStatus = netStatus;
	}

	/** ִ���߳� */
	public void execTask(Thread run) {
		threadExecutor.execute(run);
	}

	private static Toast shortToast;
	private static Toast longToast;

	public void showHint(String str) {
		if (shortToast == null) {
			shortToast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		} else {
			shortToast.setText(str);
		}
		shortToast.show();
	}

	public void showHintLong(String str) {
		if (longToast == null) {
			longToast = Toast.makeText(this, str, Toast.LENGTH_LONG);
		} else {
			longToast.setText(str);
		}
		longToast.show();
	}

	public void showHint(int strId) {
		if (shortToast == null) {
			shortToast = Toast.makeText(this, strId, Toast.LENGTH_SHORT);
		} else {
			shortToast.setText(strId);
		}
		shortToast.show();
	}

	public void showHintLong(int strId) {
		if (longToast == null) {
			longToast = Toast.makeText(this, strId, Toast.LENGTH_LONG);
		} else {
			longToast.setText(strId);
		}
		longToast.show();
	}
	
	
	/** ��ȡӦ�ð汾 */
	public int getVersionCode() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}
	/** ��ȡ�汾�� */
	public String getVersionName() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "1.0";
	}

	/** ��ȡ������ַ */
	public String getLocalMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo inf = wifi.getConnectionInfo();
		return inf.getMacAddress();
	}
	/** ��ȡ����IP */
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

}
