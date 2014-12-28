/*
 * Author: Zhang Kai
*/

package com.edu.thss.smartdental;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;

public class AddManyFragment extends Fragment {
	
	Button add_many_btn;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_many, container, false);
		add_many_btn = (Button)rootView.findViewById(R.id.add_many_btn);
		add_many_btn.setOnClickListener(addListener);
		return rootView;
	}

	private OnClickListener addListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
		    intent.setType("*/*"); 
		    intent.addCategory(Intent.CATEGORY_OPENABLE);
	        startActivityForResult(Intent.createChooser(intent, "打开文件"), 0);
		}
	};
	
}
