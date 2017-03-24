package co.kr.hybridapp.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
 
public class GpsInfo extends Service implements LocationListener {
  
    private final Activity mContext;
    
    private static final int REQUEST_LOCATION = 0;
  
    // ���� GPS �������
    public boolean isGPSEnabled = false;
  
    // ��Ʈ��ũ ������� 
    boolean isNetworkEnabled = false;
  
    // GPS ���°�
    public boolean isGetLocation = false;
  
    Location location; 
    double lat; // ���� 
    double lon; // �浵
  
    // �ּ� GPS ���� ������Ʈ �Ÿ� 10���� 
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; 
  
    // �ּ� GPS ���� ������Ʈ �ð� �и������̹Ƿ� 1��
    private static final long MIN_TIME_BW_UPDATES =  1; 
  
    protected LocationManager locationManager;
  
    public GpsInfo(Activity context) {
        this.mContext = context;
        getLocation();
    }
  
    public Location getLocation() {
        try {
            
  /*
        	// Activity에서 실행하는경우
        	if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

        	    // 이 권한을 필요한 이유를 설명해야하는가?
        	    if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

        	        // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
        	        // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다

        	    } else {

        	        ActivityCompat.requestPermissions(mContext,
        	                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
        	                REQUEST_LOCATION);

        	        // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

        	    }
        	}
        	*/
        	
        	locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
        	
            // GPS ���� �������� 
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
  
            // ���� ��Ʈ��ũ ���� �� �˾ƿ��� 
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            
            
  
            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS �� ��Ʈ��ũ����� �������� ������ �ҽ� ����
            } else {
            	
            	
            	
                this.isGetLocation = true;
                // ��Ʈ��ũ ������ ���� ��ġ�� �������� 
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // ���� �浵 ���� 
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }
                 
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }
            }
  
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }
      
    /**
     * GPS ���� 
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GpsInfo.this);
        }       
    }
      
    /**
     * �������� �����ɴϴ�. 
     * */
    public double getLatitude(){
        if(location != null){
            lat = location.getLatitude();
        }
        return lat;
    }
      
    /**
     * �浵���� �����ɴϴ�. 
     * */
    public double getLongitude(){
        if(location != null){
            lon = location.getLongitude();
        }
        return lon;
    }
      
    /**
     * GPS �� wife ������ �����ִ��� Ȯ���մϴ�. 
     * */
    public boolean isGetLocation() {
        return this.isGetLocation;
    }
      
    /**
     * GPS ������ �������� �������� 
     * ���������� ���� ����� alert â
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
 
        alertDialog.setTitle("GPS 사용 설정이 필요한 서비스입니다.");
        alertDialog.setMessage("GPS 사용 설정을 하러 가시겠습니까?");
   
        // OK �� ������ �Ǹ� ����â���� �̵��մϴ�. 
        alertDialog.setPositiveButton("확인", 
                                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // Cancle �ϸ� ���� �մϴ�. 
        alertDialog.setNegativeButton("취소", 
                              new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        alertDialog.show();
    }
  
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
 
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
         
    }
 
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
         
    }
 
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
         
    }
 
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
         
    }
}