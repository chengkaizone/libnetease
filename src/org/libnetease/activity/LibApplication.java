package org.libnetease.activity;

/**
 * 大家好,我叫甘成凯,英文名lance,来自四川,很高兴和大家一起研究Android应用开发,
 * 能写出这个程序demo,首先要感谢slidingmenu团队,PullToRefresh团队的无私分享
 * 以及在我职业生涯中帮助过我的人
 * 希望有机会能和大家一起交流 
 * 我的博客地址:http://blog.csdn.net/chengkaizone
 * 当然也可以给我邮件(^_^):1094226429@qq.com
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
 * 应用类
 * @author lance
 *
 */
public class LibApplication extends Application{
	
	/** 网络状态 */
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

	/** 执行线程 */
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
	
	
	/** 获取应用版本 */
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
	/** 获取版本名 */
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

	/** 获取本机地址 */
	public String getLocalMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo inf = wifi.getConnectionInfo();
		return inf.getMacAddress();
	}
	/** 获取本机IP */
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
