package com.edu.thss.smartdental;

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

	private FragmentManager fm = null; 
	private RadioGroup radioGroup;
	public Tooth2DFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tooth_2d, container,false);
		fm = getFragmentManager();
		radioGroup = (RadioGroup)rootView.findViewById(R.id.tooth_2d_tab);
		radioGroup.check(R.id.tooth_2d_tab_illness1);
		changeFragment(0);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch(checkedId){
				case R.id.tooth_2d_tab_illness1: changeFragment(0); break;
				case R.id.tooth_2d_tab_illness2: changeFragment(1); break;
				case R.id.tooth_2d_tab_illness3: changeFragment(2); break;
				case R.id.tooth_2d_tab_illness4: changeFragment(3); break;
				}
               
			}} );
		
		return rootView;
	}

	private void changeFragment(int index){
		FragmentTransaction transaction = fm.beginTransaction();
		Fragment tempfragment = null;
		switch(index){
		case 0: tempfragment = new Tooth2DIllness1Fragment();break;
		case 1: tempfragment = new Tooth2DIllness2Fragment();break;
		case 2: tempfragment = new Tooth2DIllness3Fragment();break;
		case 3: tempfragment = new Tooth2DIllness4Fragment();break;
		default: break;
		}
		if(tempfragment != null){
        	transaction.replace(R.id.tooth_tabcontent1, tempfragment);
        	transaction.commit();
        }
	}
	
}

