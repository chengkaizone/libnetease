package org.libnetease.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ¹ã¸æÌõ
 * @author lance
 *
 */
public class AdvViewPager extends ViewPager {

	public AdvViewPager(Context context) {
		super(context);
	}	
	
	public AdvViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(arg0);
	}
	
}
