package com.edu.thss.smartdental;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.edu.thss.smartdental.db.DBManager;
import com.edu.thss.smartdental.model.tooth.ToothChartView;
import com.edu.thss.smartdental.ui.drawer.NavDrawerItem;
import com.edu.thss.smartdental.ui.drawer.NavDrawerListAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;  
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;  
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.protocol.HTTP;  
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.edu.thss.smartdental.model.tooth.ToothChartView;
import com.edu.thss.smartdental.ui.drawer.NavDrawerItem;
import com.edu.thss.smartdental.ui.drawer.NavDrawerListAdapter;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity implements OnItemClickListener {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	
	private String[] mNavMenuTitles;
	private TypedArray mNavMenuIconsTypeArray;
	private ArrayList<NavDrawerItem> mNavDrawerItems;
	private NavDrawerListAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;

	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findView();
		
		if(savedInstanceState == null){
			selectItem(8);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			finish();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}

		
//		handler = new Handler(){
//		    @Override
//		    public void handleMessage(Message msg) {
//		        super.handleMessage(msg);
//		        Bundle data = msg.getData();
//		        String val = data.getString("result");
//		    }
//		};
//		
//		Runnable runnable = new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				HttpClient client = new DefaultHttpClient();  
//		        String getURL = "http://166.111.80.119/userinfo/tooth?user=baoye";
//		        HttpGet get = new HttpGet(getURL);
//		        String tString = "";
//		        DBManager mgr = new DBManager(MainActivity.this);
//		        try {
//					HttpResponse responseGet = client.execute(get);  
//			        HttpEntity resEntityGet = responseGet.getEntity();  
//			        if (resEntityGet != null) {  
//			                    //do something with the response
//			        	tString = EntityUtils.toString(resEntityGet);
//			        	mgr.updateDatabase(tString);
//			        	//Log.i("GET RESPONSE",EntityUtils.toString(resEntityGet));
//			        	Log.v("printtstring", tString);
//			        }
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//		        Message message = new Message();
//		        Bundle data = new Bundle();
//		        data.putString("result", tString);
//		        message.setData(data);
//		        handler.sendMessage(message);
//			}
//		};
//		new Thread(runnable).start();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		SharedPreferences preferences = getSharedPreferences("setting", MODE_PRIVATE);
		boolean justDeleted = preferences.getBoolean("justDeleted", true);
		if(justDeleted){
			setContentView(R.layout.activity_main);
			findView();
			selectItem(8);
			Editor editor = preferences.edit();
			editor.putBoolean("justDeleted", false);
			editor.commit();
			
		}
		super.onResume();
	}
	
	@SuppressLint("NewApi")
	private void findView(){
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView)findViewById(R.id.left_drawer);
		
		mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_titles);
		mNavMenuIconsTypeArray = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		mNavDrawerItems = new ArrayList<NavDrawerItem>();
		
		//------
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0],mNavMenuIconsTypeArray.getResourceId(0, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1],mNavMenuIconsTypeArray.getResourceId(1, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2],mNavMenuIconsTypeArray.getResourceId(2, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[3],mNavMenuIconsTypeArray.getResourceId(3, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[4],mNavMenuIconsTypeArray.getResourceId(4, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[5],mNavMenuIconsTypeArray.getResourceId(5, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[6],mNavMenuIconsTypeArray.getResourceId(6, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[7],mNavMenuIconsTypeArray.getResourceId(7, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[8],mNavMenuIconsTypeArray.getResourceId(8, -1)));
	
		mNavMenuIconsTypeArray.recycle();
		
		mAdapter = new NavDrawerListAdapter(getApplicationContext(),mNavDrawerItems);
		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemClickListener(this);
	
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg));
		
		mDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close
				){
			public void onDrawerClosed(View view){
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}
			public void onDrawerOpened(View drawerView){
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		selectItem(position);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
       
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	private void selectItem(int position){
		Fragment fragment  = null;
		switch (position){
		case 0: 
			fragment = new InfoFragment();
			break;
		case 1: 
			fragment = new EMRFragment();
			break;
		case 2: 
			fragment = new ToothFragment();
			break;
		case 3: 
			fragment = new ImageFragment();
			break;
		case 4: 
			fragment = new AppointmentFragment();
			break;
		case 5: 
			fragment = new ClassFragment();
			break;
		case 6: 
			fragment = new DataFragment();
			break;
		case 7: 
			fragment = new SettingFragment();
			break;
		case 8:
			fragment = new BBSInFragment(12);
			break;
		default: break;
		}
		if(fragment != null){
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
			
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(mNavMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		else{
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
    @SuppressLint("NewApi")
	@Override
    public void setTitle(CharSequence title){
    	mTitle = title;
    	getActionBar().setTitle(mTitle);
    }

}