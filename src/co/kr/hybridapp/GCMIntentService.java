package co.kr.hybridapp;

import java.util.regex.Pattern;

import com.google.android.gcm.GCMBaseIntentService;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;
import co.kr.hybridapp.common.DEFINE;
import co.kr.hybridapp.common.LoadImageFromWeb;

public class GCMIntentService extends GCMBaseIntentService{

	private static final String TAG = "GCM";
	private static final String SENDER_ID = DEFINE.GCM_ID;

	public GCMIntentService() {
		super(SENDER_ID);
	}

	public void onReceive(Context context, Intent intent) {
	  	
	}
	
	@Override
	protected void onRegistered(Context context, String regID) {
		// TODO Auto-generated method stub
		if(!regID.equals("") || regID != null){
			SplashActivity.reg_id = regID;
			Log.e(TAG, "onRegistered!! " + regID);
			((SplashActivity)SplashActivity.context).init();
		}
	}

	@Override
	protected void onUnregistered(Context context, String regID) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onUnregistered!! " + regID);
	}
	
	@Override
	protected void onMessage(Context context, Intent intent) {
		if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
			showMessage(context, intent);
		}
	}

	@Override
	protected void onError(Context context, String msg) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onError!! " + msg);
		
	}
	
	private void showMessage(Context context, Intent intent){
		
		SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
		boolean pushEnable = prefs.getBoolean("pushEnable", false);
		
		if(pushEnable)
		{
			String title = intent.getStringExtra("title");
			String msg = intent.getStringExtra("msg");
			String popup = intent.getStringExtra("popup");
			String image_url = intent.getStringExtra("image_url");
			String link_url = intent.getStringExtra("link_url");
			String push_color = intent.getStringExtra("push_color");
			String tag = intent.getStringExtra("tag");
			
			Log.e("SKY" , "title :: " + title);
			Log.e("SKY" , "msg :: " + msg);
			Log.e("SKY" , "popup :: " + popup);
			Log.e("SKY" , "image_url :: " + image_url);
			Log.e("SKY" , "link_url :: " + link_url);
			Log.e("SKY" , "push_color :: " + push_color);
			Log.e("SKY" , "tag :: " + tag);
			//진동 
			Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
			long milliseconds = 1000;
			vibrator.vibrate(milliseconds);
			
			int icon = R.drawable.ic_launcher;
			long when = System.currentTimeMillis();
			
			
			if(title==null){
				title=getString(R.string.app_name);
			}

			String[] txtcolor=null;
			if(!push_color.equals("")){
				txtcolor=push_color.split(Pattern.quote("|"));
			}
			
			Bitmap message_bitmap = null; 
			if ((image_url != null) && (!image_url.equals(""))) {
				message_bitmap = LoadImageFromWeb.downloadImage(image_url);
			}

			NotificationManager notificationManager = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
		
			Intent it=new Intent(context, MainActivity.class);
	    	it.putExtra("openurl", link_url);			
			
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), PendingIntent.FLAG_UPDATE_CURRENT);

			Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
			RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.noti_style);
			
			NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(context);
			
			if (tag.equals("1")) {
				//일반 푸시
				if(message_bitmap!=null){
					Log.e("SKY", "111!! ");
					NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
					if(!push_color.equals("")){
						Log.e("SKY", "푸시 칼라 안씀!");

						style
						.setBigContentTitle(Html.fromHtml("<font color='#"+txtcolor[0]+"'>"+title+"</font>"))
						.setSummaryText(Html.fromHtml("<font color='#"+txtcolor[0]+"'>"+msg+"</font>")).
						bigPicture(message_bitmap);
					}else{
						Log.e("SKY", "bbbb!! ");
						notiBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
						notiBuilder.setSmallIcon(R.drawable.ic_launcher)
							.setLargeIcon(largeIcon)
							.setTicker(title)
							.setContentTitle(title)
							.setContentText(msg)
							.setAutoCancel(true)
							.setContentIntent(pendingIntent);
//							.setContent(remoteViews);
						style.setBigContentTitle(title).setSummaryText(msg).bigPicture(message_bitmap);
					}
					notiBuilder.setStyle(style);		
					
					Notification noti = notiBuilder.build();
					noti.defaults |=Notification.DEFAULT_SOUND;
					notificationManager.notify(0, noti);
				}else if(!push_color.equals("")){	
					Log.e("SKY", "222!! ");

					int tcolor = Integer.parseInt(txtcolor[0],16)+0xFF000000;
					int bcolor = Integer.parseInt(txtcolor[1],16)+0xFF000000;
					remoteViews.setTextColor(R.id.push_title, tcolor);
					remoteViews.setTextColor(R.id.push_message, tcolor);
					remoteViews.setInt(R.id.push_pannel, "setBackgroundColor", bcolor);
					remoteViews.setTextViewText(R.id.push_title, title);
					remoteViews.setTextViewText(R.id.push_message, msg+"\n\n");
					
					Notification noti = notiBuilder.build();
					noti.defaults |=Notification.DEFAULT_SOUND;
					notificationManager.notify(0, noti);
				}else{
					Notification notification = new Notification(icon, msg, when);
					Intent notificationIntent = new Intent(context,MainActivity.class);

					PendingIntent Pintent = PendingIntent.getActivity(context, 0,notificationIntent, 0);

					notification.setLatestEventInfo(context, title, msg, Pintent);
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					notificationManager.notify(0, notification);
					
				}
			} else if(tag.equals("2")){
				Log.e("SKY", "3333");

				if(message_bitmap!=null){
					Log.e("SKY", "111!! ");
					NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
					if(!push_color.equals("")){
						style
						.setBigContentTitle(Html.fromHtml("<font color='#"+txtcolor[0]+"'>"+title+"</font>"))
						.setSummaryText(Html.fromHtml("<font color='#"+txtcolor[0]+"'>"+msg+"</font>")).
						bigPicture(message_bitmap);
					}else{
						style.setBigContentTitle(title).setSummaryText(msg).bigPicture(message_bitmap);
					}
					notiBuilder.setStyle(style);		
					
					Notification noti = notiBuilder.build();
					noti.defaults |=Notification.DEFAULT_SOUND;
					notificationManager.notify(0, noti);
				}else if(!push_color.equals("")){	
					Log.e("SKY", "222!! ");

					int tcolor = Integer.parseInt(txtcolor[0],16)+0xFF000000;
					int bcolor = Integer.parseInt(txtcolor[1],16)+0xFF000000;
					remoteViews.setTextColor(R.id.push_title, tcolor);
					remoteViews.setTextColor(R.id.push_message, tcolor);
					remoteViews.setInt(R.id.push_pannel, "setBackgroundColor", bcolor);
					remoteViews.setTextViewText(R.id.push_title, title);
					remoteViews.setTextViewText(R.id.push_message, msg+"\n\n");
					
					Notification noti = notiBuilder.build();
					noti.defaults |=Notification.DEFAULT_SOUND;
					notificationManager.notify(0, noti);
				}else{
					NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
					style.setSummaryText(getString(R.string.app_name)).setBigContentTitle(title).bigText(msg);
					notiBuilder.setStyle(style);	
					
					Notification noti = notiBuilder.build();
					noti.defaults |=Notification.DEFAULT_SOUND;
					notificationManager.notify(0, noti);
				}
				
				
				Intent i = new Intent(context, ShowMSGActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				i.setComponent(new ComponentName(context, ShowMSGActivity.class));
				i.putExtra("flag", false);
				i.putExtra("msg", msg);
				i.putExtra("openurl",link_url);
				i.putExtra("imgurl",image_url);
				startActivity(i);			
				
			}else if(tag.equals("3")){
				Log.e("SKY", "4444");

				
				if(message_bitmap!=null){
					Log.e("SKY", "111!! ");
					NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
					if(!push_color.equals("")){
						style
						.setBigContentTitle(Html.fromHtml("<font color='#"+txtcolor[0]+"'>"+title+"</font>"))
						.setSummaryText(Html.fromHtml("<font color='#"+txtcolor[0]+"'>"+msg+"</font>")).
						bigPicture(message_bitmap);
					}else{
						style.setBigContentTitle(title).setSummaryText(msg).bigPicture(message_bitmap);
					}
					notiBuilder.setStyle(style);		
					
					Notification noti = notiBuilder.build();
					noti.defaults |=Notification.DEFAULT_SOUND;
					notificationManager.notify(0, noti);
				}else if(!push_color.equals("")){	
					Log.e("SKY", "222!! ");

					int tcolor = Integer.parseInt(txtcolor[0],16)+0xFF000000;
					int bcolor = Integer.parseInt(txtcolor[1],16)+0xFF000000;
					remoteViews.setTextColor(R.id.push_title, tcolor);
					remoteViews.setTextColor(R.id.push_message, tcolor);
					remoteViews.setInt(R.id.push_pannel, "setBackgroundColor", bcolor);
					remoteViews.setTextViewText(R.id.push_title, title);
					remoteViews.setTextViewText(R.id.push_message, msg+"\n\n");
					
					Notification noti = notiBuilder.build();
					noti.defaults |=Notification.DEFAULT_SOUND;
					notificationManager.notify(0, noti);
				}else{
					NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
					style.setSummaryText(getString(R.string.app_name)).setBigContentTitle(title).bigText(msg);
					notiBuilder.setStyle(style);	
					
					Notification noti = notiBuilder.build();
					noti.defaults |=Notification.DEFAULT_SOUND;
					notificationManager.notify(0, noti);
				}
				
				
				Intent i = new Intent(context, ShowMSGActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				i.setComponent(new ComponentName(context, ShowMSGActivity.class));
				i.putExtra("flag", false);
				i.putExtra("msg", msg);
				i.putExtra("openurl",link_url);
				i.putExtra("imgurl",image_url);
				startActivity(i);
				
				
							
			}else if(tag.equals("4")){
				
				
				
				//이미지만 나오는 푸시 팝업
				Intent i = new Intent(context, ShowIMGActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				i.setComponent(new ComponentName(context, ShowIMGActivity.class));
				i.putExtra("flag", true);
				i.putExtra("imgurl",image_url);
				i.putExtra("openurl",link_url);
				i.putExtra("bottomview","false");

				startActivity(i);	
			}else{
				Log.e("SKY", "55555");

				NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
				style.setSummaryText(getString(R.string.app_name)).setBigContentTitle(title).bigText(msg);
				notiBuilder.setStyle(style);	
				
				Notification noti = notiBuilder.build();
				noti.defaults |=Notification.DEFAULT_SOUND;
				notificationManager.notify(0, noti);
					
			}
			
			

			
		}
		
	}
	
	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.e(TAG, "onRecoverableError!! " + errorId);

		return super.onRecoverableError(context, errorId);
	}
}


