package co.kr.hybridapp.common;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.os.Environment;
import android.util.DisplayMetrics;

public class CommonUtil {
	private static CommonUtil _instance;
	

	public String PHONE;
	public String PHONE_ID;
	
	

	static {
		_instance = new CommonUtil();
		try {								 
			_instance.PHONE = 	   		"";
			_instance.PHONE_ID = 	   		"";

			
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
}