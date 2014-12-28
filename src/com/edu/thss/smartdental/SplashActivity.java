/*
 * Author: Zhang Kai
*/

package com.edu.thss.smartdental;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;  
import android.os.Handler;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 3000;  
	  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_splash);  
  
        new Handler().postDelayed(new Runnable() {  
            public void run() {  
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);  
                SplashActivity.this.startActivity(intent);  
                SplashActivity.this.finish();  
            }  
        }, SPLASH_DISPLAY_LENGHT);  
  
    }  
}