package co.kr.hybridapp;


import java.util.HashMap;
import java.util.Map;

import com.google.android.gcm.GCMRegistrar;

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
import co.kr.hybridapp.common.ActivityEx;
import co.kr.hybridapp.common.DEFINE;
import co.kr.sky.AccumThread;

public class SplashActivity extends ActivityEx {
	Map<String, String> map = new HashMap<String, String>();
	AccumThread mThread;

	public static String reg_id = null;
	public static Context context;
	LocationManager myLocationManager;
	Dialog dialog;
	int i = 0;
	@Override
	public void onResume() {
		super.onResume();
		if (i != 0 ) {
			Log.e("SKY" , "i :: " + i );
			//start();
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		if (!checkNetwordState()) {
			//인터넷이 꺼져있으면 finish();
			AlertDialog.Builder ab = new AlertDialog.Builder(this , AlertDialog.THEME_HOLO_LIGHT);
			ab.setMessage("인터넷이 off");
			ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					finish();
					return;
				}
			})
			.show();
			return;
		}
		if (myLocationManager == null) {
			myLocationManager = (LocationManager)getSystemService(
	        		Context.LOCATION_SERVICE);
		}

		context = this;
		
		SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
		reg_id = prefs.getString("device_id","");
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				GCMRegistration_id();
			}
		}, 500);// 0.5초 정도 딜레이를 준 후 시작
		
		
	}
	private void start(){
		i = 1;
		//GPS 확인
		if (DEFINE.GPS_SWICH) {
			//체크해서 알럿 띄우기
			// 시스템 > 설정 > 위치 및 보안 > GPS 위성 사용 여부 체크.
			Boolean isGpsEnabled = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			Log.e("SKY" , "isGpsEnabled :: " + isGpsEnabled);
			if (!isGpsEnabled) {
				alertCheckGPS();
			}else{
				Handler h = new Handler ();
		    	h.postDelayed(new splashhandler(), DEFINE.SPLASH_TIME);
			}
		}else{
			
		}
    	
	}
	private void alertCheckGPS() {
			
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this , AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setMessage("원활한 서비스를 위해\nGPS를 활성화를 부탁 드립니다.");
        builder.setCancelable(false);
        builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveConfigGPS();
                            }
                    });
        builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            	Handler h = new Handler ();
                            	h.postDelayed(new splashhandler(), DEFINE.SPLASH_TIME);
                                dialog.cancel();
                            }
                    });

        AlertDialog alert = builder.create();
        alert.show();
    }

    // GPS 설정화면으로 이동
    private void moveConfigGPS() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(gpsOptionsIntent , 1);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	Log.e("SKY" , "resultCode :: " + resultCode);
    	Log.e("SKY" , "requestCode :: " + requestCode);
    	switch (requestCode) {
    	   case 1:
    		   start();
    	   break;

    	default:
    	   break;
    	}
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

		//if ( dialog != null)
			//dialog.dismiss();
	}	
}
