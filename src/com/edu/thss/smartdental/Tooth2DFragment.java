package com.edu.thss.smartdental;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.Color;

import com.edu.thss.smartdental.db.DBManager;
import com.edu.thss.smartdental.model.general.SDToothInfo;
import com.edu.thss.smartdental.model.tooth.ToothChartView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener; 
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class Tooth2DFragment extends Fragment{

	private Bitmap template;
	private Bitmap tooth2D;
	private ArrayList <Integer> pCode, pCoor, pColored;
	private ArrayList <SDToothInfo> toothInfo;
	private ImageView toothView;
	private int currentSelectTooth, currentSelectColor;
	private FragmentManager fm = null; 
	private RadioGroup radioGroup; 
	
	Context context;
	
	public Tooth2DFragment(){
		
	}
	
	private int getCode(int color){
		int r = android.graphics.Color.red(color);
		int g = android.graphics.Color.green(color);
		int b = android.graphics.Color.blue(color);
		if (r == g && r % 8 == 0 && g % 8 == 0){
			if (b == 23)
				return (r / 8) + 1 + 32;
			if (b == 233)
				return (r / 8) + 1;
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
		pColored = new ArrayList <Integer>();
		for (int i = 0; i < w * h; ++i){
			int code = getCode(pixels[i]);
			if (code >= 0){
				pCoor.add(i+1);
				pCode.add(code);
			}
			if (code > 0 && code <= 32)
				pixels[i] = android.graphics.Color.rgb(255, 255, 255);
			if (code > 32)
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
	
	private int getColor(int code) {
		int w = tooth2D.getWidth();
		for (int i = 0; i < pCoor.size(); ++i){
			if (pCode.get(i) == code)
				return tooth2D.getPixel(pCoor.get(i) % w, pCoor.get(i) / w);
		}
		return -1;
	}
	
	private void clearToothColor(){
		int white = android.graphics.Color.rgb(255, 255, 255);
		for (int i = 0; i < pColored.size(); ++i){
			fill(pColored.get(i), white, toothView);
		}
		pColored.clear();
	}
	
	private void getToothInfoFromDB() {
		DBManager mgr = new DBManager(this.context);
		toothInfo = new ArrayList <SDToothInfo>();
		mgr.openDatabase();
		for (int i = 1; i <= 32; i++) {
			SDToothInfo tooth = mgr.queryToothByPosition(i).get(0);
			toothInfo.add(tooth);
		}
		mgr.closeDB();
	}
	
	private ArrayList <Integer> getTeethArrayByIllness(int... illCodeList) {
		ArrayList <Integer> illTeethList = new ArrayList <Integer>();
		for (int i = 1; i < toothInfo.size(); i++) {
			for (int illCode : illCodeList)
				if (toothInfo.get(i).diagnose == illCode) {
					illTeethList.add(toothInfo.get(i).position);
					break;
				}
		}
		return illTeethList;
	}
	
	private void fillToothByIllness(int... illCodeList) {
		ArrayList <Integer> illTeethList = getTeethArrayByIllness(illCodeList);
		int color = android.graphics.Color.rgb(255, 0, 0);
		
		clearToothColor();
		for (int i = 0; i < illTeethList.size(); i++) {
			fill(illTeethList.get(i), color, toothView);
			pColored.add(illTeethList.get(i));
		}
	}

	private void initRadioButton(RadioButton[] radioButton){
		String[] buttonText = new String[]{
			"牙齿图",
			"牙体缺损",
			"牙列缺损",
			"牙龈炎",
			"牙周炎",
			"牙髓炎",
			"龋齿",
			"未知牙病一",
			"未知牙病二",
			"未知牙病三",
			"全部"
		};
		for(int i=0; i<11; i++){
			radioButton[i]=(RadioButton) LayoutInflater.from(super.getActivity()).inflate(R.layout.radiobutton, null); 
			radioButton[i].setId(i+1);
			radioButton[i].setText(buttonText[i]);
			radioGroup.addView(radioButton[i]);
		}
	}
	
	private void setIllnessVisible(RadioButton[] radioButton, int[] illness){
		int width = getResources().getDisplayMetrics().widthPixels;
		for(int i=0; i<illness.length; i++){
			radioButton[illness[i]].setVisibility(View.VISIBLE);
			if(illness.length<4){
				radioButton[illness[i]].setWidth(width/illness.length);
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tooth_2d, container,false);
		toothView = (ImageView)rootView.findViewById(R.id.tooth_2d_img);
		radioGroup = (RadioGroup)rootView.findViewById(R.id.tooth_2d_tab);
		RadioButton[] radioButton = new RadioButton[11];
		
		this.context = rootView.getContext();
		
		InitImage(toothView);
		initRadioButton(radioButton);
		getToothInfoFromDB();
		int[] illness = new int[]{0,1,2,3,10};
		setIllnessVisible(radioButton, illness);

		
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
						if (code > 32)
							code -= 32;
						break;
					}
				}
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						if (code >= 0){
							currentSelectColor = getColor(code);
							int color = android.graphics.Color.rgb(160, 160, 160);
							fill(code, color, toothView);
							currentSelectTooth = code;
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if (code >= 0){
							if (code == currentSelectTooth) {
								break;
							}
							fill(currentSelectTooth, currentSelectColor, toothView);
							currentSelectColor = getColor(code);
							int color = android.graphics.Color.rgb(160, 160, 160);
							fill(code, color, toothView);
							currentSelectTooth = code;
						}
						else {
							fill(currentSelectTooth, currentSelectColor, toothView);
						}
						break;
					case MotionEvent.ACTION_UP:
						if (code >= 0){
							fill(code, currentSelectColor, toothView);
							showCustomDialog(code, "hello");
						}
						break;
				}
				return true;
			}
		});
		
		radioGroup.check(1);
		
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch(checkedId){
				case 1: clearToothColor(); break;
				case 2: fillToothByIllness(1); break;
				case 3: fillToothByIllness(2); break;
				case 4: fillToothByIllness(3); break;
				case 5: fillToothByIllness(4); break;
				case 6: fillToothByIllness(5); break;
				case 7: fillToothByIllness(6); break;
				case 8: fillToothByIllness(7); break;
				case 9: fillToothByIllness(8); break;
				case 10: fillToothByIllness(9); break;
				case 11: fillToothByIllness(10); break;
				
				}
               
			}} );
		
		return rootView;
	}
	
	protected void showCustomDialog(int code, String caseId) {
    	final Dialog dialog = new Dialog(super.getActivity());
    	final String currentCaseId = caseId;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        dialog.setContentView(R.layout.dialoglayout);  
        Button button1 = (Button)dialog.findViewById(R.id.dialog_tooth_myinfo);
        Button button2 = (Button)dialog.findViewById(R.id.dialog_tooth_intr);
        
        /*Add*/
        TextView number = (TextView)dialog.findViewById(R.id.dialog_tooth_code);
        number.setText(Integer.toString(code));
        
        /*add text*/
        DBManager mgr = new DBManager(super.getActivity());
        mgr.openDatabase();
        List<SDToothInfo> teeth = mgr.queryToothByPosition(code);
        String name = teeth.get(0).name;
        String state = teeth.get(0).state;
        String diagnose = "";
        
        if(teeth.get(0).diagnose == 0)
        	diagnose = "我是健康哒";
        else{
        	for(int i = 0; i < teeth.size(); i++){
        	diagnose += mgr.queryDiseaseById(teeth.get(i).diagnose).name;
        	if(i!=(teeth.size()-1))
        		diagnose += " ";  	
        	}
        }
        
        ((TextView) dialog.findViewById(R.id.dialog_tooth_anterior)).setText(name);
        ((TextView) dialog.findViewById(R.id.dialog_tooth_permanent)).setText(state);
        ((TextView) dialog.findViewById(R.id.dialog_tooth_mesialshift)).setText(diagnose);

		DisplayMetrics  dm = new DisplayMetrics();   
	    super.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);   
	    int screenWidth = dm.widthPixels;   
	    int screenHeight = dm.heightPixels;          
		
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		
		lp.x = (int) (screenWidth*0.05);
	    lp.y = (int) (screenWidth*0.05);
	    lp.width = (int) (screenWidth*0.9);
	    lp.height = (int) (screenHeight*0.8);
	    lp.alpha = 1.0f;

        dialogWindow.setAttributes(lp);
        
        button1.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
			}
		});
        button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Tooth2DFragment.this.getActivity(), OneImageActivity.class);
				Bundle bundle = new Bundle();
				bundle.putCharSequence("imageclass", currentCaseId);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		}); 
        dialog.show();  
    }
}

