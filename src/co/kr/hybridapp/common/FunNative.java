package co.kr.hybridapp.common;

import java.io.UnsupportedEncodingException;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.exception.KakaoException;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import co.kr.hybridapp.LocationSetting;
import co.kr.hybridapp.Login;
import co.kr.hybridapp.MainActivity;
import co.kr.hybridapp.R;
import co.kr.hybridapp.R.string;
import co.kr.hybridapp.SKRoute;
import co.kr.hybridapp.SettingActivity;
import co.kr.hybridapp.SlideViewActivity;
import co.kr.hybridapp.SubNotActivity;



public class FunNative  {

	CommonUtil dataSet = CommonUtil.getInstance();
	private WebView Webview_copy;


	public void kakaoshare(String url , Activity ac , WebView vc , String return_fun) throws KakaoParameterException{
		Log.e("SKY" , "--kakaoshare-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		KakaoLinkData kakaoLinkData;
		kakaoLinkData = KakaoLinkData.getInstanceForShareBibleVerse("");
		try {
			KakaoLink kakaoLink = KakaoLink.getKakaoLink(ac);
			KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
			kakaoTalkLinkMessageBuilder.addText(kakaoLinkData.getText());
			if (kakaoLinkData.hasImage()) {
				kakaoTalkLinkMessageBuilder.addImage(kakaoLinkData.getImageSrc(), 100, 100);
			}
			if (kakaoLinkData.hasWebButton()) {
				kakaoTalkLinkMessageBuilder.addWebButton(kakaoLinkData.getButtonText(), kakaoLinkData.getButtonUrl());
			}
			if (kakaoLinkData.hasWebLink()) {
				kakaoTalkLinkMessageBuilder.addWebLink(kakaoLinkData.getLinkText(), kakaoLinkData.getLinkUrl());
			}
			kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, ac);
		} catch (KakaoParameterException e) {
		}

	}
	public void LocationSetting(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--LocationSetting-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}

