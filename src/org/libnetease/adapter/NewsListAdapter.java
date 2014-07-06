package org.libnetease.adapter;

import java.util.ArrayList;

import org.libnetease.activity.R;
import org.libnetease.entity.NewsInf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 新闻列表适配器
 * @author lance
 *
 */
public class NewsListAdapter extends BaseAdapter {
	
	private ImageLoader imageLoader = null;
	private	DisplayImageOptions options = null;
	
	private Context context;
	private ArrayList<NewsInf> newsInfs;

	public NewsListAdapter(Context context,ArrayList<NewsInf> newsInfs) {
		this.context = context;
		this.newsInfs = newsInfs;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		
		options = new DisplayImageOptions.Builder()
		.displayer(new RoundedBitmapDisplayer(0xff000000, 0))
		.cacheInMemory().cacheOnDisc().build();
	}
	
	@Override
	public int getCount() {
		return newsInfs.size();
	}

	@Override
	public NewsInf getItem(int position) {
		return newsInfs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_news, null);
			holder = new ViewHolder();
			holder.preview = (ImageView) convertView.findViewById(R.id.adapter_news_preview);
			holder.title = (TextView) convertView.findViewById(R.id.adapter_news_title);
			holder.content = (TextView) convertView.findViewById(R.id.adapter_news_content);
			holder.review = (TextView) convertView.findViewById(R.id.adapter_news_review);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		NewsInf inf=newsInfs.get(position);
		imageLoader.displayImage(inf.getPreview(), holder.preview, options);
		holder.title.setText(inf.getTitle());
		holder.content.setText(inf.getContent());
		holder.review.setText(inf.getReview());
		
		return convertView;
	}
	
	/** 添加新闻数据到列表中 */
	public synchronized void addNews(ArrayList<NewsInf> addNews) {
		for(NewsInf tmp:addNews) {
			newsInfs.add(tmp);
		}
		this.notifyDataSetChanged();
	}
	
	static class ViewHolder {
		//新闻预览
		ImageView preview;
		//标题
		TextView title;
		//简介
		TextView content;
		//回复
		TextView review;
	}

}
