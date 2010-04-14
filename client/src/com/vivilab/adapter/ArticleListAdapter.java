package com.vivilab.adapter;
import java.util.List;

import com.vivilab.model.Article;
import com.vivilab.view.ArticleItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ArticleListAdapter extends BaseAdapter {

	private static final String TAG = "DemoListAdapter";
	private Context context;
	private List<Article> datas;
	
	public ArticleListAdapter(Context context) {
		this.context = context;
	}

	public ArticleListAdapter(Context context, List<Article> datas) {
		this.context = context;
		this.datas = datas;
	}

	public void setDatas(List<Article> datas) {
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
			ArticleItemView itemView = new ArticleItemView(context);
			itemView.updateView(datas.get(location));
			view = itemView;
		} else {
			((ArticleItemView) view).updateView(datas.get(location));
		}
		return view;
	}
	
}
