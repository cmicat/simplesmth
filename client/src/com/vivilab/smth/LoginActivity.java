package com.vivilab.smth;

import com.vivilab.smth.R;
import com.vivilab.smth.helper.SmthHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	private static final String TAG = "LoginActivity";  
	private UserDbAdapter mDbHelper;
	private Button loginButton;
	private EditText mUserid;
	private EditText mPasswd;
	private ProgressDialog dialog;
	private Activity presentActivity;
	private ImageView logo;
	private int state = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "app started");
        mDbHelper = new UserDbAdapter(this);
        mDbHelper.open();
        presentActivity = this;
        doLogin();
    }

    
    private void doLogin() {
        Cursor c = mDbHelper.fetchUser();
        if(c!=null&&c.getCount()>0)
        {
        	startManagingCursor(c);
        	String userid=c.getString(c.getColumnIndexOrThrow(UserDbAdapter.KEY_ID));
        	String passwd=c.getString(c.getColumnIndexOrThrow(UserDbAdapter.KEY_PASSWD));
        	Log.i(TAG, "get user from db:"+userid);
        	dialog = ProgressDialog.show(LoginActivity.this, "",getString(R.string.info_login), true);
        	LoginThread login = new LoginThread(handler,userid,passwd,1);
        	login.start();
        }
        else
        {
	        setContentView(R.layout.main);
	        loginButton = (Button) findViewById(R.id.ok);
	        mUserid = (EditText) findViewById(R.id.userid);
	        mPasswd = (EditText) findViewById(R.id.passwd);
	        logo  = (ImageView) findViewById(R.id.ImageView01);
	        if(logo!=null)
	        {
	        	//landscape mode no image
	        	logo.setImageResource(R.drawable.sm);
	        }
        	loginButton.setOnClickListener(this);
        }
        
    }

	public void onClick(View v) {
		Log.i(TAG, "login user:"+mUserid.getText().toString()+",pass:"+mPasswd.getText().toString());
    	dialog = ProgressDialog.show(LoginActivity.this, "",getString(R.string.info_login), true);
    	LoginThread login = new LoginThread(handler,mUserid.getText().toString(),mPasswd.getText().toString(),0);
    	login.start();
	}
	
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	dialog.dismiss();
            int state = msg.getData().getInt("state");
            if(state>0)
            {
            	mDbHelper.close();
	            Intent i = new Intent(presentActivity,TabHomeActivity.class);
	            startActivity(i);
            }
            else
            {
            	Toast.makeText(getApplicationContext(),getString(R.string.info_login_fail),Toast.LENGTH_SHORT).show();
            }
        }
    };
	
	
	private class LoginThread extends Thread{
		private String mUserid;
		private String mPasswd;
		private int mFromDB;
		private Handler mHandler;
		public LoginThread(Handler handler,String userid,String passwd,int fromDb)
		{
			mUserid = userid;
			mPasswd = passwd;
			mFromDB = fromDb;
			mHandler = handler;
		}
		
		 public void run() {
			 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(presentActivity);
			 String apiHost = prefs.getString("ServerUrl", "http://smth.vivilab.info/");
			 SmthHelper.setApiHost(apiHost);
			 int state=SmthHelper.login(mUserid,mPasswd);
			 if(state>0 && mFromDB!=1)
				 mDbHelper.createUser(mUserid, mPasswd);
				 
			 Message msg = mHandler.obtainMessage();
			 Bundle b = new Bundle();
			 b.putInt("state", state);
			 msg.setData(b);
             mHandler.sendMessage(msg);
		 }
	}
	

    protected void onPause() {
        super.onPause();
        state = 1;
        Log.i(TAG,"i m on pause!what shall i do??");
    }
    
    protected void onDestroy(){
    	super.onDestroy();
    	mDbHelper.close();
    }
	
    protected void onRestart() {
        super.onRestart();
    	SmthHelper.logout();
    	Log.i(TAG,"logout,finish this act");
    	setResult(RESULT_OK);
    	finish();
    	System.exit(0);
    }

    protected void onStop() {
        super.onStop();
    	Log.i(TAG,"after login,finish this act");
//    	setResult(RESULT_OK);
//    	finish();
    }
 
}