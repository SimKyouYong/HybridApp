package co.kr.hybridapp.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import co.kr.hybridapp.R;

public class CustomDialog extends Dialog{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.3f;
		getWindow().setAttributes(lpWindow);

		setContentView(R.layout.custom_dialog);

		setLayout();
		setTitle(mTitle);
		setContent(mContent);
		setClickListener(mLeftClickListener , mRightClickListener);


	}

	public CustomDialog(Context context) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
	}

	public CustomDialog(Context context , String title , 
			View.OnClickListener singleListener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
		this.mTitle = title;
		this.mLeftClickListener = singleListener;
	}
	public CustomDialog(Context context , String title , String content , int align, String lb_name,
			View.OnClickListener leftListener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
		this.mTitle = title;
		this.mContent = content;
		this.mImgUrl = "";
		this.mAlign = align;
		this.mLeftTitle = lb_name;
		this.mLeftClickListener = leftListener;
	}	
	public CustomDialog(Context context , String title , String content , int align, String lb_name, String rb_name,
			View.OnClickListener leftListener ,	View.OnClickListener rightListener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
		this.mTitle = title;
		this.mContent = content;
		this.mImgUrl = "";
		this.mAlign = align;
		this.mLeftTitle = lb_name;
		this.mRightTitle = rb_name;
		this.mLeftClickListener = leftListener;
		this.mRightClickListener = rightListener;
	}
	public CustomDialog(Context context , String title , String content , String imgurl, int align, String lb_name, String rb_name,
			View.OnClickListener leftListener ,	View.OnClickListener rightListener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
		this.mTitle = title;
		this.mContent = content;
		this.mImgUrl = imgurl;
		this.mAlign = align;
		this.mLeftTitle = lb_name;
		this.mRightTitle = rb_name;
		this.mLeftClickListener = leftListener;
		this.mRightClickListener = rightListener;
	}	
	private void setTitle(String title){
		mTitleView.setText(title);
		mLeftButton.setText(mLeftTitle);
		mRightButton.setText(mRightTitle);
	}
	private void setContent(String content){
		mContentView.setText(content);
		mContentView.setGravity(mAlign);
		if(this.mImgUrl.equals("")){
			this.mImageView.setVisibility(View.GONE);
		}else{
			try{
				Bitmap msgbm = LoadImageFromWeb.downloadImage(this.mImgUrl);
				this.mImageView.setImageBitmap(msgbm);
			}catch(Exception e){}
		}		
	}

	private void setClickListener(View.OnClickListener left , View.OnClickListener right){
		if(left!=null && right!=null){
			mLeftButton.setOnClickListener(left);
			mRightButton.setOnClickListener(right);
			btn_close.setOnClickListener(right);
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

	private TextView mTitleView;
	private ImageView mImageView;
	private TextView mContentView;
	private Button mLeftButton;
	private Button mRightButton;
	private Button btn_close;
	private String mTitle;
	private String mLeftTitle,mRightTitle;
	private String mContent;
	private String mImgUrl;
	private int mAlign;

	private View.OnClickListener mLeftClickListener;
	private View.OnClickListener mRightClickListener;

	private void setLayout(){
		mTitleView = (TextView) findViewById(R.id.tv_title);
		mContentView = (TextView) findViewById(R.id.tv_content);
		mImageView = (ImageView) findViewById(R.id.tv_img);
		mLeftButton = (Button) findViewById(R.id.bt_left);
		mRightButton = (Button) findViewById(R.id.bt_right);
		btn_close = (Button) findViewById(R.id.btn_close);


		


	}
}









