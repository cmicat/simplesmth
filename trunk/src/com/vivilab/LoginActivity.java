package com.vivilab;

import com.vivilab.helper.SmthHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener{
	private static final String TAG = "LoginActivity";  
	private UserDbAdapter mDbHelper;
	private Button loginButton;
	private EditText mUserid;
	private EditText mPasswd;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "app started");
        mDbHelper = new UserDbAdapter(this);
        mDbHelper.open();
        doLogin();
        setContentView(R.layout.main);
        loginButton = (Button) findViewById(R.id.ok);
        mUserid = (EditText) findViewById(R.id.userid);
        mPasswd = (EditText) findViewById(R.id.passwd);
        loginButton.setOnClickListener(this);
        
    }
    
    private void doLogin() {
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchUser();
        if(c!=null&&c.getCount()>0)
        {
        	startManagingCursor(c);
        	String userid=c.getString(c.getColumnIndexOrThrow(UserDbAdapter.KEY_ID));
        	String passwd=c.getString(c.getColumnIndexOrThrow(UserDbAdapter.KEY_PASSWD));
        	Log.i(TAG, "get user from db:"+userid);
    		//int result;
    		if(SmthHelper.login(userid,passwd)>0)
    		{
    		//	mDbHelper.createUser(mUserid.getText().toString(), mPasswd.getText().toString());
    			Intent i = new Intent(this, TabHomeActivity.class);
    			startActivity(i);
    		}        	
        }
        
    }

	public void onClick(View v) {
		Log.i(TAG, "login user:"+mUserid.getText().toString()+",pass:"+mPasswd.getText().toString());
		int result;
		if((result=SmthHelper.login(mUserid.getText().toString(), mPasswd.getText().toString()))>0)
		{
			mDbHelper.createUser(mUserid.getText().toString(), mPasswd.getText().toString());
			Intent i = new Intent(this, TabHomeActivity.class);
			startActivity(i);
		}
		else
		{
			Log.w(TAG, "login fail,code:"+result);
		}
	}
}