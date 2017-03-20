package co.kr.hybridapp;

import java.security.MessageDigest;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import co.kr.hybridapp.common.Constant;
import co.kr.hybridapp.common.GlobalApplication;

public class Login extends Activity {
	
	private OAuthLogin oAuthLogin = null; 
	private Context mContext = null;
	private Activity mActivity = null;
	private CallbackManager callbackManager;
	private LinearLayout btn_login_kakao, btn_login_naver, btn_login_facebook, btn_login_email = null;
	private ImageView btn_exit = null;
	
	private SessionCallback callback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		GlobalApplication.setCurrentActivity(this);
		
		setContentView(R.layout.login);
		/*
		try {
			PackageInfo info = getPackageManager().getPackageInfo("co.kr.hybridapp", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e("SKY" , "HASH KEY :: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		*/
		mContext = this;
		mActivity = this;
		
		btn_exit = (ImageView)findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
		
		btn_login_kakao = (LinearLayout) findViewById(R.id.btn_login_kakao);
		btn_login_naver = (LinearLayout) findViewById(R.id.btn_login_naver);
		btn_login_facebook = (LinearLayout) findViewById(R.id.btn_login_facebook);
		btn_login_email = (LinearLayout) findViewById(R.id.btn_login_email);
		
		btn_login_kakao.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				kakaoLogin();
			}
		});
		
		btn_login_naver.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				naverLogin();
			}
		});

		btn_login_facebook.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				facebookLogin();
			}
		});
		
		btn_login_email.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				emailLogin();
			}
		});
		
	}
	
	public void naverLogin() {
		if(oAuthLogin == null) oAuthLogin = oAuthLogin.getInstance(); 
		oAuthLogin.init(mContext, Constant.NAVER_CLIENT_ID, Constant.NAVER_CLIENT_SECRET, Constant.NAVER_CLIENT_NAME);
		oAuthLogin.startOauthLoginActivity(mActivity, mOAuthLoginHandler);
	}
	
	public void facebookLogin() {
		FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    	GraphRequest request = GraphRequest.newMeRequest(
                                AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        // this is where you should have the profile
                                        Log.v("fetched info", object.toString());
                                        
                                        try {
                                        	String name = object.getString("name");
                                        	String email = object.getString("email");
                                        	String id = object.getString("id");
                                        	
                                        	Intent intent = new Intent();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("type", "facebook");
                                            bundle.putString("data", object.toString());
                                            intent.putExtras(bundle);
                                            setResult(RESULT_OK, intent);
                                            
                                            finish();
                                        } catch (JSONException e) {
											// TODO: handle exception
                                        	e.printStackTrace();
										}
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday"); //write the fields you need
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                         // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                         // App code   
                    }
        });
        
        LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("public_profile", "email"));
	}

	public void kakaoLogin() {
		callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        //현재 세션의 상태 체크후 Login 진행
        if (Session.getCurrentSession().checkAndImplicitOpen()) {
            //이미 열려 있을경우 callback을 호출
            Log.d("로그", "checkAndImplicitOpen");
        } else {
            //AuthType을 지정하여 원하는 플랫폼(카카오톡, 링크 , 스토리) 계정으로 로그인이 가능하다.
            Session.getCurrentSession().open(AuthType.KAKAO_TALK, mActivity);
        }
        
	}
	
	public void emailLogin() {
		final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.login_email, null);
        ImageView btn_exit = (ImageView) view.findViewById(R.id.btn_exit);
        final EditText mb_id = (EditText)view.findViewById(R.id.mb_id);
        final EditText mb_password = (EditText)view.findViewById(R.id.mb_password);
        Button btn_login = (Button)view.findViewById(R.id.btn_login);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sb = new StringBuffer();
                String id = mb_id.getText().toString();
                String password = mb_password.getText().toString();
                
                dialog.dismiss();
                
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                
                JSONObject jobj = new JSONObject();
                try {
                	jobj.put("id", id);
                	jobj.put("password", password);
                } catch (JSONException e) {
					// TODO: handle exception
				}
                
                bundle.putString("type", "email");
                bundle.putString("data", jobj.toString());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                
                finish();
            }
        });

        dialog.setContentView(view);

        // Dialog 사이즈 조절 하기
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        dialog.show();
	}
	
	private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
		
	      @Override
	      public void run(boolean success) {
	             if (success) {
	            	 /*
	                   String accessToken = oAuthLogin.getAccessToken(mContext);
	                   String refreshToken = oAuthLogin.getRefreshToken(mContext);
	                   long expiresAt = oAuthLogin.getExpiresAt(mContext);
	                   String tokenType = oAuthLogin.getTokenType(mContext);
	                   */
	            	 new RequestApiTask().execute();
	             } else {
	                   String errorCode = oAuthLogin.getLastErrorCode(mContext).getCode();
	                   String errorDesc = oAuthLogin.getLastErrorDesc(mContext);
	                   Toast.makeText(mContext, "errorCode:" + errorCode
	                         + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
	             }
	      };
	};
	
	private class RequestApiTask extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {

		}
		@Override
		protected String doInBackground(Void... params) {
			String url = "https://openapi.naver.com/v1/nid/me";
			String at = oAuthLogin.getAccessToken(mContext);
			return oAuthLogin.requestApi(mContext, at, url);
		}
		protected void onPostExecute(String content) {
			Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("type", "naver");
            bundle.putString("data", content);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            
            finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
		
		callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
	    
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    //Session.getCurrentSession().removeCallback(callback);
	}
	
	private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }
	
	private void requestMe() {
	    UserManagement.requestMe(new MeResponseCallback() {
	        @Override
	        public void onFailure(ErrorResult errorResult) {
	            String message = "failed to get user info. msg=" + errorResult;
	            Logger.d(message);

	        }

	        @Override
	        public void onSessionClosed(ErrorResult errorResult) {
	            
	        }

	        @Override
	        public void onSuccess(UserProfile userProfile) {
	            Logger.d("UserProfile : " + userProfile);
	            Intent intent = new Intent();
                Bundle bundle = new Bundle();
                
                JSONObject jobj = new JSONObject();
                try {
                	jobj.put("id", userProfile.getId());
                	jobj.put("nickname", userProfile.getNickname());
                	jobj.put("profile", userProfile.getProfileImagePath());
                } catch (JSONException e) {
					// TODO: handle exception
				}
                
                bundle.putString("type", "kakao");
                bundle.putString("data", jobj.toString());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                
                finish();
	        }

	        @Override
	        public void onNotSignedUp() {
	            
	        }
	    });
	}


}