package org.libnetease.fragment;

import java.util.ArrayList;

import org.libnetease.activity.MainAct;
import org.libnetease.activity.R;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 新闻头条
 * 
 * @author lance
 * 
 */
public class NewsFragment extends Fragment implements OnClickListener {

	private MainAct fragContext;
	private ViewPager mViewPager;
	private ArrayList<Fragment> fragList = new ArrayList<Fragment>();

	private TextView tab0;
	private TextView tab1;
	private TextView tab2;
	

	private int offset; // 间隔
	private int cursorWidth; // 游标的长度
	private int originalIndex = 0;
	private ImageView cursor = null;

	private Animation animation = null;

	public NewsFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}

	private void initViews() {
		View parent = getView();
		parent.findViewById(R.id.topbar_actionbar).setOnClickListener(this);
		parent.findViewById(R.id.topbar_right).setOnClickListener(this);
		mViewPager = (ViewPager) parent.findViewById(R.id.news_viewpager);
		cursor = (ImageView) parent.findViewById(R.id.news_tab_cursor);
		tab0 = ((TextView) parent.findViewById(R.id.news_tab_0));
		tab0.setOnClickListener(this);
		tab1 = ((TextView) parent.findViewById(R.id.news_tab_1));
		tab1.setOnClickListener(this);
		tab2 = ((TextView) parent.findViewById(R.id.news_tab_2));
		tab2.setOnClickListener(this);

		HeadLinesFragment listenFrag = new HeadLinesFragment();
		EntertainmentFragment singleFrag = new EntertainmentFragment();
		FinanceFragment readFrag = new FinanceFragment();

		fragList.add(listenFrag);
		fragList.add(singleFrag);
		fragList.add(readFrag);

		//Fragment嵌套Fragment 必须使用getChildFragmentManager()
		mViewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragList));
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new CursorOnPageChangeListener());

		initCursor(fragList.size());

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fragContext = (MainAct) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_news, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_actionbar:
			fragContext.showMenu();
			break;
		case R.id.topbar_right:
			fragContext.showSecondaryMenu();
			break;
		case R.id.news_tab_0:
			mViewPager.setCurrentItem(0, true);
			break;
		case R.id.news_tab_1:
			mViewPager.setCurrentItem(1, true);
			break;
		case R.id.news_tab_2:
			mViewPager.setCurrentItem(2, true);
			break;
		}

	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> fragmentsList;

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public MyFragmentPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> fragments) {
			super(fm);
			this.fragmentsList = fragments;
		}

		@Override
		public int getCount() {
			return fragmentsList.size();
		}

		@Override
		public Fragment getItem(int loc) {
			return fragmentsList.get(loc);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int loc) {
			mViewPager.setCurrentItem(loc, true);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	// 页面滑动监听,控制游标滑动
	private class CursorOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			// 计算游标X轴到达的距离
			int offsetValue = 2 * offset + cursorWidth;
			animation = new TranslateAnimation(originalIndex * offsetValue,
					position * offsetValue, 0, 0);
			animation.setFillAfter(true);
			animation.setDuration(300);
			cursor.startAnimation(animation);
			originalIndex = position;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

	}

	/**
	 * 根据tagd的数量初始化游标的位置
	 * 
	 * @param tagNum
	 */
	public void initCursor(int tagNum) {
		cursorWidth = BitmapFactory.decodeResource(getResources(),
				R.drawable.cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		fragContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
		offset = ((dm.widthPixels / tagNum) - cursorWidth) / 2;

		Matrix matrix = new Matrix();
		matrix.setTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
	}
}
