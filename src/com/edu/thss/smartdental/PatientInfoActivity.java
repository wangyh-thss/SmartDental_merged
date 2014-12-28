package com.edu.thss.smartdental;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PatientInfoActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_info);
		Intent intent = getIntent();
		
		int patient_id = intent.getIntExtra("patient_id", -1);
		
	}
}
