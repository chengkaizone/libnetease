package org.libnetease.fragment;

import java.util.ArrayList;
import java.util.List;

import org.libnetease.activity.R;
import org.libnetease.adapter.AdvAdapter;
import org.libnetease.adapter.NewsListAdapter;
import org.libnetease.entity.NewsInf;
import org.libnetease.widget.AdvViewPager;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 新闻头条
 * 
 * @author lance
 * 
 */
public class HeadLinesFragment extends Fragment {

	private PullToRefreshListView pullRefresh = null;
	private NewsListAdapter newAdapter = null;

	private AdvViewPager vpAdv = null;
	private ViewGroup vg = null;
	private ImageView[] imageViews = null;
	private List<View> advs = null;
	private int currentPage = 0;

	private FragmentActivity fragContext;

	public HeadLinesFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}

	private void initViews() {
		View parent = getView();

		pullRefresh = (PullToRefreshListView) parent
				.findViewById(R.id.ptrlv_headline);
		newAdapter = new NewsListAdapter(this.getActivity(),
				getSimulationNews(10));
		initPullToRefreshListView(pullRefresh, newAdapter);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fragContext = (FragmentActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_head_lines, null);
	}

	/**
	 * 初始化PullToRefreshListView 初始化在PullToRefreshListView中的ViewPager广告栏
	 * 
	 * @param rtflv
	 * @param adapter
	 */
	public void initPullToRefreshListView(PullToRefreshListView rtflv,
			NewsListAdapter adapter) {
		rtflv.setMode(Mode.BOTH);
		rtflv.setOnRefreshListener(new MyOnRefreshListener2(rtflv));
		rtflv.setAdapter(adapter);

		RelativeLayout rlAdv = (RelativeLayout) LayoutInflater.from(
				this.getActivity()).inflate(R.layout.sliding_advertisement,
				null);
		vpAdv = (AdvViewPager) rlAdv.findViewById(R.id.vpAdv);
		vg = (ViewGroup) rlAdv.findViewById(R.id.viewGroup);

		advs = new ArrayList<View>();
		ImageView iv;
		iv = new ImageView(this.getActivity());
		iv.setBackgroundResource(R.drawable.img_0);
		advs.add(iv);

		iv = new ImageView(this.getActivity());
		iv.setBackgroundResource(R.drawable.img_1);
		advs.add(iv);

		iv = new ImageView(this.getActivity());
		iv.setBackgroundResource(R.drawable.img_2);
		advs.add(iv);

		iv = new ImageView(this.getActivity());
		iv.setBackgroundResource(R.drawable.img_3);
		advs.add(iv);

		vpAdv.setAdapter(new AdvAdapter(advs));
		vpAdv.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentPage = arg0;
				for (int i = 0; i < advs.size(); i++) {
					if (i == arg0) {
						imageViews[i]
								.setBackgroundResource(R.drawable.banner_dian_focus);
					} else {
						imageViews[i]
								.setBackgroundResource(R.drawable.banner_dian_blur);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		imageViews = new ImageView[advs.size()];
		ImageView imageView;
		for (int i = 0; i < advs.size(); i++) {
			imageView = new ImageView(this.getActivity());
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageViews[i] = imageView;
			if (i == 0) {
				imageViews[i]
						.setBackgroundResource(R.drawable.banner_dian_focus);
			} else {
				imageViews[i]
						.setBackgroundResource(R.drawable.banner_dian_blur);
			}
			vg.addView(imageViews[i]);
		}

		rtflv.getRefreshableView().addHeaderView(rlAdv, null, false);

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				vpAdv.setCurrentItem(msg.what);
				super.handleMessage(msg);
			}
		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(5000);
						currentPage++;
						if (currentPage > advs.size() - 1) {
							currentPage = 0;
						}
						handler.sendEmptyMessage(currentPage);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/** 模拟获取的数据 */
	public ArrayList<NewsInf> getSimulationNews(int count) {
		ArrayList<NewsInf> tmpData = new ArrayList<NewsInf>();
		NewsInf inf = null;
		for (int i = 0; i < count; i++) {
			inf = new NewsInf();
			if (i % 2 == 0) {
				inf.setPreview("http://b79.photo.store.qq.com/psu?/10fc7dde-f511-46ec-a80e-d3ef9ab4a38d/CXri.QrQlZkENCXdZ1cagrThdIk3.pF4YJTUrJ7vYUI!/b/YasFGC90kwAAYps9Jy9pkwAA&bo=ngL2AQAAAAABAEw!&rf=viewer_4");
			} else {
				inf.setPreview("http://b79.photo.store.qq.com/psu?/10fc7dde-f511-46ec-a80e-d3ef9ab4a38d/XQdTINSox55cX0v7eZx96TQzBOVJpo1i4GhfGVlqtik!/b/YVjWKy8ClgAAYmS3JS9ilwAA&bo=ngL2AQAAAAABAEw!&rf=viewer_4");
			}
			inf.setTitle("李克强以水煮牛肉款待默克尔");
			inf.setContent("默克尔已离成都抵京,因其在成都学做川菜,故晚宴增加川菜.");
			inf.setReview(i + "跟帖");
			tmpData.add(inf);
		}
		return tmpData;
	}

	class MyOnRefreshListener2 implements OnRefreshListener2<ListView> {

		private PullToRefreshListView mPtflv;

		public MyOnRefreshListener2(PullToRefreshListView ptflv) {
			this.mPtflv = ptflv;
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 下拉刷新
			String label = DateUtils.formatDateTime(
					fragContext.getApplicationContext(),
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);

			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			new GetNewsTask(mPtflv).execute();

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 上拉加载
			new GetNewsTask(mPtflv).execute();
		}

	}

	/**
	 * 请求网络获得新闻信息
	 * 
	 * @author Louis
	 * 
	 */
	private class GetNewsTask extends AsyncTask<String, Void, Boolean> {

		private PullToRefreshListView mPtrlv;

		public GetNewsTask(PullToRefreshListView ptrlv) {
			this.mPtrlv = ptrlv;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				Thread.sleep(1000);
				return true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				newAdapter.addNews(getSimulationNews(10));
			} else {
				// showHint("请检查网络!");
			}
			mPtrlv.onRefreshComplete();
		}

	}

}
