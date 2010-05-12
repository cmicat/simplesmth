package com.vivilab.smth;

import java.util.List;

import com.vivilab.smth.adapter.ArticleListAdapter;
import com.vivilab.smth.adapter.Top10ListAdapter;
import com.vivilab.smth.helper.SmthHelper;
import com.vivilab.smth.view.Top10ItemView;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShowTop10Activity extends ListActivity implements OnItemClickListener{

	private final static String TAG = "ShowTop10Activity";
	private static List topList;
	private static ProgressDialog dialog = null;
	private static ListActivity currentActivity;
	private static Top10ListAdapter datasAdapter;
	private static ListView lv;
	private static boolean working = false;
	private static int state=0;
	
	public void onCreate(Bundle savedInstanceState) {
		currentActivity = this;
		super.onCreate(savedInstanceState);
		// myFav=SmthHelper.getFavorate();
		lv = getListView();
		lv.setTextFilterEnabled(false);

		lv.setOnItemClickListener(this);
		if(!working)
		{
			if(state==0)
			{
				dialog = ProgressDialog.show(ShowTop10Activity.this, "",
						getString(R.string.info_gettop10), true);
				ShowTop showTop = new ShowTop(handler);
				showTop.start();
			}
			else
			{
	        	lv.setAdapter(datasAdapter);
				
			}
		}
		else
		{
			if(state==0)
			{
				dialog = ProgressDialog.show(ShowTop10Activity.this, "",
						getString(R.string.info_gettop10), true);
			}
		}
	}
	
	
	
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Log.i(TAG, "we are going to show topic gid:" + ((Top10ItemView) view).getGId());
		Intent i = new Intent(this, ArticleActivity.class);
		i.putExtra("from", "top");
		i.putExtra("id",((Top10ItemView) view).getGId());
		i.putExtra("board",((Top10ItemView) view).getBoard());
		i.putExtra("title",((Top10ItemView) view).getTitle());
		startActivity(i);
		
	}

	
	final static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			state = 2;
			dialog.dismiss();
			dialog = null;
			int size = msg.getData().getInt("size");
			if (size > 0) {
				lv.setAdapter(datasAdapter);
			} else {
				Toast.makeText(currentActivity,
						currentActivity.getString(R.string.info_no_fav), Toast.LENGTH_SHORT)
						.show();
			}
			working = false;
		}
	};

	
	private class ShowTop extends Thread {
		private Handler mHandler;

		public ShowTop(Handler handler) {
			mHandler = handler;
		}

		public void run() {
			working = true;
			topList = SmthHelper.getTopTopic();
			int size;
			if(topList!=null)
			{
				datasAdapter = new Top10ListAdapter(currentActivity, topList);
				size = topList.size();
			}
			else
			{
				size = 0;
			}
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt("size", size);
			msg.setData(b);
			mHandler.sendMessage(msg);
			//datasAdapter.setDatas(topList);
		}
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
	
    protected void onDestroy() {
        super.onDestroy();
        if(dialog !=null)
        	dialog.dismiss();
    }
	
}
