package org.libnetease.fragment;

import org.libnetease.activity.MainAct;
import org.libnetease.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 本地新闻
 * @author lance
 *
 */
public class LocalFragment extends Fragment implements OnClickListener {
	private MainAct fragContext;
	
	private TextView content;
	public LocalFragment() {
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
		content=(TextView)parent.findViewById(R.id.local_content);
		content.setText("本地新闻");
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
		return inflater.inflate(R.layout.fragment_local, null);
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
		}

	}

}
