package com.vivilab.smth;

import java.util.List;

import com.vivilab.smth.R;
import com.vivilab.smth.helper.SmthHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShowFavActivity extends ListActivity implements
		OnItemClickListener {

	private final static String TAG = "ShowFavActivity";
	private List myFav;
	private ProgressDialog dialog;
	private ListActivity currentActivity;
	private UserDbAdapter mDbHelper;

	private int state = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// myFav=SmthHelper.getFavorate();
		ListView lv = getListView();
		lv.setTextFilterEnabled(false);

		lv.setOnItemClickListener(this);
		currentActivity = this;
		if(state!=2)
		{
			dialog = ProgressDialog.show(ShowFavActivity.this, "",
					getString(R.string.info_getfav), true);
			ShowFav showFav = new ShowFav(handler);
			showFav.start();
		}
		else
		{
			setListAdapter(new ArrayAdapter<String>(currentActivity,
					R.layout.listfav, myFav));
			
		}
	}

	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Log.i(TAG, "we are going to show board:" + ((TextView) view).getText());
		Intent i = new Intent(this, BoardActivity.class);
		i.putExtra("board", ((TextView) view).getText());
		startActivity(i);
	}

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			dialog.dismiss();
			int size = msg.getData().getInt("size");
			if (size > 0) {
				setListAdapter(new ArrayAdapter<String>(currentActivity,
						R.layout.listfav, myFav));
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.info_no_fav), Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	private class ShowFav extends Thread {
		private Handler mHandler;

		public ShowFav(Handler handler) {
			mHandler = handler;
		}

		public void run() {
			myFav = SmthHelper.getFavorate();
			int size = myFav.size();
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt("size", size);
			msg.setData(b);
			mHandler.sendMessage(msg);

		}
	}
	//menu 
	public static final int GOBOARD = Menu.FIRST;
	public static final int CHANGEUSER = Menu.FIRST+1;

	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, GOBOARD, 0, R.string.info_goboard);
	    menu.add(0, CHANGEUSER, 0, R.string.info_changeuser);
	    return true;
	}	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case GOBOARD:
		    	doGoBoard();
		        return true;
		    case CHANGEUSER:
		    	doChangeUser();
		        return true;
	    }
	    return false;
	}
	
    private static final int DIALOG_BOARD = 1;
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_BOARD:
            LayoutInflater factory = LayoutInflater.from(this);
            final View textEntryView = factory.inflate(R.layout.goboard_dialog, null);
            return new AlertDialog.Builder(ShowFavActivity.this)
                .setTitle(R.string.info_goboard)
                .setView(textEntryView)
                .setPositiveButton("GO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	EditText boardNameEdit = (EditText) textEntryView.findViewById(R.id.board_input);
                		Intent i = new Intent(currentActivity, BoardActivity.class);
                		i.putExtra("board", boardNameEdit.getText().toString());
                		startActivity(i);	
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked cancel so do some stuff */
                    }
                })
                .create();

        }
        return null;

    }

	
	private void doGoBoard()
	{
		showDialog(DIALOG_BOARD);
	}

	private void doChangeUser()
	{
        mDbHelper = new UserDbAdapter(this);
        mDbHelper.open();
        mDbHelper.clearUser();
        mDbHelper.close();
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"i m on pause!what shall i do??");
    }
	
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"i m on resume!what shall i do??");
    }
}
