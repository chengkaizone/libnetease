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
 * �ƾ�ģ��
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
	
	/** ģ���ȡ������ */
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

			inf.setTitle("�ɷɼ���4���������300%");
			inf.setContent("�ɷɼ��ɣ������һֻ��������ͣ��5���º��ƣ��ٴ�����һ��ϯ�������еĿ�쭺�Ϸ��һ�����߹��������ǣ����������ƣ����Ƶ����س�");
			inf.setReview(i + "����");
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
			// ����ˢ��
			String label = DateUtils.formatDateTime(fragContext.getApplicationContext(),
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);

			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			new GetNewsTask(pullRefresh).execute();

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// ��������
			new GetNewsTask(pullRefresh).execute();
		}

	}
	
	/**
	 * ����������������Ϣ
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
				//showHint("��������!");
			}
			pullRefresh.onRefreshComplete();
		}

	}
}
