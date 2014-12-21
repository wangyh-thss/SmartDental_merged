package com.edu.thss.smartdental;

import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.Map;
import java.awt.Color;

import com.edu.thss.smartdental.model.tooth.ToothChartView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener; 

public class Tooth2DIllness4Fragment extends Fragment{
	private Bitmap template;
	private Bitmap tooth2D;
	private ArrayList <Integer> pCode, pCoor;
	private ImageView toothView;
	
	private int getCode(int color){
		int r = android.graphics.Color.red(color);
		int g = android.graphics.Color.green(color);
		int b = android.graphics.Color.blue(color);
		if (r == g && r % 8 == 0 && g % 8 == 0){
			if (b == 23)
				return r / 8 + 32;
			if (b == 233)
				return r / 8;
			return -1;
		}
		else
			return -1;
	}
	
	private void InitImage(ImageView toothView){
		template = BitmapFactory.decodeResource(getResources(), R.drawable.tooth_2d);
		//tooth2D = template.copy(template.getConfig(), true);
		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;
		
		if (1.0 * height / width < 2.0 / 1.3)
			width = (int)(height * 1.3 / 2.0);
		else
			height = (int)(width * 2.0 / 1.3);
		
		tooth2D = Bitmap.createScaledBitmap(template, width, height, false);
		template = tooth2D.copy(tooth2D.getConfig(), true);
		int w = tooth2D.getWidth();
		int h = tooth2D.getHeight();
		int[] pixels = new int[w * h];
		tooth2D.getPixels(pixels, 0, w, 0, 0, w, h);
		pCoor = new ArrayList <Integer>();
		pCode = new ArrayList <Integer>();
		for (int i = 0; i < w * h; ++i){
			int code = getCode(pixels[i]);
			if (code >= 0){
				pCoor.add(i);
				pCode.add(code);
			}
			if (code >= 0 && code < 32)
				pixels[i] = android.graphics.Color.rgb(255, 255, 255);
			if (code >= 32)
				pixels[i] = android.graphics.Color.rgb(233, 233, 233);
		}
		tooth2D.setPixels(pixels, 0, w, 0, 0, w, h);
		toothView.setImageBitmap(tooth2D);
		/*
		tooth2D.getPixels(pixels, 0, w, 0, 0, w, h);
		for (int i = 0; i < w * h; ++i)
			pixels[i] = 0;
		tooth2D.setPixels(pixels, 0, w, 0, 0, w, h);*/
		
	}
	
	private void fill(int code, int color, ImageView toothView){
		int w = tooth2D.getWidth();
		for (int i = 0; i < pCoor.size(); ++i){
			if (pCode.get(i) == code)
				tooth2D.setPixel(pCoor.get(i) % w, pCoor.get(i) / w, color);
		}
		toothView.setImageBitmap(tooth2D);
	}
	
