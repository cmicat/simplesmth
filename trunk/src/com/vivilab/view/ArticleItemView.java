package com.vivilab.view;


import com.vivilab.R;
import com.vivilab.model.Article;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ArticleItemView extends LinearLayout{
	private Context context;
	private TextView topTextView, bottomTextView;
	private String articleId;
	public ArticleItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public ArticleItemView(Context context) {
		super(context);
		initialize(context);
	}
	
	private void initialize(Context context) {
		this.context = context;
		View view = LayoutInflater.from(this.context).inflate(R.layout.thread, null);
		topTextView = (TextView) view.findViewById(R.id.topTextView);
		bottomTextView = (TextView) view.findViewById(R.id.bottomTextView);
		addView(view);
	}
	
	public String getArticleId()
	{
		return articleId;
	}
	
	public CharSequence getTitle()
	{
		return topTextView.getText();
	}
	
	public void updateView(Article d) {
		topTextView.setText(d.getTitle());
		bottomTextView.setText(d.getInfo());
		articleId = d.getId();
	}


}
