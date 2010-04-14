package com.vivilab;

import com.vivilab.helper.SmthHelper;
import com.vivilab.model.Article;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ArticleActivity extends Activity{
	private TextView tv;
	private Article article;
	private String board;
	private String id;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.article);
        Bundle extras = getIntent().getExtras();
        board = extras.getString("board");
        id=extras.getString("id");
        String title = extras.getString("title");
        this.setTitle(title);
        article = SmthHelper.article(board, id,null);
        tv = (TextView) findViewById(R.id.content);
        tv.setText(article.getContent());
	}

	//menu
	public static final int READ_TP = Menu.FIRST;
	public static final int READ_TN = Menu.FIRST+1;
	public static final int READ_TOP = Menu.FIRST+2;
	public static final int READ_REPLY = Menu.FIRST+3;
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, READ_TP, 0, R.string.read_tp);
	    menu.add(0, READ_TN, 0, R.string.read_tn);
	    menu.add(0, READ_TOP, 0, R.string.read_top);
	    menu.add(0, READ_REPLY, 0, R.string.read_reply);
	    return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case READ_TP:
		    	doReadTp();
		        return true;
		    case READ_TN:
		    	doReadTn();
		        return true;
		    case READ_TOP:
		    	doReadTop();
		        return true;
	    }
	    return false;
	}
	
	private void doReadTp()
	{
		article = SmthHelper.article(board, article.getId(), "tp");
        tv.setText(article.getContent());
        this.setTitle(article.getTitle());
	}
	private void doReadTn()
	{
		article = SmthHelper.article(board, article.getId(), "tn");
        tv.setText(article.getContent());		
        this.setTitle(article.getTitle());
	}
	
	private void doReadTop()
	{
		article = SmthHelper.article(board, article.getTopid(), null);
        tv.setText(article.getContent());		
        this.setTitle(article.getTitle());
	}
	
}
