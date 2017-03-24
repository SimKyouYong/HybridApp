package co.kr.hybridapp;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import co.kr.hybridapp.common.Check_Preferences;
import co.kr.hybridapp.common.SharedPreferencesUtil;


public class SettingActivity extends Activity {
	String[] arr={"on","off"};
	Spinner sp;
	TextView tx1;
	SharedPreferencesUtil spu;
	TextView logintext,deviceid,setup_version;
	ImageView push_aa1,push_aa2,push_bb1,push_bb2,img_exit;
	TelephonyManager tMgr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		setup_version=(TextView) findViewById(R.id.setup_version);
		push_aa1=(ImageView) findViewById(R.id.push_aa1);
		push_aa2=(ImageView) findViewById(R.id.push_aa2);
		
		
		setup_version.setText("1");

		tMgr = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		//deviceid.setText(tMgr.getDeviceId());

		Log.e("deviceid", tMgr.getDeviceId());
		
		
		if (!Check_Preferences.getAppPreferencesboolean(this, "pushEnable")) {
			//oFF
			push_aa1.setVisibility(View.INVISIBLE);
			push_aa2.setVisibility(View.VISIBLE);
		}else{
			//oN
			push_aa2.setVisibility(View.INVISIBLE);
			push_aa1.setVisibility(View.VISIBLE);
		}
		findViewById(R.id.setup_btn_exit).setOnClickListener(btnListener); 
		findViewById(R.id.push_aa1).setOnClickListener(btnListener); 
		findViewById(R.id.push_aa2).setOnClickListener(btnListener); 
		findViewById(R.id.ss_gonji).setOnClickListener(btnListener); 
		findViewById(R.id.ss_appinfo1).setOnClickListener(btnListener); 
		findViewById(R.id.ss_appinfo2).setOnClickListener(btnListener); 
		findViewById(R.id.ss_appinfo3).setOnClickListener(btnListener); 
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
				Check_Preferences.setAppPreferences(SettingActivity.this, "pushEnable", true);
				push_aa1.setVisibility(View.INVISIBLE);
				push_aa2.setVisibility(View.VISIBLE);
				break;
			case R.id.push_aa2:
				Check_Preferences.setAppPreferences(SettingActivity.this, "pushEnable", false);
				push_aa2.setVisibility(View.INVISIBLE);
				push_aa1.setVisibility(View.VISIBLE);
				break;
			case R.id.ss_gonji:
				setResult(130);
				finish();
				break;
			case R.id.ss_appinfo1:
				Log.e("SKY", "ss_appinfo1");
				setResult(131);
				finish();
				break;
			case R.id.ss_appinfo2:
				Log.e("SKY", "ss_appinfo2");
				setResult(132);
				finish();
				break;
			case R.id.ss_appinfo3:
				Log.e("SKY", "ss_appinfo3");
				setResult(133);
				finish();
				break;
			case R.id.ss_appinfo4:
				break;
			}
		}
	};


}
