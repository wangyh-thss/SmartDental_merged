package com.edu.thss.smartdental;
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
import android.app.Activity;
import android.content.SharedPreferences;

public class ToothFragment extends Fragment  {
	
	private FragmentManager fm = null; 
	private RadioGroup radioGroup;
	private String userName = "";
	public ToothFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tooth, container,false);
		userName = getActivity().getSharedPreferences("setting", android.content.Context.MODE_PRIVATE).getString("username", "");
		fm = getFragmentManager();
		radioGroup = (RadioGroup)rootView.findViewById(R.id.tooth_tab);
		radioGroup.check(R.id.tooth_tab_permanent);
		changeFragment(0);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch(checkedId){
				case R.id.tooth_tab_permanent: changeFragment(0); break;
				case R.id.tooth_tab_primary: changeFragment(1); break;
				case R.id.tooth_tab_2d: changeFragment(2); break;
				case R.id.tooth_tab_setting: changeFragment(3); break;
				}
               
			}} );
		
		return rootView;
	}

	private void changeFragment(int index){
		FragmentTransaction transaction = fm.beginTransaction();
		Fragment tempfragment = null;
		switch(index){
		case 0: tempfragment = new ToothPermanentFragment();break;
		case 1: tempfragment = new ToothPrimaryFragment();break;
		case 2: tempfragment = new Tooth2DFragment(userName);break;
		default: break;
		}
		if(tempfragment != null){
        	transaction.replace(R.id.tooth_tabcontent, tempfragment);
        	transaction.commit();
        }
	}
	

}
