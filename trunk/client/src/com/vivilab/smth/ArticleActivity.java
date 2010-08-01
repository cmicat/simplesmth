package com.vivilab.smth;

import com.vivilab.smth.R;
import com.vivilab.smth.helper.SmthHelper;
import com.vivilab.smth.model.Article;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ArticleActivity extends Activity{
	private TextView tv;
	private ScrollView sv;
	private Article article;
	private String board;
	private String id;
	private String from;
	private String title;
	private final static String TAG="ArticleActivity";
	private ImageButton topPost;
	private ImageButton prevPost;
	private ImageButton nextPost;
	private ImageButton replyPost;
	private ImageButton downAttach;
	
	private String showDisplay(Article article)
	{
    	StringBuffer displayContent = new StringBuffer();
    	displayContent.append("作者:"+article.getAuthor()).append(",信区:"+board).append("\n");
    	displayContent.append("发表时间:"+article.getDate()+"\n");
    	displayContent.append("\n");
    	displayContent.append(article.getContent());
    	return displayContent.toString();
	}
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.article);
	    topPost = (ImageButton) findViewById(R.id.top);
	    prevPost = (ImageButton) findViewById(R.id.prevpost);
	    nextPost = (ImageButton) findViewById(R.id.nextpost);
	    replyPost = (ImageButton) findViewById(R.id.reply);
	    downAttach = (ImageButton) findViewById(R.id.downAttach);
	    downAttach.setVisibility(View.INVISIBLE);
        Bundle extras = getIntent().getExtras();
        board = extras.getString("board");
        id=extras.getString("id");
        from = extras.getString("from");
        title = extras.getString("title");
        this.setTitle(title);
        article = SmthHelper.beautya(board, id,null);
        sv = (ScrollView)findViewById(R.id.ScrollView01);
        if(article!=null)
        {
        	tv = (TextView) findViewById(R.id.content);
   		 	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
   		 	int textSize = Integer.parseInt(prefs.getString("TextSize", "15"));
        	tv.setTextSize(textSize);
        	tv.setText(showDisplay(article));
        	if(article.getHasAttach()!=0)
        		downAttach.setVisibility(View.VISIBLE);
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
	public static final int BACK = Menu.FIRST+4;
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, READ_TP, 0, R.string.read_tp);
	    menu.add(0, READ_TN, 0, R.string.read_tn);
	    menu.add(0, READ_TOP, 0, R.string.read_top);
	    menu.add(0, READ_REPLY, 0, R.string.read_reply);
	    menu.add(0, BACK, 0, R.string.back_board);
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
		    case BACK:
		    	doBack();
		        return true;
	    }
	    return false;
	}
	
	private void doReadTp()
	{
		article = SmthHelper.beautya(board, article.getId(), "tp");
		sv.scrollTo(0, 0);
        tv.setText(showDisplay(article));
    	if(article.getHasAttach()!=0)
    		downAttach.setVisibility(View.VISIBLE);

        this.setTitle(article.getTitle());
	}
	private void doReadTn()
	{
		article = SmthHelper.beautya(board, article.getId(), "tn");
		sv.scrollTo(0, 0);
        tv.setText(showDisplay(article));
    	if(article.getHasAttach()!=0)
    		downAttach.setVisibility(View.VISIBLE);

        this.setTitle(article.getTitle());
	}
	
	private void doReadTop()
	{
		article = SmthHelper.beautya(board, article.getTopid(), null);
		sv.scrollTo(0, 0);
        tv.setText(showDisplay(article));
    	if(article.getHasAttach()!=0)
    		downAttach.setVisibility(View.VISIBLE);

        this.setTitle(article.getTitle());
	}
	
	private static final int ACTIVITY_REPLY=0;
	private void doReply()
	{
		Intent i = new Intent(this, PostActivity.class);
		i.putExtra("board",board);
		i.putExtra("reid", article.getId());
		if(article.getTitle().startsWith("Re"))
			i.putExtra("title", article.getTitle());
		else
			i.putExtra("title", "Re: "+article.getTitle());
		String quote="";
		quote = "\n【 在 "+article.getAuthor()+" 的大作中提到: 】\n";
		String[] contents = article.getContent().split("\n");
		if(contents.length>2)
			quote = quote+": "+contents[0]+"\n: "+contents[1];
		else
			quote = quote+": "+contents[0];
		i.putExtra("quote", quote);		
		startActivityForResult(i, ACTIVITY_REPLY);
	}
	
	private void doBack()
	{
		if(from.equals("top"))
		{
			Log.i(TAG, "we are going to show board:" + this.board);
			Intent i = new Intent(this, BoardActivity.class);
			i.putExtra("board", this.board);
			startActivity(i);
		}
		else
		{
	    	setResult(RESULT_OK);
	    	finish();
		}
	}
	
    protected void onResume()
    {
    	super.onResume();
    	//handle button
    	topPost.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.i(TAG,"go first");
    			doReadTop();
    		}
    		
    	});
    	
    	prevPost.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.i(TAG,"go last");
    			doReadTp();
    		}
    		
    	});

    	nextPost.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.i(TAG,"go next");
    			doReadTn();
    		}
    		
    	});
    	
    	replyPost.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.i(TAG,"go Prev");
    			doReply();
    		}
    		
    	});

    	downAttach.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.i(TAG,"open url for attachment");
    			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.newsmth.net/"+article.getAtthUrl())));
    		}
    		
    	});

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
    protected void onStop() {
        super.onStop();
    	setResult(RESULT_OK);
    	finish();
    }

}
