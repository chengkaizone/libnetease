package org.libnetease.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

/**
 * 公共父类
 * @author lance
 *
 */
public class BaseAct extends FragmentActivity {

	/** 屏幕的宽度和高度 */
	protected int mScreenWidth;
	protected int mScreenHeight;

	protected void onCreate(Bundle saved) {
		if (BuildConfig.DEBUG) {// 启用深度检测代码质量
			//DebugUtil.enableStrictMode(this);
		}
		super.onCreate(saved);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	protected void setBarTitle(int titleId) {
		try {// 防止没有bar
			TextView titleText = ((TextView) findViewById(titleId));
			String title = this.getTitle().toString().trim();
			titleText.setText(title);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getNetStatus() {
		return ((LibApplication) getApplication()).getNetStatus();
	}

	public void execTask(Thread run) {
		getLibApplication().execTask(run);
	}

	public void showHint(String str) {
		getLibApplication().showHint(str);
	}

	public void showHintLong(String str) {
		getLibApplication().showHintLong(str);
	}

	public void showHint(int strId) {
		getLibApplication().showHint(strId);
	}

	public void showHintLong(int strId) {
		getLibApplication().showHintLong(strId);
	}
	
	public LibApplication getLibApplication() {
		return (LibApplication) getApplication();
	}
	
	/** 获取应用版本号 */
	public int getVersionCode() {
		return ((LibApplication) getApplication()).getVersionCode();
	}

	/** 获取版本名 */
	public String getVersionName() {
		return ((LibApplication) getApplication()).getVersionName();
	}

	/** 发送通知给系统 点击通知跳转到指定的界面 */
	public void sendNotifyInfo(int notifyId, int resIcon, String title,
			String content, Class<? extends Activity> clazz) {
		NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notify = new Notification();
		notify.icon = resIcon;
		notify.defaults = Notification.DEFAULT_SOUND;
		Intent intent = new Intent(this, clazz);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
		notify.setLatestEventInfo(this, title, content, pi);
		notifyManager.notify(notifyId, notify);
	}

	/** 退出界面 这里必须是公有方法 */
	public void back(View view) {
		finish();
	}

}