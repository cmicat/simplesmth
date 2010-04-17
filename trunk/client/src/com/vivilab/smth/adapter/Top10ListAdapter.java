package com.vivilab.smth.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vivilab.smth.model.TopTenPost;
import com.vivilab.smth.view.Top10ItemView;

public class Top10ListAdapter extends BaseAdapter{
	private static final String TAG = "ArticleListAdapter";
	private Context context;
	private List<TopTenPost> datas;
	
	public Top10ListAdapter(Context context) {
		this.context = context;
	}

	public Top10ListAdapter(Context context, List<TopTenPost> datas) {
		this.context = context;
		this.datas = datas;
	}

	public void setDatas(List<TopTenPost> datas) {
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public Object getItem(int location) {
		return datas.get(location);
	}

	public long getItemId(int location) {
		return location;
	}

	public View getView(int location, View view, ViewGroup parent) {
		if(datas == null) {
			return null;
		}
		if(view == null) {
			Top10ItemView itemView = new Top10ItemView(context);
			itemView.updateView(datas.get(location),location);
			view = itemView;
		} else {
			((Top10ItemView) view).updateView(datas.get(location),location);
		}
		return view;
	}
	

}
