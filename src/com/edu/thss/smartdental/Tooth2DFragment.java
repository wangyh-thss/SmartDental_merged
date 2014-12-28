package com.edu.thss.smartdental;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.edu.thss.smartdental.db.DBManager;
import com.edu.thss.smartdental.model.general.SDDisease;
import com.edu.thss.smartdental.model.general.SDToothInfo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class Tooth2DFragment extends Fragment{

	private Bitmap template; // Template picture for locate
	private Bitmap tooth2D; // Picture for view

	private ImageView toothView; //Main imageview

	private ArrayList <Integer> pCode, pCoor; //Store the point set of every tooth
	private ArrayList <Integer> pColored; //Store the list of filled teeth

	private ArrayList <SDToothInfo> toothInfo; //Tooth infomation, from database

	private int currentSelectTooth, currentSelectColor; //Store the status
	private RadioGroup radioGroup;

	private Handler handler;
	private String getURL = "";
	private String tString = "";
	private DBManager mgr;

	Context context;

	public Tooth2DFragment(String userName){
		getURL = "http://166.111.80.119/userinfo/tooth?user=" + userName;
		handler = new Handler(){
		    @Override
		    public void handleMessage(Message msg) {
		        super.handleMessage(msg);
		        Bundle data = msg.getData();
		        String val = data.getString("result");
		    }
		};
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpClient client = new DefaultHttpClient();  
		        HttpGet get = new HttpGet(getURL);
		        
		        try {
					HttpResponse responseGet = client.execute(get);  
			        HttpEntity resEntityGet = responseGet.getEntity();  
			        if (resEntityGet != null) {
			        	tString = EntityUtils.toString(resEntityGet);
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}
		        Message message = new Message();
		        Bundle data = new Bundle();
		        data.putString("result", tString);
		        message.setData(data);
		        handler.sendMessage(message);
			}
		};
		new Thread(runnable).start();
	}

	/*
	 * Initialization functions
	 */

	/**
	 * Initialization of tooth picture
	 */
	private void initImage(){
		template = BitmapFactory.decodeResource(getResources(), R.drawable.tooth_2d);
		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;

		//Calculate scale
		if (1.0 * height / width < 2.0 / 1.3)
			width = (int)(height * 1.3 / 2.0);
		else
			height = (int)(width * 2.0 / 1.3);

		//Create bitmap
		tooth2D = Bitmap.createScaledBitmap(template, width, height, false);
		template = tooth2D.copy(tooth2D.getConfig(), true);
		int w = tooth2D.getWidth();
		int h = tooth2D.getHeight();
		int[] pixels = new int[w * h];
		tooth2D.getPixels(pixels, 0, w, 0, 0, w, h); //Get pixels

		pCoor = new ArrayList <Integer>();
		pCode = new ArrayList <Integer>();
		pColored = new ArrayList <Integer>();

		for (int i = 0; i < w * h; ++i){
			int code = getCodeByColor(pixels[i]);
			//Add point set
			if (code >= 0){
				pCoor.add(i);
				pCode.add(code);
			}

			//Modify pixels to grey or white
			if (code > 0 && code <= 32)
				pixels[i] = android.graphics.Color.rgb(255, 255, 255);
			if (code > 32)
				pixels[i] = android.graphics.Color.rgb(233, 233, 233);
		}

		tooth2D.setPixels(pixels, 0, w, 0, 0, w, h);
		toothView.setImageBitmap(tooth2D);
	}


	/*
	 * Process functions
	 */

	/***************************
	 * Get tooth code from color
	 * @param color
	 * @return specified code
	 */
	private int getCodeByColor(int color){
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

	/**
	 * Get tooth code from coor
	 * @param coor
	 * @return specified code
	 */
	private int getCodeByCoor(int coor){
		int code = -1;
		for (int i = 0; i < pCoor.size(); ++i){
			if (pCoor.get(i) == coor){
				code = pCode.get(i);
				if (code > 32)
					code -= 32;
				break;
			}
		}
		return code;
	}


	/**
	 * Get current color by code
	 * @param code
	 * @return current color
	 */
	private int getColor(int code) {
		int w = tooth2D.getWidth();
		for (int i = 0; i < pCoor.size(); ++i){
			if (pCode.get(i) == code)
				return tooth2D.getPixel(pCoor.get(i) % w, pCoor.get(i) / w);
		}
		return -1;
	}

	/**
	 * Fill specified tooth by code
	 * @param code    tooth code
	 * @param color   fill color
	 */
	private void fillToothColor(int code, int color){
		int w = tooth2D.getWidth();
		for (int i = 0; i < pCoor.size(); ++i){
			if (pCode.get(i) == code)
				tooth2D.setPixel(pCoor.get(i) % w, pCoor.get(i) / w, color);
		}
		toothView.setImageBitmap(tooth2D);
	}

	/**
	 * Clear all teeth's fill color
	 */
	private void clearToothColor(){
		int white = android.graphics.Color.rgb(255, 255, 255);
		for (int i = 0; i < pColored.size(); ++i){
			fillToothColor(pColored.get(i), white);
		}
		pColored.clear();
	}

	/*
	 * ???Fill the teeth which suffer the specified illness(es)
	 */
	private void fillToothByIllness(ArrayList <Integer> illCodeList) {
		ArrayList <Integer> illTeethList = getTeethArrayByIllness(illCodeList);
		int color = android.graphics.Color.rgb(0, 0, 255);
		clearToothColor();

		if(illCodeList.get(0) == 1){
			for(int i = 0; i < 32; i++){
				if(!illTeethList.contains(i)){
					fillToothColor(i, color);
					pColored.add(i);
				}
			}
		}
		else{
			for (int i = 0; i < illTeethList.size(); i++) {
				fillToothColor(illTeethList.get(i), color);
				pColored.add(illTeethList.get(i));
			}
		}
	}

	/**
	 * Get list of teeth which suffer the specified illness
	 * @param illCodeList    list of code of ill teeth
	 * @return
	 */
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

	/**
	 * Get list of diagnoses by illness
	 */
	private ArrayList <Integer> getDiagnoseArrayByIllness() {
		ArrayList <Integer> illCodeList = new ArrayList <Integer>();
		for (int i = 0; i < toothInfo.size(); i++) {
			if ((!illCodeList.contains(toothInfo.get(i).diagnose))&&(toothInfo.get(i).diagnose!=0)) {
				illCodeList.add(toothInfo.get(i).diagnose);
			}
		}
		return illCodeList;
	}

	/*
	 * Database interface
	 */

	/**
	 * Get tooth information from database
	 */
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

	/*
	 * ???Initialization of the bottom bar
	 */
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

		//Initialization of widgets
		View rootView = inflater.inflate(R.layout.tooth_2d, container,false);
		toothView = (ImageView)rootView.findViewById(R.id.tooth_2d_img);
		radioGroup = (RadioGroup)rootView.findViewById(R.id.tooth_2d_tab);
		radioGroup.check(0);

		//Read database
		getToothInfoFromDB();

		this.context = rootView.getContext();
		initImage();
		ArrayList <Integer> illCodeList = getDiagnoseArrayByIllness();
		RadioButton[] radioButton = new RadioButton[illCodeList.size()+2];
		initRadioButton(radioButton, illCodeList);

		mgr = new DBManager(this.context);
	    mgr.openDatabase();
	    
	    try {
			mgr.updateDatabase(tString);
			mgr.closeDB();
			} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Touch action handler
		toothView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//Transform from the screen coordinate to picture coordinate
				int imageW = tooth2D.getWidth();
				int imageH = tooth2D.getHeight();
				int viewW = toothView.getWidth();
				int viewH = toothView.getHeight();
				double rateW = 1.0 * imageW / viewW;
				double rateH = 1.0 * imageH / viewH;
				double rate = Math.max(rateW, rateH);

				int x = (int)((event.getX() - viewW / 2) * rate + imageW / 2);
				int y = (int)((event.getY() - viewH / 2) * rate + imageH / 2);
				if (x < 0 || x >= imageW || y < 0 || y >= imageH)
					return false;

				int code = getCodeByCoor(y * imageW + x);

				//Action handler
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						if (code >= 0){
							//Store the current status
							currentSelectColor = getColor(code);
							currentSelectTooth = code;
							int color = android.graphics.Color.rgb(160, 160, 160);
							fillToothColor(code, color);
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if (code >= 0){
							if (code == currentSelectTooth) {
								break;
							}
							//Restore the status
							fillToothColor(currentSelectTooth, currentSelectColor);
							currentSelectColor = getColor(code);
							int color = android.graphics.Color.rgb(160, 160, 160);
							fillToothColor(code, color);
							currentSelectTooth = code;
						}
						else {
							fillToothColor(currentSelectTooth, currentSelectColor);
						}
						break;
					case MotionEvent.ACTION_UP:
						if (code >= 0){
							fillToothColor(code, currentSelectColor);

							//???
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

		//Radio action handler
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