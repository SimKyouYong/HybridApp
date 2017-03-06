package co.kr.hybridapp;

import com.android.volley.toolbox.NetworkImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import co.kr.hybridapp.net.H5ImageLoader;

public class ShowIMGActivity extends Activity {

	Context context;
	String openurl;


	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		
		Intent i = getIntent();
		String msg = i.getStringExtra("msg");
		openurl=i.getStringExtra("openurl");
		String imgurl=i.getStringExtra("imgurl");

		//requestWindowFeature(Window.FEATURE_NO_TITLE);      
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		
		
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		//getWindow().setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));

		setContentView(R.layout.dialog);
		
		NetworkImageView image_exit = (NetworkImageView)findViewById(R.id.img_exit);

		image_exit.setScaleType(ImageView.ScaleType.FIT_XY);
		H5ImageLoader.getInstance(context).set(i.getStringExtra("imgurl"), image_exit);


		//버튼 리스너 설정 
		findViewById(R.id.exit).setOnClickListener(btnListener);
		findViewById(R.id.img_exit).setOnClickListener(btnListener);
	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.exit:	
				Log.e("SKY"  , "--exit--");
				finish();
				break;
			case R.id.img_exit:	
				Log.e("SKY"  , "--img_exit--");
				Intent i = new Intent(context, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				i.putExtra("openurl", openurl);
				startActivity(i);   
				break;
			}
		}
	};

}
