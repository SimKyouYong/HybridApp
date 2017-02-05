package co.kr.hybridapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ShowMSGActivity extends Activity {

	Context context;
	String openurl;
	
	private CustomDialog mCustomDialog;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    requestWindowFeature(Window.FEATURE_NO_TITLE);      
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	    getWindow().setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));
	    
	    context = this;
	    
	    Intent i = getIntent();
	    String msg = i.getStringExtra("msg");
	    openurl=i.getStringExtra("openurl");
	    String imgurl=i.getStringExtra("imgurl");

		mCustomDialog = new CustomDialog(context, 
				getString(R.string.app_name),
				msg,
				imgurl,
				Gravity.CENTER,
				getString(R.string.check_now),
				getString(R.string.check_later),
				leftClickListener, 
				rightClickListener);
		mCustomDialog.show();
	}
	private View.OnClickListener leftClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

         		Intent i = new Intent(context, MainActivity.class);
        		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        		i.putExtra("openurl", openurl);
        		startActivity(i);             		
    		
        	mCustomDialog.dismiss();
        	ShowMSGActivity.this.finish();
		}
	};

	private View.OnClickListener rightClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			mCustomDialog.dismiss();
			ShowMSGActivity.this.finish();
		}
	};
}
