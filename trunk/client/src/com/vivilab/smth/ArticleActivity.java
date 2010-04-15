package com.vivilab.smth;

import com.vivilab.smth.R;
import com.vivilab.smth.helper.SmthHelper;
import com.vivilab.smth.model.Article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
        if(article!=null)
        {
        	tv = (TextView) findViewById(R.id.content);
        	tv.setText(article.getContent());
        }else
        {
        	Toast.makeText(getApplicationContext(),getString(R.string.info_session_fail),Toast.LENGTH_SHORT).show();
        	setResult(RESULT_OK);
        	finish();

        }
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
		    case READ_REPLY:
		    	doReply();
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
	
	private static final int ACTIVITY_REPLY=0;
	private void doReply()
	{
		Intent i = new Intent(this, PostActivity.class);
		i.putExtra("board",board);
		i.putExtra("reid", article.getId());
		i.putExtra("title", "Re:"+article.getTitle());
		startActivityForResult(i, ACTIVITY_REPLY);
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	 super.onActivityResult(requestCode, resultCode, intent);
    	 if(requestCode == ACTIVITY_REPLY)
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
	    		 }
        	 }
    		 
    	 }
    	 //going to this board
    	 Intent i = new Intent(this, BoardActivity.class);
 		i.putExtra("board", board);
		startActivity(i);

    }

}
