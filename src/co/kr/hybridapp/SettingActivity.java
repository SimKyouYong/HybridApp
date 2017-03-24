package co.kr.hybridapp;



import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import co.kr.hybridapp.common.SharedPreferencesUtil;
import co.kr.hybridapp.net.NetWork;


public class SettingActivity extends Activity {
	String[] arr={"on","off"};
	Spinner sp;
	TextView tx1;
	SharedPreferencesUtil spu;
	TextView logintext,deviceid,setup_version;
	ImageView aa1,aa2,bb1,bb2,img_exit;
	TelephonyManager tMgr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		spu=new SharedPreferencesUtil(getApplicationContext());

		setup_version=(TextView) findViewById(R.id.setup_version);
		aa1=(ImageView) findViewById(R.id.push_aa1);
		aa2=(ImageView) findViewById(R.id.push_aa2);
		bb1=(ImageView) findViewById(R.id.push_bb1);
		bb2=(ImageView) findViewById(R.id.push_bb2);
		//deviceid=(TextView) findViewById(R.id.ss_deviceid);
		//logintext=(TextView) findViewById(R.id.ss_logintext);
		
		
		setup_version.setText(spu.getValue("version", "1"));
		imgexe();

		Log.e("SKY" , "LOGINE VAL ::" + spu.getValue("islogin", 0));
		tMgr = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		//deviceid.setText(tMgr.getDeviceId());

		Log.e("deviceid", tMgr.getDeviceId());
		

		findViewById(R.id.setup_btn_exit).setOnClickListener(btnListener); 
		findViewById(R.id.push_aa1).setOnClickListener(btnListener); 
		findViewById(R.id.push_aa2).setOnClickListener(btnListener); 
		findViewById(R.id.push_bb1).setOnClickListener(btnListener); 
		findViewById(R.id.ss_gonji).setOnClickListener(btnListener); 
		findViewById(R.id.ss_appinfo1).setOnClickListener(btnListener); 
		findViewById(R.id.ss_appinfo4).setOnClickListener(btnListener); 

	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.setup_btn_exit:
				finish();
				break;
			case R.id.push_aa1:
				spu.put("aa", 0);
				try{	
					NetWork nw=new NetWork("http://tbu.co.kr/push/gcm_type1_off.php",getApplicationContext());
					ArrayList<NameValuePair> nn=new ArrayList<NameValuePair>();
					nn.add(new BasicNameValuePair("phone_id", tMgr.getDeviceId()));
					nn.add(new BasicNameValuePair("pnum", tMgr.getLine1Number()));
					nn.add(new BasicNameValuePair("reg_id", spu.getValue("reg_id", "0")));
					nw.getJSONArrayByPOST(nn);
				}
				catch(Exception e){
				}
				aa1.setVisibility(View.INVISIBLE);
				aa2.setVisibility(View.VISIBLE);
				break;
			case R.id.push_aa2:
				spu.put("aa", 1);
				try{	
					NetWork nw=new NetWork("http://tbu.co.kr/push/gcm_type1_on.php",getApplicationContext());
					ArrayList<NameValuePair> nn=new ArrayList<NameValuePair>();
					nn.add(new BasicNameValuePair("phone_id", tMgr.getDeviceId()));
					nn.add(new BasicNameValuePair("pnum", tMgr.getLine1Number()));
					nn.add(new BasicNameValuePair("reg_id", spu.getValue("reg_id", "0")));
					nw.getJSONArrayByPOST(nn);
				}
				catch(Exception e){
				}
				aa2.setVisibility(View.INVISIBLE);
				aa1.setVisibility(View.VISIBLE);
				break;
			case R.id.push_bb1:
				spu.put("bb", 0);
				try{	
					NetWork nw=new NetWork("http://tbu.co.kr/push/gcm_type2_off.php",getApplicationContext());
					ArrayList<NameValuePair> nn=new ArrayList<NameValuePair>();
					nn.add(new BasicNameValuePair("phone_id", tMgr.getDeviceId()));
					nn.add(new BasicNameValuePair("pnum", tMgr.getLine1Number()));
					nn.add(new BasicNameValuePair("reg_id", spu.getValue("reg_id", "0")));
					nw.getJSONArrayByPOST(nn);
				}
				catch(Exception e){

				}
				bb1.setVisibility(View.INVISIBLE);
				bb2.setVisibility(View.VISIBLE);
				break;
			case R.id.push_bb2:
				spu.put("bb",1);
				try{	
					NetWork nw=new NetWork("http://tbu.co.kr/push/gcm_type2_on.php",getApplicationContext());
					ArrayList<NameValuePair> nn=new ArrayList<NameValuePair>();
					nn.add(new BasicNameValuePair("phone_id", tMgr.getDeviceId()));
					nn.add(new BasicNameValuePair("pnum", tMgr.getLine1Number()));
					nn.add(new BasicNameValuePair("reg_id", spu.getValue("reg_id", "0")));
					nw.getJSONArrayByPOST(nn);
				}
				catch(Exception e){

				}
				bb2.setVisibility(View.INVISIBLE);
				bb1.setVisibility(View.VISIBLE);
				break;
			case R.id.ss_gonji:
				setResult(130);
				finish();
				break;
			case R.id.ss_appinfo1:
				setResult(131);
				finish();
				break;
			case R.id.ss_appinfo2:
				setResult(132);
				finish();
				break;
			case R.id.ss_appinfo3:
				setResult(133);
				finish();
				break;
			case R.id.ss_appinfo4:
				break;
			}
		}
	};
	private void imgexe() {
		// TODO Auto-generated method stub
		if(spu.getValue("aa", 1)==1){
			aa1.setVisibility(View.VISIBLE);
			aa2.setVisibility(View.INVISIBLE);
		}
		if(spu.getValue("aa", 1)==0){
			aa2.setVisibility(View.VISIBLE);
			aa1.setVisibility(View.INVISIBLE);
		}
		if(spu.getValue("bb", 1)==1){
			bb1.setVisibility(View.VISIBLE);
			bb2.setVisibility(View.INVISIBLE);
		}
		if(spu.getValue("bb", 1)==0){
			bb2.setVisibility(View.VISIBLE);
			bb1.setVisibility(View.INVISIBLE);
		}



	}


}
