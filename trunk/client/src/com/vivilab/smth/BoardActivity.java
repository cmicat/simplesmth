package com.vivilab.smth;

import java.util.List;

import com.vivilab.smth.R;
import com.vivilab.smth.adapter.ArticleListAdapter;
import com.vivilab.smth.helper.SmthHelper;
import com.vivilab.smth.model.Board;
import com.vivilab.smth.view.ArticleItemView;

import android.app.Activity;
//import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BoardActivity extends Activity implements OnItemClickListener{

	private static final String TAG = "BoardActivity";
	private Context context;
	private ListView listView;
	private ArticleListAdapter datasAdapter;
	private String boardName;
	private Board currentBoard;
	private int state;
	private int boardMode;
	private ImageButton first;
	private ImageButton prev;
	private ImageButton next;
	private ImageButton last;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state =0;
        setContentView(R.layout.board);
        context = this;
        listView = (ListView) findViewById(R.id.listThread);
        first = (ImageButton) findViewById(R.id.first);
        prev = (ImageButton) findViewById(R.id.prev);
        next = (ImageButton) findViewById(R.id.next);
        last = (ImageButton) findViewById(R.id.last);
        
        Bundle extras = getIntent().getExtras();
        String boardname = extras.getString("board");
        this.setTitle(boardname);
        this.boardName = boardname;
        Log.i(TAG,"ready to get board:"+boardname);
		 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		 boardMode = Integer.parseInt(prefs.getString("BoardType", "1"));
       if(boardMode !=1)
    	   currentBoard = SmthHelper.getboard(boardname,null);
       else
    	   currentBoard = SmthHelper.gettopic(boardname, null);
        if(currentBoard !=null)
        {
	        if(currentBoard.getArticles().size()>0)
	        {
	        	datasAdapter = new ArticleListAdapter(context, currentBoard.getArticles());
	        	listView.setAdapter(datasAdapter);
	       	//listView.u
	        	listView.setOnItemClickListener(this);
	        }
	        else
	        {
	        	Toast.makeText(getApplicationContext(),getString(R.string.info_no_board),Toast.LENGTH_SHORT).show();
	        	setResult(RESULT_OK);
	        	finish();
	        }
        }
        else
        {
        	Toast.makeText(getApplicationContext(),getString(R.string.info_session_fail),Toast.LENGTH_SHORT).show();
        	setResult(RESULT_OK);
        	finish();
        	
        }
    }
	
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
//	      Toast.makeText(getApplicationContext(), ((ArticleItemView) view).getArticleId(),Toast.LENGTH_SHORT).show();
		String aid=((ArticleItemView) view).getArticleId();
		String title = (String)((ArticleItemView) view).getTitle();
		Log.i(TAG, "we are going to show article:"+aid);
		state = 2;
		Intent i = new Intent(this, ArticleActivity.class);
		i.putExtra("from", "board");
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
		    case POST:
		    	doPost();
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
			break;
		case 1:
			currentBoard = SmthHelper.getwz(this.boardName, String.valueOf(currentBoard.getPpage()));
			break;
		case 3:
			currentBoard = SmthHelper.getmark(this.boardName, String.valueOf(currentBoard.getPpage()));
			break;
		case 6:
			currentBoard = SmthHelper.gettopic(this.boardName, String.valueOf(currentBoard.getPpage()));
			break;
			
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
				break;
			case 1:
				currentBoard = SmthHelper.getwz(this.boardName, String.valueOf(currentBoard.getNpage()));
				break;
			case 3:
				currentBoard = SmthHelper.getmark(this.boardName, String.valueOf(currentBoard.getNpage()));
				break;
			case 6:
				currentBoard = SmthHelper.gettopic(this.boardName, String.valueOf(currentBoard.getNpage()));
				break;
			}
	        datasAdapter.setDatas(currentBoard.getArticles());		
	        datasAdapter.notifyDataSetChanged();
		}
		
	}
	
	private void doFirstPage()
	{
//		currentBoard = SmthHelper.getboard(this.boardName, "1");
		switch(currentBoard.getFtype()){
		case 0:
			currentBoard = SmthHelper.getboard(this.boardName, "1");
			break;
		case 1:
			currentBoard = SmthHelper.getwz(this.boardName, "1");
			break;
		case 3:
			currentBoard = SmthHelper.getmark(this.boardName, "1");
			break;
		case 6:
			currentBoard = SmthHelper.gettopic(this.boardName, "1");
			break;
		}

        datasAdapter.setDatas(currentBoard.getArticles());
        datasAdapter.notifyDataSetChanged();		
	}

	private void doLastPage()
	{
//		currentBoard = SmthHelper.getboard(this.boardName, null);
		switch(currentBoard.getFtype()){
		case 0:
			currentBoard = SmthHelper.getboard(this.boardName, null);
			break;
		case 1:
			currentBoard = SmthHelper.getwz(this.boardName, null);
			break;
		case 3:
			currentBoard = SmthHelper.getmark(this.boardName, null);
			break;
		case 6:
			currentBoard = SmthHelper.gettopic(this.boardName, null);
			break;
		}
		
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
	
	private static final int ACTIVITY_POST=0;
	private void doPost()
	{
		Intent i = new Intent(this, PostActivity.class);
		i.putExtra("board",this.boardName);
		i.putExtra("reid", "0");
		state = 2;
		startActivityForResult(i, ACTIVITY_POST);
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	 super.onActivityResult(requestCode, resultCode, intent);
    	 if(requestCode == ACTIVITY_POST)
    	 {
        	 Bundle extra =intent.getExtras();
        	 if(extra!=null)
        	 {
	        	 int state = extra.getInt("state");
	    		 if(state!=0)
	    		 {
	    			 Toast.makeText(getApplicationContext(),getString(R.string.info_post_fail),Toast.LENGTH_SHORT).show();
	    		 }
	    		 else
	    		 {
	    			 Toast.makeText(getApplicationContext(),getString(R.string.info_post_ok),Toast.LENGTH_SHORT).show();
	    			 doLastPage();
	    		 }
        	 }
    	 }
    }

    protected void onResume()
    {
    	super.onResume();
    	//handle button
    	first.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.i(TAG,"go first");
    			doFirstPage();
    		}
    		
    	});
    	
    	last.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.i(TAG,"go last");
    			doLastPage();
    		}
    		
    	});

    	next.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.i(TAG,"go next");
    			doNextPage();
    		}
    		
    	});
    	
    	prev.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.i(TAG,"go Prev");
    			doPrevPage();
    		}
    		
    	});


    }

    
    protected void onRestart()
    {
    	super.onRestart();
    	state = 0;
    }
    
    protected void onStop() {
        super.onStop();
        if(state!=2)
        {
        	Log.i(TAG,"not back from article,finish activity");
        	setResult(RESULT_OK);
        	finish();
        }
    }
    

}
