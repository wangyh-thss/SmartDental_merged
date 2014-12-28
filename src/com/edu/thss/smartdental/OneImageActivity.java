package com.edu.thss.smartdental;

import java.awt.MultipleGradientPaint.ColorSpaceType;
import java.awt.peer.CanvasPeer;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.edu.thss.smartdental.model.general.DrawNotateClass;
import com.edu.thss.smartdental.model.general.HttpGetProcess;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.R.xml;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.InputFilter.LengthFilter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
//【画图】
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View; 
import android.graphics.BitmapFactory;  


/**
 * 一张图片浏览页面，实现缩放、拖动、自动居中
 * */
public class OneImageActivity extends Activity implements OnTouchListener,OnClickListener{

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	DisplayMetrics dm;
	Button leftRotate;
	Button rightRotate;
	private Bitmap origin;
	
	float minScaleR; //最小缩放比例
	static final float MAX_SCALE = 5f; //最大缩放比例
	static final float MIN_SCALE = 0.5f;
	
	static final int  NONE = 0;  //初始状态
	static final int DRAG = 1; //拖动
	static final int ZOOM = 2; // 缩放
	int mode = NONE;
	
	PointF prev = new PointF();
	PointF mid = new PointF();
	float dist = 1f;
	
	int rotate; //旋转角度
	DownloadImageTask download;

	private ImageView MarkedRect;
	private TextView NotateInfoName;
	private Handler handler;
	private String[] notate_info;
	private ArrayList<Integer> num_of_point;
	private int notate_num;
	private ArrayList<ArrayList<Integer>> pointX;
	private ArrayList<ArrayList<Integer>> pointY;
	private String JSONInfo = "";
	
	
	public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;
	    Bitmap bitmap;

	    public DownloadImageTask(ImageView bmImage, Bitmap bitmap) {
	        this.bmImage = bmImage;
	        this.bitmap = bitmap;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	            this.bitmap = mIcon11;
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }
	    
