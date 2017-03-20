package co.kr.hybridapp;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.HttpMethod;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.ResponseMessage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import co.kr.hybridapp.obj.SKLocation;
import co.kr.hybridapp.obj.SKPoint;
public class SKRoute extends Activity {
	
	private GoogleMap googleMap = null;
	private double startX, startY, endX, endY = 0;
	private ArrayList<SKPoint> pointList = null;
	private ImageView img_exit = null;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sk_route);
		
		Intent receiveIntent = getIntent();
		Bundle data = receiveIntent.getExtras();
		startX = data.getDouble("startX");
		startY = data.getDouble("startY");
		endX = data.getDouble("endX");
		endY = data.getDouble("endY");
		
		img_exit=(ImageView) findViewById(R.id.setup_btn_exit);
		img_exit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
        startMap();
	}
	
	public String init(double startX, double startY, double endX, double endY) {
		
		// Input here.
		APIRequest.setAppKey("62d4e380-bdc9-3257-8863-99309db3b41b");
        
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", "1");	
		map.put("endX", URLEncoder.encode(String.valueOf(endX)));
		map.put("endY", URLEncoder.encode(String.valueOf(endY)));
		map.put("startX", URLEncoder.encode(String.valueOf(startX)));
		map.put("startY", URLEncoder.encode(String.valueOf(startY)));
		map.put("reqCoordType", "WGS84GEO");
		map.put("resCoordType", "WGS84GEO");
		 
		JSONObject jobj = new JSONObject();
		try {
			
			
			// 사용 파라미터
			jobj.put("endX", endX);
			jobj.put("endY", endY);
			jobj.put("startX", startX);
			jobj.put("startY", startY);
			jobj.put("reqCoordType", "WGS84GEO");
			jobj.put("resCoordType", "WGS84GEO");
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//String payload = jobj.toString();
		//String payload = "{\"endX\":"+endX+", \"endY\":"+endY+", \"startX\":"+startX+", \"startY\":"+startY+", \"reqCoordType\":\"WGS84GEO\", \"resCoordType\":\"WGS84GEO\"}";
		String payload = "endX="+URLEncoder.encode(String.valueOf(endX))+"&endY="+URLEncoder.encode(String.valueOf(endY))+"&startX="+URLEncoder.encode(String.valueOf(startX))+"&startY="+URLEncoder.encode(String.valueOf(startY))+"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO";
		Log.i("SKPARAM", payload);
		
		RequestBundle reqBundle = new RequestBundle();
		reqBundle.setUrl("https://apis.skplanetx.com/tmap/routes");
		reqBundle.setParameters(map);
		//reqBundle.setPayload(payload);
		reqBundle.setHttpMethod(HttpMethod.POST);
		reqBundle.setRequestType(CONTENT_TYPE.FORM);
		reqBundle.setResponseType(CONTENT_TYPE.JSON);
		 
		APIRequest api = new APIRequest();
		ResponseMessage result = null;
		try {
			result = api.request(reqBundle);
		} catch (PlanetXSDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result.getResultMessage();
	}

	@SuppressLint("NewApi")
	public void startMap() {
		// TODO Auto-generated method stub
		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		googleMap = mapFragment.getMap();
		
		 // 맵 위치를 이동하기
        CameraUpdate update = CameraUpdateFactory.newLatLng(
                new LatLng(startY, startX));
        // 위도,경도
        googleMap.moveCamera(update);
        
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);
        
        MarkerOptions markerOptions = new MarkerOptions()
        .position(new LatLng(startY, startX))
        .title("출발");
        
        MarkerOptions markerOptions2 = new MarkerOptions()
        .position(new LatLng(endY, endX))
        .title("도착");
        
        // 마커를 추가하고 말풍선 표시한 것을 보여주도록 호출
        googleMap.addMarker(markerOptions2).showInfoWindow();
        googleMap.addMarker(markerOptions).showInfoWindow();
        
		new InitTask().execute();
		
	}
	
	private class InitTask extends AsyncTask<Void, Void, Void> {

		String res = "";
		LatLng cLocation = null;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			cLocation = new LatLng(startY, startX);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			res = init(startX, startY, endX, endY);
			Log.i("SKDATA", res);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			try {
				JSONObject jobj = new JSONObject(res);
				JSONArray jarr = jobj.getJSONArray("features");
				
				pointList = new ArrayList<SKPoint>();
				for(int i=0; i<jarr.length(); i++) {
					JSONObject data = jarr.getJSONObject(i);
					JSONObject geometry = data.getJSONObject("geometry");
					JSONObject properties = data.getJSONObject("properties");
					JSONArray coordinates = geometry.getJSONArray("coordinates");
					
					String type = geometry.getString("type");
					SKPoint skPoint = new SKPoint();
					
					// geometry 정보 파싱 시작
					ArrayList<SKLocation> locationList = new ArrayList<SKLocation>();
					
					if(type.equals("Point")) {
						SKLocation skLocationPoint = new SKLocation();
						for(int j=0; j<coordinates.length(); j++) {
							if(j == 0) {
								skLocationPoint.setLng(coordinates.getDouble(j));
							} else {
								skLocationPoint.setLat(coordinates.getDouble(j));
							}
						}
						locationList.add(skLocationPoint);
					} else {
						
						for(int j=0; j<coordinates.length(); j++) {
							SKLocation skLocationLine = new SKLocation();
							JSONArray latlng = coordinates.getJSONArray(j);
							for(int k=0; k<latlng.length(); k++) {
								if(k == 0) {
									skLocationLine.setLng(latlng.getDouble(k));
								} else {
									skLocationLine.setLat(latlng.getDouble(k));
								}
							}
							locationList.add(skLocationLine);
						}
					}
					
					skPoint.setLocationList(locationList);
					
					// 프로퍼티 파싱 시작
					skPoint.setIndex(properties.getInt("index"));
					if(!properties.isNull("pointIndex")){
						skPoint.setPointIndex(properties.getInt("pointIndex"));
					}
					if(!properties.isNull("name")){
						skPoint.setName(properties.getString("name"));
					}
					if(!properties.isNull("description")){
						skPoint.setDescription(properties.getString("description"));
					}
					if(!properties.isNull("nextRoadName")){
						skPoint.setNextRoadName(properties.getString("nextRoadName"));
					}
					if(!properties.isNull("turnType")){
						skPoint.setTurnType(properties.getInt("turnType"));
					}
					
					if(!properties.isNull("pointType")){
						skPoint.setPointType(properties.getString("pointType"));
					}
					
					pointList.add(skPoint);
					
				}
				ArrayList<LatLng> points = new ArrayList<LatLng>();
				points.add(cLocation);
				// 선긋기 시작
				for(int i=0; i<pointList.size(); i++) {
					SKPoint point = pointList.get(i);
					ArrayList<SKLocation> locationList = point.getLocationList();
					
					for(int j=0; j<locationList.size(); j++) {
						SKLocation location = locationList.get(j);
						LatLng loc1 = cLocation;
						LatLng loc2 = new LatLng(location.getLat(), location.getLng());
						cLocation = loc2;
						
						points.add(loc2);
						
						Log.i("LOC", "" + location.getLat() + ", " +location.getLng());
						
						
						
					}
					
					
					
				}
				
				googleMap.addPolyline(new PolylineOptions().addAll(points).width(5).color(Color.RED));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}