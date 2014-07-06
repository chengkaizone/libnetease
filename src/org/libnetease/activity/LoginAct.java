package org.libnetease.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * ¼òµ¥µÇÂ¼Ò³Ãæ
 * @author lance
 *
 */
public class LoginAct extends BaseAct implements OnClickListener {
	
	private Button actionBar;
	
	protected void onCreate(Bundle saved){
		super.onCreate(saved);
		setContentView(R.layout.act_login);
		actionBar=(Button)findViewById(R.id.topbar_actionbar);
		
		actionBar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}

}