	private void clearToothColor(){
		int white = android.graphics.Color.rgb(255, 255, 255);
		for (int i = 0; i < 32; i++){
			fill(i, white, toothView);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		

		View rootView = inflater.inflate(R.layout.tooth_2d_illness4, container,false);
		toothView = (ImageView)rootView.findViewById(R.id.tooth_2d_img);
		
		//Spinner
		Spinner spinner=(Spinner)rootView.findViewById(R.id.illness);
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long id){
				String result = parent.getItemAtPosition(pos).toString();
				clearToothColor();

				if (result.equals("无")){
					
				}
				else if(result.equals("牙龈炎")){
					int color = android.graphics.Color.rgb(255, 0, 0);
					fill(0,color,toothView);
					fill(10,color,toothView);
					fill(20,color,toothView);
				}
				else if(result.equals("牙周炎")){
					int color = android.graphics.Color.rgb(0, 255, 0);
					fill(2,color,toothView);
					fill(12,color,toothView);
					fill(22,color,toothView);
				}
				else if(result.equals("龋齿")){
					int color = android.graphics.Color.rgb(0, 0, 255);
					fill(3,color,toothView);
					fill(31,color,toothView);
					fill(17,color,toothView);
				}
				else {
					int color = android.graphics.Color.rgb(0, 0, 0);
					fill(0,color,toothView);
					fill(10,color,toothView);
					fill(20,color,toothView);
					fill(2,color,toothView);
					fill(12,color,toothView);
					fill(22,color,toothView);
					fill(3,color,toothView);
					fill(31,color,toothView);
					fill(17,color,toothView);
				}
			}
			public void onNothingSelected(AdapterView<?> arg0){
			}
		});
		
		//Tooth-2D
		InitImage(toothView);
		toothView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int imageW = tooth2D.getWidth();
				int imageH = tooth2D.getHeight();
				int viewW = toothView.getWidth();
				int viewH = toothView.getHeight();
				double rateW = 1.0 * imageW / viewW;
				double rateH = 1.0 * imageH / viewH;
				double rate = Math.max(rateW, rateH);
				
				int x = (int)((event.getX() - viewW / 2) * rate + imageW / 2);
				int y = (int)((event.getY() - viewH / 2) * rate + imageH / 2);
				
				System.out.println(x);
				System.out.println(y);
				//toothView.
				//touchY = (int)(event.getY()/toothView.scale);
			    //AlertDialog.Builder builder = new AlertDialog
			    //		.Builder(com.edu.thss.smartdental.model.tooth.Tooth2DFragment.this); 
				//builder.setMessage(Float.toString(scaleX));
				//builder.show();
				if (x < 0 || x >= imageW || y < 0 || y >= imageH)
					return false;
				int code = -1;
				for (int i = 0; i < pCoor.size(); ++i){
					if (pCoor.get(i) == y * imageW + x){
						code = pCode.get(i);
						break;
					}
				}
				switch(event.getAction() & MotionEvent.ACTION_MASK){
					case MotionEvent.ACTION_DOWN:
						if (code >= 0){
							showCustomDialog(code);
							if (code > 32)
								code -= 32;
							int color = android.graphics.Color.rgb(160, 160, 160);
							//fill(code, color, toothView);
							color = android.graphics.Color.rgb(255, 255, 255);
							//fill(code, color, toothView);
						}
						/*
						int color = template.getPixel((int)(x), (int)(y));
						int r = android.graphics.Color.red(color);
						int g = android.graphics.Color.green(color);
						int b = android.graphics.Color.blue(color);
						if (b == 23 || b == 233 && r == g && r % 8 == 0 && g % 8 == 0){
							fill(r / 8 , 0);
							showCustomDialog(r / 8);
						}*/
						break;
				}
				return false;
			}
			
		});
		/*
		final ToothChartView toothView = (ToothChartView)rootView.findViewById(R.id.tooth_2d);
		toothView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction() & MotionEvent.ACTION_MASK){
					case MotionEvent.ACTION_DOWN:
						Log.i("x", event.getX()/toothView.scale+"");
						Log.i("y", event.getY()/toothView.scale+"");
						toothView.touchX = (int)(event.getX()/toothView.scale);
						toothView.touchY = (int)(event.getY()/toothView.scale);
						int color = toothView.bgImg.getPixel(toothView.touchX, toothView.touchY);
						int red = (color & 0x00ff0000) >> 16;
						int greed = (color & 0x0000ff00) >> 8;
						int blue = color & 0x000000ff;
						int toothIndex = red / 8;
						showCustomDialog();
						break;
					
				}
				return false;
			}
		});
		*/
		//Context mContext = getApplicationContext();  
		
		return rootView;
	}
	
	protected void showCustomDialog(int code) {
    	final Dialog dialog = new Dialog(super.getActivity());  
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        dialog.setContentView(R.layout.dialoglayout);  
        Button button1 = (Button)dialog.findViewById(R.id.dialog_tooth_myinfo);
        Button button2 = (Button)dialog.findViewById(R.id.dialog_tooth_intr);
        
        /*Add*/
        TextView number = (TextView)dialog.findViewById(R.id.dialog_tooth_code);
        number.setText(Integer.toString(code));
        
        TableLayout tableLayout = (TableLayout)dialog.findViewById(R.id.table3);
		tableLayout.setVisibility(View.GONE);
		
		
		DisplayMetrics  dm = new DisplayMetrics();   
	     //取锟矫达拷锟斤拷锟斤拷锟斤拷   
	     super.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);   
	        
	     //锟斤拷锟节的匡拷锟�   
	    int screenWidth = dm.widthPixels;   
	        
	    //锟斤拷锟节高讹拷   
	    int screenHeight = dm.heightPixels;          
		
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		
		lp.x = (int) (screenWidth*0.05); // 锟斤拷位锟斤拷X锟斤拷锟�
	    lp.y = (int) (screenWidth*0.05); // 锟斤拷位锟斤拷Y锟斤拷锟�
	    lp.width = (int) (screenWidth*0.9); // 锟斤拷锟�
	    lp.height = (int) (screenHeight*0.9); // 锟竭讹拷
	    lp.alpha = 1.0f; // 透锟斤拷锟斤拷

        dialogWindow.setAttributes(lp);
        
        button1.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 TableLayout tableLayout = (TableLayout)dialog.findViewById(R.id.table1);
				 tableLayout.setVisibility(View.VISIBLE);
				 tableLayout = (TableLayout)dialog.findViewById(R.id.table2);
				 tableLayout.setVisibility(View.VISIBLE);
				 tableLayout = (TableLayout)dialog.findViewById(R.id.table3);
				 tableLayout.setVisibility(View.GONE);
			}
		});
        button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 TableLayout tableLayout = (TableLayout)dialog.findViewById(R.id.table3);
				 tableLayout.setVisibility(View.VISIBLE);
				 tableLayout = (TableLayout)dialog.findViewById(R.id.table1);
				 tableLayout.setVisibility(View.GONE);
				 tableLayout = (TableLayout)dialog.findViewById(R.id.table2);
				 tableLayout.setVisibility(View.GONE);
			}
		}); 
        dialog.show();  
    }
}
