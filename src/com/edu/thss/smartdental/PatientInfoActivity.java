package com.edu.thss.smartdental;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class PatientInfoActivity extends FragmentActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_info);
		Intent intent = getIntent();
		FragmentManager fragmentManager = getSupportFragmentManager();
		String patient_username = intent.getStringExtra("patient_username");
		Fragment fragment = new Tooth2DFragment(patient_username);
		Log.i("UserName", patient_username);
		fragmentManager.beginTransaction().replace(R.id.patient_tooth, fragment).commit();
	}
}
