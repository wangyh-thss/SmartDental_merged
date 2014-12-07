package com.edu.thss.smartdental;

import com.edu.thss.smartdental.model.tooth.ToothChartView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.view.View.OnClickListener; 

public class Tooth2DFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tooth_2d, container,false);
		Spinner spinner=(Spinner)rootView.findViewById(R.id.illness);
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long id){
				String result = parent.getItemAtPosition(pos).toString();
				if(result == "牙龈炎"){}
				else if(result == "牙周炎"){}
				else if(result == "龋齿"){}
				else {}
			}
			public void onNothingSelected(AdapterView<?> arg0){
			}
		});
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
								
						break;
					
				}
				return false;
			}
		});
		//Context mContext = getApplicationContext();  
		showCustomDialog();
		return rootView;
	}
	
	protected void showCustomDialog() {
    	final Dialog dialog = new Dialog(super.getActivity());  
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        dialog.setContentView(R.layout.dialoglayout);  
        Button button1 = (Button)dialog.findViewById(R.id.dialog_tooth_myinfo);
        Button button2 = (Button)dialog.findViewById(R.id.dialog_tooth_intr);
        TableLayout tableLayout = (TableLayout)dialog.findViewById(R.id.table3);
		tableLayout.setVisibility(View.GONE);
		
		
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		lp.x = 100; // 新位置X坐标
        lp.y = 100; // 新位置Y坐标
        lp.width = 720; // 宽度
        lp.height = 900; // 高度
        lp.alpha = 0.8f; // 透明度
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

