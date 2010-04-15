package com.vivilab.smth;

import com.vivilab.smth.R;
import com.vivilab.smth.helper.SmthHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PostActivity extends Activity implements OnClickListener{

	private EditText mTitle;
	private EditText mContent;
	private String mBoard;
	private String reid;
	private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);
        setTitle(getString(R.string.info_inpost));
        mTitle = (EditText) findViewById(R.id.title);
        mContent = (EditText) findViewById(R.id.body);
        Bundle extra = getIntent().getExtras();
        mBoard = extra.getString("board");
        reid = extra.getString("reid");
        if(!reid.equals("0"))
        {
        	String title = extra.getString("title");
        	mTitle.setText(title);
        }
        Button postButton = (Button)findViewById(R.id.post);
        postButton.setOnClickListener(this);
    }

    
	public void onClick(View v) {
		dialog = ProgressDialog.show(PostActivity.this, "",getString(R.string.info_post), true);
		PostThread postThread = new PostThread(handler,mTitle.getText().toString(), mContent.getText().toString());
		postThread.start();
	}
	
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	dialog.dismiss();
            int state = msg.getData().getInt("state");
            Intent mIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("state", state);
            mIntent.putExtras(bundle);
            setResult(RESULT_OK, mIntent);
            finish();    
        }
    };
	
	private class PostThread extends Thread{
		private String title;
		private String content;
		private Handler mHandler;
		public PostThread(Handler handler,String title,String content)
		{
			this.title = title;
			this.content = content;
			this.mHandler = handler;
		}
		
		 public void run() {
			 int state=SmthHelper.post(mBoard, title,content,reid);
			 Message msg = mHandler.obtainMessage();
			 Bundle b = new Bundle();
			 b.putInt("state", state);
			 msg.setData(b);
             mHandler.sendMessage(msg);
		 }
	}
	
}
