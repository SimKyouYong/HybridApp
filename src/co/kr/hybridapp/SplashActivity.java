package co.kr.hybridapp;


import com.google.android.gcm.GCMRegistrar;

import co.kr.hybridapp.R;
import co.kr.hybridapp.common.DEFINE;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

public class SplashActivity extends Activity {
	
	public static String reg_id = null;
	public static Context context;
	
	Dialog dialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		context = this;
			
		SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
		reg_id = prefs.getString("device_id","");
		
		if(reg_id.equals("")){
			GCMRegistration_id();
		}else{
			start();
		}
	}
	private void start(){
    	Handler h = new Handler ();
    	h.postDelayed(new splashhandler(), DEFINE.SPLASH_TIME);
	}
	public class splashhandler implements Runnable{
		public void run(){
			
     		Intent i = new Intent(context, MainActivity.class);
    		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
    		i.putExtra("openurl", "");
    		startActivity(i);  
    		
			SplashActivity.this.finish();
		}
	}	
	public void GCMRegistration_id()
	{
		dialog = new Dialog(SplashActivity.this,R.style.NewDialog);
		View v = LayoutInflater.from(SplashActivity.this).inflate(R.layout.progress_circle, null);       
		dialog.setContentView(v);
		dialog.show();
		
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);

		final String regId = GCMRegistrar.getRegistrationId(context);
		//Log.i("GCM", "registration id ===== "+regId);

		if (regId.equals("") || regId == null) {
			GCMRegistrar.register(context, DEFINE.GCM_ID);
		} else {
			reg_id = regId;
			//Log.i("Already Registered",""+regId);
			start();
		}
	}
	public void init(){
		if ( dialog != null) dialog.dismiss();

		if(!reg_id.equals("")){
			SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
	        editor.putString("device_id", reg_id);
	        editor.commit();
		}
		
 		Intent i = new Intent(context, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		i.putExtra("openurl", "");
		startActivity(i);  
		
		SplashActivity.this.finish();		

	}
	@Override
 	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		if ( dialog != null)
			dialog.dismiss();
	}	
}
