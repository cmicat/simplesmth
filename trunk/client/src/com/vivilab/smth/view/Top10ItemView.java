package com.vivilab.smth.view;

import com.vivilab.smth.R;
import com.vivilab.smth.model.TopTenPost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Top10ItemView extends LinearLayout{

	private Context context;
	private TextView topTextView, bottomTextView;
	private String gId;
	private String title;
	private String board;
	public Top10ItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public Top10ItemView(Context context) {
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
	
	public String getGId()
	{
		return gId;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getBoard()
	{
		return board;
	}
	
	public void updateView(TopTenPost d,int seq) {
		topTextView.setText(String.valueOf(seq+1)+"."+d.getTitle());
		bottomTextView.setText(d.getAuthor()+"发表于"+d.getBoard());
		gId = d.getGid();
		title = d.getTitle();
		board = d.getBoard();
	}

}
