package com.vivilab.smth;

import java.util.List;

import com.vivilab.R;
import com.vivilab.smth.helper.SmthHelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShowFavActivity extends ListActivity implements OnItemClickListener{

	private final static String TAG = "ShowFavActivity";
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  List myFav=SmthHelper.getFavorate();
		  setListAdapter(new ArrayAdapter<String>(this, R.layout.listfav, myFav));

		  ListView lv = getListView();
		  lv.setTextFilterEnabled(false);

		  lv.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
//	      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
//		          Toast.LENGTH_SHORT).show();
		Log.i(TAG, "we are going to show board:"+((TextView) view).getText());
		Intent i = new Intent(this, BoardActivity.class);
		i.putExtra("board", ((TextView) view).getText());
		startActivity(i);
	}
}
