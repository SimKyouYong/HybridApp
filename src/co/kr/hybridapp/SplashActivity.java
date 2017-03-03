package co.kr.hybridapp;


import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import co.kr.hybridapp.common.DEFINE;
import co.kr.sky.AccumThread;

public class SplashActivity extends Activity {
	Map<String, String> map = new HashMap<String, String>();
	AccumThread mThread;

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
		
		GCMRegistration_id();
		
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
		dialog = new Dialog(SplashActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		View v = LayoutInflater.from(SplashActivity.this).inflate(R.layout.progress_circle, null);       
		dialog.setContentView(v);
		//dialog.show();
		
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);

		final String regId = GCMRegistrar.getRegistrationId(context);
		Log.e("SKY", "registration id ===== "+regId);

		if (regId.equals("") || regId == null) {
			GCMRegistrar.register(context, DEFINE.GCM_ID);
		} else {
			reg_id = regId;
			Log.e("Already Registered",""+regId);
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
		//Server 전송 
		map.put("url", "http://snap40.cafe24.com/Hybrid/hybrid_register.php");
		map.put("reg_id", reg_id);
		map.put("type", "android");

		//스레드 생성 
		mThread = new AccumThread(this , mAfterAccum , map , 0 , 0 , null);
		mThread.start();		//스레드 시작!!
	}
	Handler mAfterAccum = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.arg1  == 0 ) {
				Intent i = new Intent(context, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				i.putExtra("openurl", "");
				startActivity(i);  
				
				SplashActivity.this.finish();	
			}
		}
	};
	@Override
 	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		if ( dialog != null)
			dialog.dismiss();
	}	
}
