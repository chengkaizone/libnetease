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
 * ������
 * 
 * @author lance
 * 
 */
public class MainAct extends BaseAct {

	private SlidingMenu slidingMenu = null;

	private Timer timer = null;
	private TimerTask timeTask = null;
	private boolean isExit = false; // ����Ƿ�Ҫ�˳�

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);

		configSlidemenu();
		timer = new Timer();

	}

	// ���û����˵�
	private void configSlidemenu() {
		// ���ó���˵�
		slidingMenu = new SlidingMenu(this);
		// ������˵�����ҳ���������SLIDING_CONTENT�������ActionBar,SLIDING_WINDOW
		// ����������Content֮ǰ���÷���ᱨ��
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// ���ò˵� ��/�ҷ��
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		// �����߽��ϳ��˵�
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// ���ò˵����µ�ƫ����
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		// ������Ҫ��ʾ����
		slidingMenu.setContent(R.layout.slidingmenu_content);

		// Ĭ�ϲ˵�
		slidingMenu.setMenu(R.layout.slidingmenu_left);
		slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
		// ���ôμ��˵�
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
	// ���ؼ��ص�
	public void onBackPressed() {
		if (isExit) {
			finish();
		} else {
			isExit = true;
			showHint("�ٰ�һ���˳�Ӧ��");
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
