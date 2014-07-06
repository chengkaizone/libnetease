package org.libnetease.fragment;

import org.libnetease.activity.LoginAct;
import org.libnetease.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * ”“≤‡≤Àµ•
 * @author lance
 *
 */
public class MenuRightFragment extends Fragment implements OnClickListener {

	private ImageView userHead;
	private Button menuRight;
	
	private FragmentActivity mContext;
	public MenuRightFragment(){
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}
	
	private void initViews(){
		View parent=getView();
		
		userHead=(ImageView)parent.findViewById(R.id.user_head);
		menuRight=(Button)parent.findViewById(R.id.right_login);
		menuRight.setOnClickListener(this);
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext=(FragmentActivity)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.slidingmenu_right_content, null);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.right_login:
			startActivity(new Intent(mContext,LoginAct.class));
			//slidingMenu.toggle();
			break;
		}
		
	}
	
}
