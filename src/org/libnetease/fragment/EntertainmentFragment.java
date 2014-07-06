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
 * ����ģ��
 * @author lance
 *
 */
public class EntertainmentFragment extends Fragment {
	
	private PullToRefreshListView pullRefresh = null;
	private NewsListAdapter newAdapter = null;
	
	private FragmentActivity fragContext;
	public EntertainmentFragment(){
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}
	
	private void initViews(){
		View parent=getView();
		
		pullRefresh = (PullToRefreshListView) parent
				.findViewById(R.id.ptrlv_entertainment);
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
		return inflater.inflate(R.layout.fragment_entertainment, null);
	}
	
	/** ģ���ȡ������ */
	public ArrayList<NewsInf> getSimulationNews(int count) {
		ArrayList<NewsInf> tmpData = new ArrayList<NewsInf>();
		NewsInf inf = null;
		for (int i = 0; i < count; i++) {
			inf = new NewsInf();
			if (i % 2 == 0) {
				inf.setPreview("http://b84.photo.store.qq.com/psb?/bfcb5f94-6868-4330-97a6-ca67612a02a2/BTKo8b1GfhHrwz*R1Wo3oFUqiMdK639m4eAppqGfV8g!/b/YYFzFDKNhQAAYuShHTKLhwAA&bo=NgG3AAAAAAABAKc!&rf=viewer_4");
			} else {
				inf.setPreview("http://b89.photo.store.qq.com/psb?/bfcb5f94-6868-4330-97a6-ca67612a02a2/2bV89e0PTPrq8GYBhSg5OdAAitbJQ1RENiu1z.2b4bs!/b/YeVzEjW6OAAAYp2XGzWDNwAA&bo=yAC2AAAAAAABAFk!&rf=viewer_4");
			}
			inf.setTitle("��Т���ѳ��� ��ҪС�����Ů��");
			inf.setContent("��������Ѷ ǰF4��Ա��Т�죬ȥ��׵͵���̩����ȳ��ң����Ծ����Ĳ�����״̬���ԡ�����·��Ž�����.");
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
