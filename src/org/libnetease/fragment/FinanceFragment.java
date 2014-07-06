package org.libnetease.fragment;

import java.util.ArrayList;

import org.libnetease.activity.R;
import org.libnetease.adapter.NewsListAdapter;
import org.libnetease.entity.NewsInf;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 财经模块
 * @author lance
 *
 */
public class FinanceFragment extends Fragment {
	
	private PullToRefreshListView pullRefresh = null;
	private NewsListAdapter newAdapter = null;
	
	private FragmentActivity fragContext;
	public FinanceFragment(){
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}
	
	private void initViews(){
		View parent=getView();
		
		pullRefresh = (PullToRefreshListView) parent
				.findViewById(R.id.ptrlv_finance);
		newAdapter = new NewsListAdapter(this.getActivity(), getSimulationNews(10));
		pullRefresh.setMode(Mode.BOTH);
		pullRefresh.setOnRefreshListener(new MyOnRefreshListener(pullRefresh));
		pullRefresh.setAdapter(newAdapter);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fragContext=(FragmentActivity)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_finance, null);
	}
	
	/** 模拟获取的数据 */
	public ArrayList<NewsInf> getSimulationNews(int count) {
		ArrayList<NewsInf> tmpData = new ArrayList<NewsInf>();
		NewsInf inf = null;
		for (int i = 0; i < count; i++) {
			inf = new NewsInf();
			if (i % 2 == 0) {
				inf.setPreview("http://b89.photo.store.qq.com/psb?/bfcb5f94-6868-4330-97a6-ca67612a02a2/PiQnb.hahL9IGTPaZv75oGcdJAYbZnTYNb1ZG5AWIAE!/m/Ya3.EzWbHgAAYmN7FTVNHgAA&bo=ewLXAQAAAAABAIg!&rf=photolist");
			} else {
				inf.setPreview("http://a2.qpic.cn/psb?/bfcb5f94-6868-4330-97a6-ca67612a02a2/0AE3b4MTKadLyzsncMr6LdPfy2g6dNxswEh.IhydQ6s!/b/dKlY76acEAAA&bo=fQFDAQAAAAABABk!&rf=viewer_4");
			}

			inf.setTitle("成飞集成4年后再涨逾300%");
			inf.setContent("成飞集成，当年的一只不死鸟，在停牌5个月后复牌，再次上演一场席卷沪深两市的狂飙好戏，一副王者归来的气魄，其凌厉攻势，堪称荡气回肠");
			inf.setReview(i + "跟帖");
			tmpData.add(inf);
		}
		return tmpData;
	}
	
	class MyOnRefreshListener implements OnRefreshListener2<ListView> {

		private PullToRefreshListView pullRefresh;

		public MyOnRefreshListener(PullToRefreshListView pullRefresh) {
			this.pullRefresh = pullRefresh;
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 下拉刷新
			String label = DateUtils.formatDateTime(fragContext.getApplicationContext(),
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);

			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			new GetNewsTask(pullRefresh).execute();

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 上拉加载
			new GetNewsTask(pullRefresh).execute();
		}

	}
	
	/**
	 * 请求网络获得新闻信息
	 * 
	 * @author Louis
	 * 
	 */
	private class GetNewsTask extends AsyncTask<String, Void, Boolean> {
		
		private PullToRefreshListView pullRefresh;

		public GetNewsTask(PullToRefreshListView pullRefresh) {
			this.pullRefresh = pullRefresh;
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
				//showHint("请检查网络!");
			}
			pullRefresh.onRefreshComplete();
		}

	}
}
