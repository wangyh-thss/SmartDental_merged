package com.edu.thss.smartdental;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.Color;

import com.edu.thss.smartdental.db.DBManager;
import com.edu.thss.smartdental.model.general.SDDisease;
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
		if (code < 1 || code >  32)
			return;
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
			List<SDToothInfo> tooth = mgr.queryToothByPosition(i);
			for(int j = 0; j < tooth.size(); j++){
				toothInfo.add(tooth.get(j));
			}
		}
		mgr.closeDB();
	}
	
	private ArrayList <Integer> getTeethArrayByIllness(ArrayList <Integer> illCodeList) {
		ArrayList <Integer> illTeethList = new ArrayList <Integer>();
		for (int i = 0; i < toothInfo.size(); i++) {
			for (int illCode = 0; illCode < illCodeList.size(); illCode++)
				if (toothInfo.get(i).diagnose == illCodeList.get(illCode)-1) {
					illTeethList.add(toothInfo.get(i).position);
					break;
				}
		}

		return illTeethList;
	}
	
	private ArrayList <Integer> getDiagnoseArrayByIllness() {
		ArrayList <Integer> illCodeList = new ArrayList <Integer>();
		for (int i = 0; i < toothInfo.size(); i++) {
			if ((!illCodeList.contains(toothInfo.get(i).diagnose))&&(toothInfo.get(i).diagnose!=0)) {
				illCodeList.add(toothInfo.get(i).diagnose);
			}
		}
		
		return illCodeList;
	}
	
	private void fillToothByIllness(ArrayList <Integer> illCodeList) {
		ArrayList <Integer> illTeethList = getTeethArrayByIllness(illCodeList);
		int color = android.graphics.Color.rgb(0, 0, 255);
		if(illCodeList.get(0) == 1){
			clearToothColor();
			for(int i = 0; i < 32; i++){
				if(!illTeethList.contains(i)){
					fill(i, color, toothView);
					pColored.add(i);
				}
			}
		}
		else{
			clearToothColor();
			for (int i = 0; i < illTeethList.size(); i++) {
				fill(illTeethList.get(i), color, toothView);
				pColored.add(illTeethList.get(i));
			}
		}
	}

	private void initRadioButton(RadioButton[] radioButton, ArrayList <Integer> illCodeList){
		DBManager mgr = new DBManager(this.context);
		mgr.openDatabase();
		SDDisease disease = new SDDisease();
		String buttonText = new String();
		int width = getResources().getDisplayMetrics().widthPixels;
		radioButton[0]=(RadioButton) LayoutInflater.from(super.getActivity()).inflate(R.layout.radiobutton, null); 
		radioButton[0].setId(0);
		radioButton[0].setText("牙齿图");
		radioGroup.addView(radioButton[0]);
		
		if(illCodeList.size()<2){
			radioButton[0].setWidth(width/(illCodeList.size()+2));
			
		}
		for(int i=1; i<illCodeList.size()+1; i++){
			radioButton[i]=(RadioButton) LayoutInflater.from(super.getActivity()).inflate(R.layout.radiobutton, null); 
			radioButton[i].setId(illCodeList.get(i-1)+1);
			disease = mgr.queryDiseaseById(illCodeList.get(i-1));
			buttonText = disease.name;
			radioButton[i].setText(buttonText);
			radioGroup.addView(radioButton[i]);
			if(illCodeList.size()<2){
				radioButton[i].setWidth(width/(illCodeList.size()+2));
			}
		}
		radioButton[illCodeList.size()+1]=(RadioButton) LayoutInflater.from(super.getActivity()).inflate(R.layout.radiobutton, null); 
		radioButton[illCodeList.size()+1].setId(1);
		radioButton[illCodeList.size()+1].setText("全部");
		radioGroup.addView(radioButton[illCodeList.size()+1]);
		if(illCodeList.size()<2){
			radioButton[illCodeList.size()+1].setWidth(width/(illCodeList.size()+2));
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tooth_2d, container,false);
		toothView = (ImageView)rootView.findViewById(R.id.tooth_2d_img);
		radioGroup = (RadioGroup)rootView.findViewById(R.id.tooth_2d_tab);
		this.context = rootView.getContext();
		
		getToothInfoFromDB();
		InitImage(toothView);
		ArrayList <Integer> illCodeList = getDiagnoseArrayByIllness();
		RadioButton[] radioButton = new RadioButton[illCodeList.size()+2];
		initRadioButton(radioButton, illCodeList);
		
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
							currentSelectTooth = -1;
						}
						break;
					case MotionEvent.ACTION_UP:
						if (code >= 0){
							fill(code, currentSelectColor, toothView);
							currentSelectTooth = -1;
							
							DBManager mgr = new DBManager(context);
							mgr.openDatabase();
							SDToothInfo tooth = mgr.queryToothByPosition(code).get(0);
							mgr.closeDB();
							
							showCustomDialog(code, tooth.recordId);
						}
						break;
				}
				return true;
			}
		});
		
		radioGroup.check(0);
		
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				ArrayList <Integer> illCodeList = new ArrayList <Integer>();
				ArrayList <Integer> illCodeList_ = getDiagnoseArrayByIllness();
				if (checkedId == 0){
					clearToothColor();
				}
				else {
					
					illCodeList.add(checkedId);
					fillToothByIllness(illCodeList);
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

