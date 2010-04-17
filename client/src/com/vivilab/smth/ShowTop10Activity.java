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
	private List topList;
	private ProgressDialog dialog;
	private ListActivity currentActivity;
	private Top10ListAdapter datasAdapter;
	private ListView lv;
	
	private int state=0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// myFav=SmthHelper.getFavorate();
		lv = getListView();
		lv.setTextFilterEnabled(false);

		lv.setOnItemClickListener(this);
		currentActivity = this;
		if(state!=2)
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
	
	
	
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Log.i(TAG, "we are going to show topic gid:" + ((Top10ItemView) view).getGId());
		state = 2;
		Intent i = new Intent(this, ArticleActivity.class);
		i.putExtra("from", "top");
		i.putExtra("id",((Top10ItemView) view).getGId());
		i.putExtra("board",((Top10ItemView) view).getBoard());
		i.putExtra("title",((Top10ItemView) view).getTitle());
		startActivity(i);
		
	}

	
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			dialog.dismiss();
			int size = msg.getData().getInt("size");
			if (size > 0) {
				lv.setAdapter(datasAdapter);
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.info_no_fav), Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	
	private class ShowTop extends Thread {
		private Handler mHandler;

		public ShowTop(Handler handler) {
			mHandler = handler;
		}

		public void run() {
			topList = SmthHelper.getTopTopic();
			datasAdapter = new Top10ListAdapter(currentActivity, topList);
			//datasAdapter.setDatas(topList);
			int size = topList.size();
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt("size", size);
			msg.setData(b);
			mHandler.sendMessage(msg);
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
	
	
}
