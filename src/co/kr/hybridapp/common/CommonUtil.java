package co.kr.hybridapp.common;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

public class CommonUtil {
	private static CommonUtil _instance;
	

	public String PHONE;
	public String PHONE_ID;
	public String address;
	public double latitude = 0;
	public double longitude=0;
	

	static {
		_instance = new CommonUtil();
		try {								 
			_instance.PHONE = 	   		"";
			_instance.PHONE_ID = 	   		"";
			_instance.address = 	   		"";
			_instance.latitude = 	   		0;
			_instance.longitude = 	   		0;

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static CommonUtil getInstance() {
		return _instance;
	}

	
	public ArrayList<String> Token_String(String url , String token){
		ArrayList<String> Obj = new ArrayList<String>();

		StringTokenizer st1 = new StringTokenizer( url , token);
		while(st1.hasMoreTokens()){
			Obj.add(st1.nextToken());
		}
		return Obj;
	}
	public boolean paget(String s , Activity ac) {
		Log.e("SKY" , "S  :: " + s);
		Boolean ispass = false;
		try {
			if (new DataSync(ac).execute(s).get().equals("200")) {
				Log.e("SKY" , "S0  :: ");
				// progressDialog=MyProgressDialog.show(WebViewActivity.this,"","",true,true,null);
				ispass = true;
			} else {
				Log.e("SKY" , "S1  :: ");
				ispass = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("SKY" , "S2  :: ");
			Toast.makeText(ac, "네트워크 상태가 불안정합니다.",
					1000).show();
			ispass = false;
			e.printStackTrace();
		}
		return ispass;
	}
}
