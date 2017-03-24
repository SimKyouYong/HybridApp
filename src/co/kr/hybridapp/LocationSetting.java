package co.kr.hybridapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.kr.hybridapp.R;
import co.kr.hybridapp.common.Constant;
import co.kr.hybridapp.common.GlobalApplication;
import co.kr.hybridapp.common.GpsInfo;
import co.kr.hybridapp.common.SharedPreferencesUtil;
import co.kr.hybridapp.common.SqlLiteUtil;
import co.kr.hybridapp.net.AddrSearch;
import co.kr.hybridapp.obj.AddrDTO;

public class LocationSetting extends Activity {
	
	private LinearLayout search_list, search_result, search_list_wrap, search_result_wrap = null;
	private EditText location_search_text, current_location = null;
	private Button location_search_btn = null;
	private Context mContext = null;
	private ImageView setup_btn_exit = null;
	private Button btn_search_curaddr = null;
	
	// 로케이션 관련 변수
	private GpsInfo gps = null;
	private double cLat, cLng = 0.0;
	private String cAddress = "";
	
	private LocationManager locationManager = null;
	private LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			searchCurAddr(false);
		}
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			cLat = location.getLatitude();
			cLng = location.getLongitude();
			String address = findAddress(cLat, cLng);
			current_location.setText(address);
			cAddress = address;
			
		}
	}; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		GlobalApplication.setCurrentActivity(this);
		mContext = this;
		
		gps = new GpsInfo(this);
		
		setContentView(R.layout.location_setting);
		
		search_list = (LinearLayout)findViewById(R.id.search_list);
		search_list_wrap = (LinearLayout)findViewById(R.id.search_list_wrap);
		search_result = (LinearLayout)findViewById(R.id.search_result);
		search_result_wrap = (LinearLayout)findViewById(R.id.search_result_wrap);
		
		current_location = (EditText)findViewById(R.id.current_location);
		int color = Color.parseColor("#01abdc");
		current_location.getBackground().setColorFilter(color, Mode.SRC_IN);
		current_location.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(cLat == 0.0 || cLng == 0.0 || cAddress.equals("")) {
					Toast.makeText(mContext, "먼저 현재 위치를 검색해주세요.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				// TODO Auto-generated method stub
				SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
				sharedPreferencesUtil.put("address", cAddress);
				sharedPreferencesUtil.put("lat", String.valueOf(cLat));
				sharedPreferencesUtil.put("lng", String.valueOf(cLng));
				
				JSONObject jobj = new JSONObject();
				try {
					jobj.put("address", cAddress);
					jobj.put("lat", cLat);
					jobj.put("lng", cLng);
					
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("data", jobj.toString());
					intent.putExtras(bundle);
					
					setResult(RESULT_OK, intent);
				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				finish();
			}
		});
		
		location_search_text = (EditText)findViewById(R.id.location_search_text);
		
		location_search_btn = (Button)findViewById(R.id.location_search_btn);
		
		btn_search_curaddr = (Button)findViewById(R.id.btn_search_curaddr);
		btn_search_curaddr.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchCurAddr(true);
			}
		});
		
		setup_btn_exit = (ImageView)findViewById(R.id.setup_btn_exit);
		
		setup_btn_exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		location_search_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchAddr(location_search_text.getText().toString());
			}
		});
		
		getCurrentAddr();
		
		SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
		cAddress = sharedPreferencesUtil.getValue("address", "");
		if(!sharedPreferencesUtil.getValue("lat", "").equals("") && !sharedPreferencesUtil.getValue("lng", "").equals("")) {
			cLat = Double.parseDouble(sharedPreferencesUtil.getValue("lat", ""));
			cLng = Double.parseDouble(sharedPreferencesUtil.getValue("lng", ""));
		}
		if(!cAddress.equals("")) {
			current_location.setText(cAddress);
		} else {
			searchCurAddr(false);
		}
		
		
	}
	
	public void getCurrentAddr() {
		SqlLiteUtil sqlLiteUtil = new SqlLiteUtil(getApplicationContext(), "moin.db", null, 1);
		ArrayList<AddrDTO> list = sqlLiteUtil.getCurrentAddr();
		
		search_list_wrap.setVisibility(View.VISIBLE);
		search_result_wrap.setVisibility(View.GONE);
		search_list.removeAllViews();
		int cnt = list.size();
		for(int i=0; i<cnt; i++) {
			final AddrDTO addr = list.get(i);
			
			View view = (View) getLayoutInflater().inflate(R.layout.location_setting_item, null);
			
			ImageView iv_delete = (ImageView)view.findViewById(R.id.iv_delete);
			TextView tv_address = (TextView)view.findViewById(R.id.tv_address);
			Button btn_select = (Button)view.findViewById(R.id.btn_select);
			
			iv_delete.setVisibility(View.INVISIBLE);
			tv_address.setText(addr.getAddress());
			
			btn_select.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
					sharedPreferencesUtil.put("address", addr.getAddress());
					sharedPreferencesUtil.put("lat", String.valueOf(addr.getLat()));
					sharedPreferencesUtil.put("lng", String.valueOf(addr.getLng()));
					
					JSONObject jobj = new JSONObject();
					try {
						jobj.put("address", addr.getAddress());
						jobj.put("lat", addr.getLat());
						jobj.put("lng", addr.getLng());
						
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putString("data", jobj.toString());
						intent.putExtras(bundle);
						
						setResult(RESULT_OK, intent);
					} catch (JSONException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					finish();
				}
			});
			
			search_list.addView(view);
			
		}
		
		if(cnt == 0) {
			View view = (View) getLayoutInflater().inflate(R.layout.no_item, null);
			TextView tv_no_item = (TextView)view.findViewById(R.id.tv_no_item);
			tv_no_item.setText("검색내역이 없습니다.");
			search_list.addView(view);
		}
	}
	
	public void searchAddr(String query) {
		AddrSearch addrSearch = new AddrSearch(this);
		
		addrSearch.getAddrArray("?apikey="+Constant.DAUM_API_KEY+"&query="+query, new AddrSearch.OnResultCallback() {
			
			@Override
			public void onResult(JSONArray result) {
				// TODO Auto-generated method stub
				if(result==null) {
					Toast.makeText(mContext, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
					return;
				}
				search_list_wrap.setVisibility(View.GONE);
				search_result_wrap.setVisibility(View.VISIBLE);
				search_result.removeAllViews();
				
				try {
					int cnt = result.length();
					for(int i=0; i<cnt; i++) {
						
						JSONObject data = result.getJSONObject(i);
						final String address = data.getString("address");
						final double latitude = data.getDouble("latitude");
						final double longitude = data.getDouble("longitude");
						
						View view = (View) getLayoutInflater().inflate(R.layout.location_setting_item, null);
						
						ImageView iv_delete = (ImageView)view.findViewById(R.id.iv_delete);
						TextView tv_address = (TextView)view.findViewById(R.id.tv_address);
						Button btn_select = (Button)view.findViewById(R.id.btn_select);
						
						iv_delete.setVisibility(View.INVISIBLE);
						tv_address.setText(address);
						
						btn_select.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								SqlLiteUtil sqlLiteUtil = new SqlLiteUtil(getApplicationContext(), "moin.db", null, 1);
								sqlLiteUtil.insert_addr(address, latitude, longitude);
								
								SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext);
								sharedPreferencesUtil.put("address", address);
								sharedPreferencesUtil.put("lat", String.valueOf(latitude));
								sharedPreferencesUtil.put("lng", String.valueOf(longitude));
							
								JSONObject jobj = new JSONObject();
								try {
									jobj.put("address", address);
									jobj.put("lat", latitude);
									jobj.put("lng", longitude);
									
									Intent intent = new Intent();
									Bundle bundle = new Bundle();
									bundle.putString("data", jobj.toString());
									intent.putExtras(bundle);
									
									setResult(RESULT_OK, intent);
								} catch (JSONException e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
								finish();
							}
						});
						
						search_result.addView(view);
						
					}
					
					if(cnt == 0) {
						View view = (View) getLayoutInflater().inflate(R.layout.no_item, null);
						TextView tv_no_item = (TextView)view.findViewById(R.id.tv_no_item);
						tv_no_item.setText("검색결과가 없습니다.");
						search_result.addView(view);
					}
					
				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			
			
		});
	}
	
	public void searchCurAddr(boolean setting) {
		
		gps.getLocation();
		if(!gps.isGPSEnabled && setting) {
			gps.showSettingsAlert();
			return;
		}
		
		if(gps.isGetLocation) {
			cLat = gps.getLatitude();
			cLng = gps.getLongitude();
			String address = findAddress(cLat, cLng);
			current_location.setText(address);
			cAddress = address;
		}
	}
	
	 private String findAddress(double lat, double lng) {                                             
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> address;
        try {
          if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
               if (address != null && address.size() > 0) {
                    // 주소
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    
                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress);
                }
            }
            
        } catch (IOException e) {
           Toast.makeText(mContext, "주소취득 실패", Toast.LENGTH_LONG).show();
           current_location.setText("현재 위치를 찾지 못했습니다.");
           e.printStackTrace();
        }
        return bf.toString();
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(locationListener);
	} 
	
	 
	 
}