		//인텐트 태우기
		Intent it = new Intent(ac, LocationSetting.class);
		ac.startActivityForResult(it , DEFINE.REQ_LOCATION);

	}
	public void startSetting(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--startSetting-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}

		//인텐트 태우기
		Intent it = new Intent(ac, SettingActivity.class);
		ac.startActivityForResult(it , 120);

	}
	public void startNavi(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--startNavi-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}

		//인텐트 태우기
		Intent it = new Intent(ac, SKRoute.class);
		it.putExtra("startX", Double.parseDouble(val[0]));
		it.putExtra("startY", Double.parseDouble(val[1]));
		it.putExtra("endX", Double.parseDouble(val[2]));
		it.putExtra("endY", Double.parseDouble(val[3]));
		ac.startActivity(it);

	}
	public void LoginActivity(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--LoginActivity-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}

		//인텐트 태우기
		Intent it = new Intent(ac, Login.class);
		ac.startActivityForResult(it, 9000);

	}
	public void ProgressBar3(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--ProgressBar3-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		if (val[1].equals("true")) {
			Check_Preferences.setAppPreferences(ac, "PROGRESSBAR_3", true);
		}else{
			Check_Preferences.setAppPreferences(ac, "PROGRESSBAR_3", false);
		}
		Log.e("SKY", "RETURN :: " + "javascript:"+return_fun + "('" + Check_Preferences.getAppPreferencesboolean(ac, "PROGRESSBAR_3") + "')" );
		vc.loadUrl("javascript:"+return_fun + "('" + Check_Preferences.getAppPreferencesboolean(ac, "PROGRESSBAR_3") + "')");
	}

	public void ProgressBar(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--showToast-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		if (val[1].equals("true")) {
			Check_Preferences.setAppPreferences(ac, "PROGRESSBAR", true);
		}else{
			Check_Preferences.setAppPreferences(ac, "PROGRESSBAR", false);
		}
		Log.e("SKY", "RETURN :: " + "javascript:"+return_fun + "('" + Check_Preferences.getAppPreferencesboolean(ac, "PROGRESSBAR") + "')" );
		vc.loadUrl("javascript:"+return_fun + "('" + Check_Preferences.getAppPreferencesboolean(ac, "PROGRESSBAR") + "')");
	}


	/*
	 * param 
	 * url :: 안씀 
	 * name :: 띄울 메시지
	 * return :: 안씀
	 * window.location.href = "js2ios://SETPopExit_TYPE1?url=null&name=true&return=GETPopExit_TYPE1";
	 * */
	public void SETPOPEXIT_TYPE1(String url , final Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "-SETPOPEXIT_TYPE1-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		Log.e("SKY", "VAL[1] :: " + val[1] );
		if (val[1].equals("true")) {
			Check_Preferences.setAppPreferences(ac, "SETPOPEXIT_TYPE1" , "true");
		}else{
			Check_Preferences.setAppPreferences(ac, "SETPOPEXIT_TYPE1" , "false");
		}
		Log.e("SKY", "RETURN :: " + "javascript:"+return_fun + "('" + Check_Preferences.getAppPreferences(ac, "SETPOPEXIT_TYPE1") + "')" );
		vc.loadUrl("javascript:"+return_fun + "('" + Check_Preferences.getAppPreferences(ac, "SETPOPEXIT_TYPE1") + "')");
	}
	public void SETPOPEXIT_TYPE2(String url , final Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "-SETPOPEXIT_TYPE2-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		Log.e("SKY", "VAL[1] :: " + val[1] );
		if (val[1].equals("true")) {
			Check_Preferences.setAppPreferences(ac, "SETPOPEXIT_TYPE1" , "true");
		}else{
			Check_Preferences.setAppPreferences(ac, "SETPOPEXIT_TYPE1" , "false");
		}
		Log.e("SKY", "RETURN :: " + "javascript:"+return_fun + "('" + Check_Preferences.getAppPreferences(ac, "SETPOPEXIT_TYPE1") + "')" );
		vc.loadUrl("javascript:"+return_fun + "('" + Check_Preferences.getAppPreferences(ac, "SETPOPEXIT_TYPE1") + "')");
	}

	/*
	 * param 
	 * url :: 안씀 
	 * name :: 띄울 메시지
	 * return :: 안씀
	 * window.location.href = "js2ios://SETEXIT_TYPE?url=null&name=true&return=weblocation";
	 * */
	public void SETEXIT_TYPE1(String url , final Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "-SETEXIT_TYPE1-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		Log.e("SKY", "VAL[1] :: " + val[1] );
		if (val[1].equals("true")) {
			Check_Preferences.setAppPreferences(ac, "SETEXIT_TYPE" , "true");
		}else{
			Check_Preferences.setAppPreferences(ac, "SETEXIT_TYPE" , "false");
		}
		Log.e("SKY", "RETURN :: " + "javascript:"+return_fun + "('" + Check_Preferences.getAppPreferences(ac, "SETEXIT_TYPE") + "')" );
		vc.loadUrl("javascript:"+return_fun + "('" + Check_Preferences.getAppPreferences(ac, "SETEXIT_TYPE") + "')");
	}
	public void SETEXIT_TYPE2(String url , final Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "-SETEXIT_TYPE2-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		Log.e("SKY", "VAL[1] :: " + val[1] );
		if (val[1].equals("true")) {
			Check_Preferences.setAppPreferences(ac, "SETEXIT_TYPE" , "true");
		}else{
			Check_Preferences.setAppPreferences(ac, "SETEXIT_TYPE" , "false");
		}
		Log.e("SKY", "RETURN :: " + "javascript:"+return_fun + "('" + Check_Preferences.getAppPreferences(ac, "SETEXIT_TYPE") + "')" );
		vc.loadUrl("javascript:"+return_fun + "('" + Check_Preferences.getAppPreferences(ac, "SETEXIT_TYPE") + "')");
	}

	public void GetPhoneNumber(String url , final Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "-GetPhoneNumber-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		vc.loadUrl("javascript:"+"setPhoneNumber" + "('" + dataSet.PHONE + "')");
	}
	public void GetPhoneId(String url , final Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "-GetPhoneId-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		vc.loadUrl("javascript:"+"setDeviceidNumber" + "('" + dataSet.PHONE_ID + "')");
	}
	/*
	 * param 
	 * url :: 이동 url 주소 
	 * title :: 타이틀명 
	 * action :: 애니메이션 (예 : 시작점이 왼쪽 : 왼쪽에서 -> 오른쪽 , 오른쪽 : 오른쪽-> 왼쪽)
	 * new    :: 1 : N 이미지 활성화 , 0 : 비활성화
	 * button :: 버튼 텍스트값
	 * button_url :: 버튼 url 주소
	 * window.location.href = "js2ios://SubNotActivity?url=urladdress&title=타이틀명&action=left&new=1&button=로그인&button_url=http://snap40.cafe24.com";
	 * */
	public void SubNotActivity(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--SubNotActivity-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}

		//인텐트 태우기
		Intent it = new Intent(ac, SubNotActivity.class);
		it.putExtra("SUB_URL", val[0]);
		it.putExtra("TITLE", val[1]);
		//it.putExtra("NEW", val[2]);
		it.putExtra("NEW", val[3]);
		it.putExtra("BUTTON", val[4]);
		it.putExtra("BUTTON_URL", return_fun);
		ac.startActivity(it);
		if (val[2] . equals("top")) {
			ac.overridePendingTransition(R.drawable.anim_slide_in_top, R.drawable.anim_slide_out_bottom);  // 위에서 -> 아래
		}else if(val[2] . equals("right")){
			ac.overridePendingTransition(R.drawable.anim_slide_in_left, R.drawable.anim_slide_out_right);  // ->
		}else if(val[2] . equals("bottom")){
			ac.overridePendingTransition(R.drawable.anim_slide_in_bottom, R.drawable.anim_slide_out_top);  // 아래 -> 위로
		}else if(val[2] . equals("left")){
			ac.overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left);  // <-
		}else if(val[2] . equals("new")){
			ac.overridePendingTransition(R.drawable.anim_slide_in_new, R.drawable.anim_slide_out_new);  // <-
		}else{
			ac.overridePendingTransition(0, 0);  // <-

		}
		Check_Preferences.setAppPreferences(ac, "SUB_URL" , val[0]);
		Check_Preferences.setAppPreferences(ac, "TITLE" ,  	val[1]);
		Check_Preferences.setAppPreferences(ac, "NEW" ,  	val[3]);
		Check_Preferences.setAppPreferences(ac, "BUTTON" ,  val[4]);
		Check_Preferences.setAppPreferences(ac, "BUTTON_URL" , return_fun);

	}
	/*
	 * param 
	 * url :: 이동 url 주소 
	 * title :: 타이틀명 
	 * action :: 애니메이션 (예 : 시작점이 왼쪽 : 왼쪽에서 -> 오른쪽 , 오른쪽 : 오른쪽-> 왼쪽)
	 * new    :: 1 : N 이미지 활성화 , 0 : 비활성화
	 * button :: 버튼 텍스트값
	 * button_url :: 버튼 url 주소
	 * window.location.href = "js2ios://SubActivity?url=urladdress&title=타이틀명&action=left&new=1&button=로그인&button_url=http://snap40.cafe24.com";
	 * */
	public void SubActivity(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--SubActivity-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}

		//인텐트 태우기
		Intent it = new Intent(ac, SlideViewActivity.class);
		it.putExtra("SUB_URL", val[0]);
		it.putExtra("TITLE", val[1]);
		//it.putExtra("NEW", val[2]);
		it.putExtra("NEW", val[3]);
		it.putExtra("BUTTON", val[4]);
		it.putExtra("BUTTON_URL", return_fun);
		ac.startActivity(it);
		/*
		if (val[2] . equals("top")) {
			ac.overridePendingTransition(R.drawable.anim_slide_in_top, R.drawable.anim_slide_out_bottom);  // 위에서 -> 아래
		}else if(val[2] . equals("right")){
			ac.overridePendingTransition(R.drawable.anim_slide_in_left, R.drawable.anim_slide_out_right);  // ->
		}else if(val[2] . equals("bottom")){
			ac.overridePendingTransition(R.drawable.anim_slide_in_bottom, R.drawable.anim_slide_out_top);  // 아래 -> 위로
		}else if(val[2] . equals("left")){
			ac.overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left);  // <-
		}else if(val[2] . equals("new")){
			ac.overridePendingTransition(R.drawable.anim_slide_in_new, R.drawable.anim_slide_out_new);  // <-
		}else{
			ac.overridePendingTransition(0, 0);  // <-
		}
		 */
		Check_Preferences.setAppPreferences(ac, "SUB_URL" , val[0]);
		Check_Preferences.setAppPreferences(ac, "TITLE" ,  	val[1]);
		Check_Preferences.setAppPreferences(ac, "NEW" ,  	val[3]);
		Check_Preferences.setAppPreferences(ac, "BUTTON" ,  val[4]);
		Check_Preferences.setAppPreferences(ac, "BUTTON_URL" , return_fun);

	}
	/*
	 * param 
	 * url :: 안씀 
	 * name :: 띄울 메시지
	 * return :: 안씀
	 * window.location.href = "js2ios://Location?url=null&name=null&return=weblocation";
	 * */
	public void Location(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--Location-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		Log.e("SKY" , "javascript:"+return_fun + "('" + MainActivity.latitude + "','" + MainActivity.longitude + "')");
		vc.loadUrl("javascript:"+return_fun + "('" + MainActivity.latitude + "','" + MainActivity.longitude + "')");

	}
	/*
	 * param 
	 * url :: 안씀 
	 * name :: 띄울 메시지
	 * return :: 안씀
	 * window.location.href = "js2ios://ShowToast?url=not&name=문구&return=not";
	 * */
	public void showToast(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--showToast-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}

		String word = val[val.length-1];
		Toast.makeText(ac, word, 0).show();
	}

	/*
	 * param 
	 * url :: 안씀 
	 * name :: 복사할 문구
	 * return :: 안씀
	 * window.location.href = "js2ios://ClipboardCopy?url=not&name=문구&return=not";
	 * */
	public void ClipboardCopy(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--ClipboardCopy-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		ClipboardManager clipboardManager = (ClipboardManager) ac.getSystemService(Context.CLIPBOARD_SERVICE);
		String word = val[val.length-1];
		try {
			word = new String(word.getBytes("x-windows-949"), "ksc5601");
			clipboardManager.setText(word);
			//			MainActivity.myTTS.speak(word, TextToSpeech.QUEUE_FLUSH, null);
			//			MainActivity.myTTS.stop();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toast.makeText(ac, "복사되었습니다.", 0).show();
	}
	/*
	 * param 
	 * url :: 안씀 
	 * txt :: 공유하기 문구
	 * return :: 안씀
	 * window.location.href = "js2ios://ShareStr?url=not&txt=문구&return=not";
	 * */
	public void ShareStr(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--ShareStr-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		Intent msg = new Intent(Intent.ACTION_SEND);
		msg.addCategory(Intent.CATEGORY_DEFAULT);
		//msg.putExtra(Intent.EXTRA_SUBJECT, "주제");
		//msg.putExtra(Intent.EXTRA_TITLE, "제목");
		msg.putExtra(Intent.EXTRA_TEXT, val[val.length]);
		msg.setType("text/plain");    
		ac.startActivity(Intent.createChooser(msg, "공유"));
	}
}
