package co.kr.hybridapp.fregment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import co.kr.hybridapp.R;
import co.kr.hybridapp.SlideViewActivity;
import co.kr.hybridapp.common.FragmentEx;

public class SlideViewFregment extends FragmentEx implements OnTouchListener{
	private Context mContext;
	Activity av_;    //  액티비티 av_로 지정
	public SlideViewFregment(Context context , Activity av , int tag) {  //  액티비티 지정, 프래그먼트
		mContext = context;  
		av_ = av;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		Log.e("SKY" , "--MAIN START--");
		View view = inflater.inflate(R.layout.activity_slidefregment, null);
		SlideViewActivity.wc = (WebView)view.findViewById(R.id.webview);
//		view.findViewById(R.id.country_etc).setOnClickListener(mClickListener1);

		SlideViewActivity.wc.loadUrl(SlideViewActivity.SUB_URL);
		return view;

	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}