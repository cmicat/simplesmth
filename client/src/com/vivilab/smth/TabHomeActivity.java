package com.vivilab.smth;

import com.vivilab.smth.R;
import com.vivilab.smth.helper.SmthHelper;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class TabHomeActivity extends TabActivity{
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabhome);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ShowFavActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("fav").setIndicator(getString(R.string.fav),
	                      res.getDrawable(R.drawable.favorites))
	                  .setContent(intent);
	    tabHost.addTab(spec);


	    intent = new Intent().setClass(this, ShowTop10Activity.class);
	    spec = tabHost.newTabSpec("top10").setIndicator(getString(R.string.top10),
	                      res.getDrawable(R.drawable.top10))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    this.setTitle(SmthHelper.getCurrentUser()+"@smth");
	    tabHost.setCurrentTab(0);
	}
}
