package com.vivilab.smth;

import com.vivilab.smth.R;
import com.vivilab.smth.helper.SmthHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class TabHomeActivity extends TabActivity{
	private TabActivity currentActivity;
	private UserDbAdapter mDbHelper;
	private final static String TAG ="TabHomeActivity";
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    currentActivity = this;
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

	public static final int GOBOARD = Menu.FIRST;
	public static final int CHANGEUSER = Menu.FIRST+1;
	public static final int EXIT = Menu.FIRST+2;
	public static final int ABOUT = Menu.FIRST+3;

	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, GOBOARD, 0, R.string.info_goboard);
	    menu.add(0, CHANGEUSER, 0, R.string.info_changeuser);
	    menu.add(0, EXIT, 0, R.string.info_exit);
	    menu.add(0, ABOUT, 0, R.string.info_about);
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
		    case EXIT:
		    	doExit();
		        return true;
		    case ABOUT:
		    	doAbout();
		        return true;
	    }
	    return false;
	}
	
    private static final int DIALOG_BOARD = 1;
    private static final int DIALOG_ABOUT = 2;
    
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_BOARD:
            LayoutInflater factory = LayoutInflater.from(this);
            final View textEntryView = factory.inflate(R.layout.goboard_dialog, null);
            return new AlertDialog.Builder(TabHomeActivity.this)
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
        case DIALOG_ABOUT:
        	Log.i(TAG,"show about");
        	//Context mContext = getApplicationContext();
        	Dialog dialog = new Dialog(this);

        	dialog.setContentView(R.layout.about);
        	dialog.setTitle("About");

        	TextView text = (TextView) dialog.findViewById(R.id.about_info);
        	text.setText(getString(R.string.info_version));
        	ImageView image = (ImageView) dialog.findViewById(R.id.icon_image);
        	image.setImageResource(R.drawable.icon);
        	return dialog;
        }
        return null;

    }

	
	private void doGoBoard()
	{
		showDialog(DIALOG_BOARD);
	}

	private void doChangeUser()
	{
		SmthHelper.logout();
        mDbHelper = new UserDbAdapter(this);
        mDbHelper.open();
        mDbHelper.clearUser();
        mDbHelper.close();
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
	}
	
	private void doExit()
	{
		SmthHelper.logout();
    	setResult(RESULT_OK);
    	finish();
	}
	
	private void doAbout()
	{
		showDialog(DIALOG_ABOUT);
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
