package co.kr.hybridapp.common;

import java.io.UnsupportedEncodingException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

public class FunNative  {


	private WebView Webview_copy;
	/*
	 * param 
	 * url :: 이동 url 주소 
	 * title :: 타이틀명 
	 * action :: 애니메이션 (예 : 시작점이 왼쪽 : 왼쪽에서 -> 오른쪽 , 오른쪽 : 오른쪽-> 왼쪽)
	 * window.location.href = "js2ios://SubActivity?url=urladdress&title=타이틀명&action=left";
	 * */
	public void SubActivity(String url , Activity ac , WebView vc , String return_fun){
		Log.e("SKY" , "--ClipboardCopy-- :: ");
		String val[] = url.split(",");
		for (int i = 0; i < val.length; i++) {
			Log.e("SKY" , "VAL["+i + "]  :: " + i + " --> " + val[i]);
		}
		//인텐트 태우기
		
		
	}
	
	/*
	 * param 
	 * url :: 안씀 
	 * name :: 띄울 메시지
	 * return :: 안씀
	 * window.location.href = "js2ios://ShowToast?url=not&name=문구&return=not";
	 * */
	@SuppressLint("ShowToast")
	@SuppressWarnings("deprecation")
	public void ShowToast(String url , Activity ac , WebView vc , String return_fun){
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
			Toast.makeText(ac, word, 0).show();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * param 
	 * url :: 안씀 
	 * name :: 복사할 문구
	 * return :: 안씀
	 * window.location.href = "js2ios://ClipboardCopy?url=not&name=문구&return=not";
	 * */
	@SuppressLint("ShowToast")
	@SuppressWarnings("deprecation")
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
