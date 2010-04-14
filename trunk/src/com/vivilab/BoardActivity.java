package com.vivilab;

import java.util.List;

import com.vivilab.adapter.ArticleListAdapter;
import com.vivilab.helper.SmthHelper;
import com.vivilab.model.Board;
import com.vivilab.view.ArticleItemView;

import android.app.Activity;
//import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BoardActivity extends Activity implements OnItemClickListener{

	private static final String TAG = "BoardActivity";
	private Context context;
	private ListView listView;
	//private List<Article> datas;
	private ArticleListAdapter datasAdapter;
	private String boardName;
	private Board currentBoard;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        context = this;
        listView = (ListView) findViewById(R.id.listThread);
        Bundle extras = getIntent().getExtras();
        String boardname = extras.getString("board");
        this.setTitle(boardname);
        this.boardName = boardname;
        Log.i(TAG,"ready to get board:"+boardname);
        currentBoard = SmthHelper.getboard(boardname,null);
        datasAdapter = new ArticleListAdapter(context, currentBoard.getArticles());
       	listView.setAdapter(datasAdapter);
       	//listView.u
       	listView.setOnItemClickListener(this);
    }
	
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
//	      Toast.makeText(getApplicationContext(), ((ArticleItemView) view).getArticleId(),Toast.LENGTH_SHORT).show();
		String aid=((ArticleItemView) view).getArticleId();
		String title = (String)((ArticleItemView) view).getTitle();
		Log.i(TAG, "we are going to show article:"+aid);
		Intent i = new Intent(this, ArticleActivity.class);
		i.putExtra("id",aid);
		i.putExtra("board",this.boardName);
		i.putExtra("title",title);
		startActivity(i);
		
	}
	public static final int PR_PAGE = Menu.FIRST;
	public static final int NEXT_PAGE = Menu.FIRST+1;
	public static final int LAST_PAGE = Menu.FIRST+2;
	public static final int POST = Menu.FIRST+3;
	public static final int FIRST_PAGE = Menu.FIRST+4;
	public static final int JINGHUA = Menu.FIRST+5;
	public static final int WENZAI = Menu.FIRST+6;
	public static final int MARK = Menu.FIRST+7;
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, PR_PAGE, 0, R.string.ppage);
	    menu.add(0, NEXT_PAGE, 0, R.string.npage);
	    menu.add(0, LAST_PAGE, 0, R.string.lpage);
	    menu.add(0, POST, 0, R.string.post);
	    menu.add(0, FIRST_PAGE, 0, R.string.fpage);
	    menu.add(0, JINGHUA, 0, R.string.ann);
	    menu.add(0, WENZAI, 0, R.string.wz);
	    menu.add(0, MARK, 0, R.string.mark);
	    return true;
	}	

	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case PR_PAGE:
		    	doPrevPage();
		        return true;
		    case NEXT_PAGE:
		    	doNextPage();
		        return true;
		    case FIRST_PAGE:
		    	doFirstPage();
		    	return true;
		    case LAST_PAGE:
		    	doLastPage();
		    	return true;
		    case WENZAI:
		    	doWenzai();
		    	return true;
		    case MARK:
		    	doMark();
		    	return true;
	    }
	    return false;
	}
	
	private void doPrevPage()
	{
		switch(currentBoard.getFtype()){
		case 0:
			currentBoard = SmthHelper.getboard(this.boardName, String.valueOf(currentBoard.getPpage()));
		case 1:
			currentBoard = SmthHelper.getwz(this.boardName, String.valueOf(currentBoard.getPpage()));
		case 3:
			currentBoard = SmthHelper.getmark(this.boardName, String.valueOf(currentBoard.getPpage()));
		}
        datasAdapter.setDatas(currentBoard.getArticles());
        datasAdapter.notifyDataSetChanged();
	}
	private void doNextPage()
	{
		if(currentBoard.getNpage()>0)
		{
			switch(currentBoard.getFtype()){
			case 0:
				currentBoard = SmthHelper.getboard(this.boardName, String.valueOf(currentBoard.getNpage()));
			case 1:
				currentBoard = SmthHelper.getwz(this.boardName, String.valueOf(currentBoard.getNpage()));
			case 3:
				currentBoard = SmthHelper.getmark(this.boardName, String.valueOf(currentBoard.getNpage()));
			}
	        datasAdapter.setDatas(currentBoard.getArticles());		
	        datasAdapter.notifyDataSetChanged();
		}
		
	}
	
	private void doFirstPage()
	{
		currentBoard = SmthHelper.getboard(this.boardName, "1");
        datasAdapter.setDatas(currentBoard.getArticles());
        datasAdapter.notifyDataSetChanged();		
	}

	private void doLastPage()
	{
		currentBoard = SmthHelper.getboard(this.boardName, null);
        datasAdapter.setDatas(currentBoard.getArticles());
        datasAdapter.notifyDataSetChanged();		
	}
	
	private void doWenzai()
	{
		currentBoard = SmthHelper.getwz(this.boardName, null);
        datasAdapter.setDatas(currentBoard.getArticles());
        this.setTitle(boardName+"-"+this.getString(R.string.wz));
        datasAdapter.notifyDataSetChanged();		
	}
	private void doMark()
	{
		currentBoard = SmthHelper.getmark(this.boardName, null);
        datasAdapter.setDatas(currentBoard.getArticles());
        this.setTitle(boardName+"-"+this.getString(R.string.mark));
        datasAdapter.notifyDataSetChanged();		
	}

}
