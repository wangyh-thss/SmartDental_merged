package com.edu.thss.smartdental;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ClassFragment extends Fragment {
	
	Button ok;
   
	private static final int REQUEST_CODE = 0; //«Î«Û¬Î
	
	public ClassFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_class, container,false);
		this.ok = (Button) rootView.findViewById(R.id.class_ok);
		
		ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(getActivity(),MyTestActivity.class);
				intent.putExtra("data",ok.getText() );
				startActivityForResult(intent,REQUEST_CODE);
			}});
		
		//intent.putExtra("data",ok.getText() );
		return rootView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE&&resultCode == MyTestActivity.RESULT_CODE){
			String result = data.getExtras().getString("result-data");
		}
	}
}