	    protected void onPostExecute(Bitmap result) {
	    	Bitmap tmp = result.copy(result.getConfig(), true);
	    	origin = result.copy(result.getConfig(), true);
	    	
	    	selectItem(0);
	        matrix.set(savedMatrix);
			minZoom();
			CheckView();
			bmImage.setImageMatrix(matrix);
			
			
			UpdateNotateListView();
	    }
	}
	
	private void selectItem(int position) {
		SetFocusOnNotationI(position);
		mDrawerLayout.closeDrawer(mDrawerList);
		NotateInfoName.setText(notate_info[position]);
	}
	
	
	private class DrawerItemClickListener implements
		ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}
	
	private void UpdateNotateListView(){
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item_image, notate_info));
	}
	
	private class GetSingleImageInfo implements CallBackInterface {

		@Override
		public void CallBack(String src) {
			// TODO Auto-generated method stub
			try {
				JSONTokener jsonParser = new JSONTokener(src);
				JSONObject jsonObj = (JSONObject) jsonParser.nextValue();
				String picurl = jsonObj.getString("picurl");
				JSONArray notate = jsonObj.getJSONArray("notate");
				notate_num = notate.length();
				pointX = new ArrayList<ArrayList<Integer>>();
				pointY = new ArrayList<ArrayList<Integer>>();
				notate_info = new String [notate_num];
				num_of_point = new ArrayList<Integer>();
				for (int i = 0; i < notate_num; i++) {
					JSONObject jo = (JSONObject)notate.optJSONObject(i);
					JSONArray point = jo.getJSONArray("point");
					num_of_point.add(point.length());
					ArrayList<Integer> x = new ArrayList<Integer>();
					ArrayList<Integer> y = new ArrayList<Integer>();
					for (int j = 0; j < point.length(); j++) {
						JSONObject jo1 = (JSONObject)point.optJSONObject(j);
						x.add((int)jo1.getDouble("x"));
						y.add((int)jo1.getDouble("y"));
					}
					pointX.add(x);
					pointY.add(y);
					notate_info[i] = "批注" + (i+1) + ":" + jo.getString("data");
				}
				
				download.execute("http://166.111.80.119/" + picurl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_image);
		
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg));
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		final String tempclass = bundle.getString("imageclass");

		mTitle = mDrawerTitle = getTitle();
		notate_info = new String[1];
		notate_info[0] = "没有标记";
		notate_num = 0;
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		UpdateNotateListView();
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		NotateInfoName = (TextView)findViewById(R.id.notate_info_name);
		MarkedRect = (ImageView)findViewById(R.id.one_image_view);
		
		new HttpGetProcess(handler, JSONInfo, "http://166.111.80.119/getinfo?caseid=" + tempclass, new GetSingleImageInfo()).start();

		Bitmap bitmap = BitmapFactory.decodeResource(OneImageActivity.this.getResources(),R.drawable.loading); //获取图片资源
		download = new DownloadImageTask(MarkedRect, bitmap);
		origin = bitmap.copy(bitmap.getConfig(), true);
		
		MarkedRect.setImageBitmap(download.bitmap); //填充控件
		MarkedRect.setOnTouchListener(this); //触屏监听
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); //获取分辨率
		minZoom();
		center();
		MarkedRect.setImageMatrix(matrix);
		leftRotate = (Button)findViewById(R.id.image_leftRotate);
		rightRotate = (Button)findViewById(R.id.image_rightRotate);
		leftRotate.setOnClickListener(this);
		this.rightRotate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == leftRotate.getId()){
			//matrix.postRotate(-90,imgView.getWidth()/2,imgView.getHeight()/2);
			matrix.postRotate(-90,dm.widthPixels/2,dm.heightPixels/2);
			MarkedRect.setImageMatrix(matrix);
			savedMatrix.set(matrix);
			mode = NONE;
			CheckView();
		}
		else if(v.getId() == rightRotate.getId()){
			matrix.postRotate(90,dm.widthPixels/2,dm.heightPixels/2);
			MarkedRect.setImageMatrix(matrix);
			savedMatrix.set(matrix);
			mode = NONE;
			CheckView();
		}
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()&MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN: //主点按下
			savedMatrix.set(matrix);
			prev.set(event.getX(),event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN: //副点按下
			dist = spacing(event);
			if(dist > 10f){
				//连续两点的距离大于10，多点模式
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if(mode == DRAG){
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX()-prev.x, event.getY()-prev.y);
			}
			else if(mode == ZOOM){
				float newDist = spacing(event);
				if(newDist >10f){
					matrix.set(savedMatrix);
					float tScale = newDist/dist;
					matrix.postScale(tScale, tScale,mid.x,mid.y);
				}
			}
			break;
		}
		MarkedRect.setImageMatrix(matrix);
		CheckView();
		return true;
		
	}
	/**
	 * 限制最大最小缩放比例，自动居中
	 * */
	private void CheckView(){
		float p[] = new float[9];
		matrix.getValues(p);
		/*if(mode == ZOOM){
			if(p[0] < minScaleR) {
				matrix.setScale(minScaleR, minScaleR);
			}
			if(p[0] > MAX_SCALE) {
				matrix.set(savedMatrix);
			}
		}*/
		//else matrix.set(savedMatrix);
		/*if(p[0]<minScaleR){
			matrix.setScale(minScaleR, minScaleR);
		}*/
		float scale = Math.abs(p[0] + p[3]);
		Log.v("printscale", scale + "");
		if(scale > MAX_SCALE || scale < MIN_SCALE){
			matrix.set(savedMatrix);
			//matrix.setScale(MAX_SCALE, MAX_SCALE);
		}
		center();
	}
	/**
	 * 最小缩放比例
	 * */
	private void minZoom(){
		minScaleR = Math.min(
				(float)dm.widthPixels/(float)download.bitmap.getWidth(), 
				(float)dm.heightPixels/(float)download.bitmap.getHeight());
		if(minScaleR < 1.0){
			matrix.postScale(minScaleR, minScaleR);
		}
	}
	private void center(){
		center(true,true);
	}
	
	/**
	 * 横向、纵向居中
	 * */
	protected void center(boolean horizontal, boolean vertical){
		
		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0,0,download.bitmap.getWidth(),download.bitmap.getHeight());
		m.mapRect(rect);
		
		float height = rect.height();
		float width = rect.width();
		
		float deltaX = 0;
		float deltaY = 0;
		
		// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
		if(vertical){
			int screenHeight = dm.heightPixels;
			if(height<screenHeight){
				deltaY = (screenHeight - height)/2 - rect.top;
			}
			else if(rect.top>0){
				deltaY = -rect.top;
			}
			else if(rect.bottom<screenHeight){
				deltaY = MarkedRect.getHeight() - rect.bottom;
			}
		}
		if(horizontal){
			int screenWidth = dm.widthPixels;
			if(width<screenWidth){
				deltaX = (screenWidth-width)/2 - rect.left;
			}
			else if(rect.left>0){
				deltaX = - rect.left;
			}
			else if(rect.right < screenWidth){
				deltaX = screenWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY);
	}
	
	/**
	 * 两点的距离
	 * */
	private float spacing(MotionEvent event){
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x*x+y*y);
	}
	/**
	 * 两点的中点
	 * */
	private void midPoint(PointF point, MotionEvent event){
		float x = event.getX(0)+event.getX(1);
		float y = event.getY(0)+event.getY(1);
		point.set(x/2,y/2);
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	private void SetFocusOnNotationI(int index) {
		if (notate_num == 0) {
			return;
		}
		int i;
		Bitmap tmpBitmap = origin.copy(origin.getConfig(), true);
		DrawNotateClass draw = new DrawNotateClass(num_of_point, pointX, pointY);
		for (i = 0; i < notate_num; i++) {
			if (i == index) {
				continue;
			}
			else {
				draw.drawNotate(i, 3, tmpBitmap, Color.GREEN);
			}
		}
		draw.drawNotate(index, 5, tmpBitmap, Color.YELLOW);
		MarkedRect.setImageBitmap(tmpBitmap);
	}

}
