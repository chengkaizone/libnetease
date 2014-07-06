package org.libnetease.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.libnetease.fragment.LocalFragment;
import org.libnetease.fragment.MenuLeftFragment;
import org.libnetease.fragment.MenuRightFragment;
import org.libnetease.fragment.NewsFragment;
import org.libnetease.fragment.PicsFragment;
import org.libnetease.fragment.ReaderFragment;
import org.libnetease.fragment.TiesFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 主界面
 * 
 * @author lance
 * 
 */
public class MainAct extends BaseAct {

	private SlidingMenu slidingMenu = null;

	private Timer timer = null;
	private TimerTask timeTask = null;
	private boolean isExit = false; // 标记是否要退出

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);

		configSlidemenu();
		timer = new Timer();

	}

	// 配置滑动菜单
	private void configSlidemenu() {
		// 设置抽屉菜单
		slidingMenu = new SlidingMenu(this);
		// 将抽屉菜单与主页面关联起来SLIDING_CONTENT不会包含ActionBar,SLIDING_WINDOW
		// 必须在设置Content之前设置否则会报错
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// 设置菜单 左/右风格
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		// 触摸边界拖出菜单
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// 设置菜单留下的偏移量
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		// 设置主要显示内容
		slidingMenu.setContent(R.layout.slidingmenu_content);

		// 默认菜单
		slidingMenu.setMenu(R.layout.slidingmenu_left);
		slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
		// 设置次级菜单
		slidingMenu.setSecondaryMenu(R.layout.slidingmenu_right);
		slidingMenu
				.setSecondaryShadowDrawable(R.drawable.slidingmenu_shadow_secondary);
		slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setFadeEnabled(true);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.slidingmenu_content, new NewsFragment());
		transaction.replace(R.id.slidingmenu_left, new MenuLeftFragment());
		transaction.replace(R.id.slidingmenu_right, new MenuRightFragment());

		transaction.commit();
	}

	@Override
	// 返回键回调
	public void onBackPressed() {
		if (isExit) {
			finish();
		} else {
			isExit = true;
			showHint("再按一次退出应用");
			timeTask = new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			};
			timer.schedule(timeTask, 2000);
		}
	}

	public void showMenu() {
		slidingMenu.showMenu();
	}

	public void showSecondaryMenu() {
		slidingMenu.showSecondaryMenu();
	}


	public void toggle(int menuItem) {
		switch (menuItem) {
		case 0: {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.slidingmenu_content, new NewsFragment())
					.commit();
		}
			break;
		case 1:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.slidingmenu_content, new ReaderFragment())
					.commit();
			break;
		case 2:
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.slidingmenu_content, new LocalFragment())
			.commit();
			break;
		case 3:
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.slidingmenu_content, new TiesFragment())
			.commit();
			break;
		case 4:
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.slidingmenu_content, new PicsFragment())
			.commit();
			break;
		default:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.slidingmenu_content, new NewsFragment())
					.commit();
			break;
		}
		slidingMenu.toggle();
	}

}
