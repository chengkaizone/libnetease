package org.libnetease.fragment;

import org.libnetease.activity.MainAct;
import org.libnetease.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * ×ó²à²Ëµ¥ËéÆ¬
 * @author lance
 *
 */
public class MenuLeftFragment extends Fragment implements OnClickListener {

	private LinearLayout menuNews;
	private LinearLayout menuRead;
	private LinearLayout menuLocal;
	private LinearLayout menuTies;
	private LinearLayout menuPics;
	private MainAct fragContext;
	
	public MenuLeftFragment(){
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}
	
	private void initViews(){
		View parent=getView();
		
		menuNews=(LinearLayout)parent.findViewById(R.id.menu_news);
		menuRead=(LinearLayout)parent.findViewById(R.id.menu_read);
		menuLocal=(LinearLayout)parent.findViewById(R.id.menu_local_news);
		menuTies=(LinearLayout)parent.findViewById(R.id.menu_ties);
		menuPics=(LinearLayout)parent.findViewById(R.id.menu_pics);
		
		menuNews.setOnClickListener(this);
		menuRead.setOnClickListener(this);
		menuLocal.setOnClickListener(this);
		menuTies.setOnClickListener(this);
		menuPics.setOnClickListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fragContext=(MainAct)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.slidingmenu_left_content, null);
	}

	@Override
	public void onClick(View v) {
		menuNews.setSelected(false);
		menuRead.setSelected(false);
		menuLocal.setSelected(false);
		menuTies.setSelected(false);
		menuPics.setSelected(false);
		switch(v.getId()){
		case R.id.menu_news:
			menuNews.setSelected(true);
			fragContext.toggle(0);
			break;
		case R.id.menu_read:
			menuRead.setSelected(true);
			fragContext.toggle(1);
			break;
		case R.id.menu_local_news:
			menuLocal.setSelected(true);
			fragContext.toggle(2);
			break;
		case R.id.menu_ties:
			menuTies.setSelected(true);
			fragContext.toggle(3);
			break;
		case R.id.menu_pics:
			menuPics.setSelected(true);
			fragContext.toggle(4);
			break;
		}
	}
	
}
