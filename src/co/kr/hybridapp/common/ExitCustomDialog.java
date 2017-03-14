package co.kr.hybridapp.common;

import com.android.volley.toolbox.NetworkImageView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import co.kr.hybridapp.R;
import co.kr.hybridapp.net.H5ImageLoader;

public class ExitCustomDialog extends Dialog{

	private Button mLeftButton;
	private Button mRightButton;
	private ImageView img;
	private int mAlign;
	private Context ac;
	private String url;
	private View.OnClickListener mLeftClickListener;
	private View.OnClickListener mRightClickListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.3f;
		getWindow().setAttributes(lpWindow);

		setContentView(R.layout.custom_dialog2);

		setLayout();
		setClickListener(mLeftClickListener , mRightClickListener);
		
		
	}

	public ExitCustomDialog(Context context) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
	}

	
	public ExitCustomDialog(Context context, String img,
			View.OnClickListener leftListener ,	View.OnClickListener rightListener) {
		super(context);
		this.url = img;
		this.ac = context;
		this.mLeftClickListener = leftListener;
		this.mRightClickListener = rightListener;
	}

	private void setClickListener(View.OnClickListener left , View.OnClickListener right){
		if(left!=null && right!=null){
			mLeftButton.setOnClickListener(left);
			mRightButton.setOnClickListener(right);
		}else if(left!=null && right==null){
			mLeftButton.setOnClickListener(left);
			mLeftButton.setBackgroundResource(R.drawable.tab_ok_click);
			mRightButton.setVisibility(View.GONE);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode==KeyEvent.KEYCODE_BACK){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}


	private void setLayout(){
		mLeftButton 		= (Button) findViewById(R.id.bt_left);
		mRightButton 		= (Button) findViewById(R.id.bt_right);
		img		 			= (NetworkImageView) findViewById(R.id.img);
		
		
		NetworkImageView image_exit = (NetworkImageView)findViewById(R.id.img);

		image_exit.setScaleType(ImageView.ScaleType.FIT_XY);
		H5ImageLoader.getInstance(ac).set(url, image_exit);
	}
}









