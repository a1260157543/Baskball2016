package com.lyy2016.ball.appupdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lyy2016.ball.MainActivity;
import com.lyy2016.ball.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

	private static final int LOADMAIN = 1;//加载主界面
	private static final int SHOWUPDATEDIALOG = 2;//显示是否更新的对话框
	protected static final int ERROR = 3;//错误统一代号
	private LinearLayout rl_root;// 界面的根布局组件
	private int versionCode;// 版本号
	private String versionName;// 版本名
	private TextView tv_versionName;// 显示版本名的组件
	private UrlBean parseJson;//url信息封装bean
	private long startTimeMillis;//记录开始访问网络的时间
	private ProgressBar pb_download;//下载最新版本apk的进度条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化界面
		initView();
		// 初始化数据
		initData();
		// 初始化动画
		initAnimation();
		// 检测服务器的版本
		checkVerion();

	}

	private void initData() {
		// 获取自己的版本信息
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			// 版本号			
			versionCode = packageInfo.versionCode;
			// 版本名		
			versionName = packageInfo.versionName;

			// 设置textview
			tv_versionName.setText("当前版本 : "+versionName);
		} catch (NameNotFoundException e) {
			// can not reach 异常不会发生
		}
	}

	private void checkVerion() {
		// 耗时操作都要放到子线程中执行
		new Thread(new Runnable() {

			

			

			@Override
			public void run() {
				BufferedReader bfr = null;
				HttpURLConnection conn = null;
				int errorCode = -1;//正常，没有错误
				try {
					startTimeMillis = System.currentTimeMillis();
					
					URL url = new URL("https://a1260157543.github.io/ball/check.json");
					conn = (HttpURLConnection) url
							.openConnection();
					// 读取数据的超时时间
					conn.setReadTimeout(5000);
					// 网络连接超时
					conn.setConnectTimeout(5000);
					// 设置请求方式
					conn.setRequestMethod("GET");
					// 获取相应结果
					int code = conn.getResponseCode();
					
					if (code == 200) {// 数据获取成功
						// 获取读取的字节流
						InputStream is = conn.getInputStream();
						// 把字节流转换成字符流
						bfr = new BufferedReader(
								new InputStreamReader(is));
						// 读取一行信息
						String line = bfr.readLine();
						// json字符串数据的封装
						StringBuilder json = new StringBuilder();
						while (line != null) {
							json.append(line);
							line = bfr.readLine();
						}
						parseJson = parseJson(json);//返回数据封装信息
	
					} else {
						errorCode = 404;
					}
				} catch (MalformedURLException e) {//4002
					errorCode = 4002;
					e.printStackTrace();
				} catch (IOException e) {// 4001
					// TODO Auto-generated catch block
					//
					errorCode = 4001;
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					errorCode = 4003;
					e.printStackTrace();
				} finally {
					/*if (errorCode == -1){
						isNewVersion(parseJson);// 是否有新版本
					} else {
						Message msg = Message.obtain();
						msg.what = ERROR;
						msg.arg1 = errorCode;
						handler.sendMessage(msg);//发送错误信息
					}*/
					Message msg = Message.obtain();
					if (errorCode == -1){
						msg.what = isNewVersion(parseJson);//检测是否有新版本
					} else {
						msg.what = ERROR;
						msg.arg1 = errorCode;
					}
					long endTime = System.currentTimeMillis();
					if (endTime - startTimeMillis < 3000){
						SystemClock.sleep(3000 - (endTime - startTimeMillis));//时间不超过3秒，补足3秒
					}
					handler.sendMessage(msg);//发送消息
					try {
						//关闭连接资源
						if (bfr == null || conn == null){
							return;
						}
						bfr.close();
						conn.disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

			}
		}).start();
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			//处理消息
			switch (msg.what) {
			case LOADMAIN://加载主界面
//				Toast.makeText(getApplicationContext(), "咩，已经是最新版本了！", 1).show();
				loadMain();
				break;
			case ERROR://有异常
				switch (msg.arg1) {
				case 404://资源找不到
				    Toast.makeText(getApplicationContext(), "咩，资源找不到", 0).show();
					break;
				case 4001://找不到网络
					Toast.makeText(getApplicationContext(), "咩，没有网络", 0).show();
					break;
				case 4003://json格式错误
					Toast.makeText(getApplicationContext(), "咩，json格式错误", 0).show();
					break;
				default:
					break;
				}
				loadMain();//进入主界面
				break;
			case SHOWUPDATEDIALOG://显示更新版本的对话框
				showUpdateDialog();
				break; 
			default:
				break;
			}
		}

	};
	

	private void loadMain() {
		Intent intent = new Intent(SplashActivity.this,MainActivity.class);
		startActivity(intent);//进入主界面
		finish();//关闭自己
	};
	/**
	 * 在子线程中执行
	 * @param parseJson
	 */
	protected int isNewVersion(UrlBean parseJson) {
		int serverCode = parseJson.getVersionCode();// 获取服务器的版本
		
		if (serverCode == versionCode){//版本一致
			return LOADMAIN;
			/*//进入主界面
			Message msg = Message.obtain();
			msg.what = LOADMAIN;
			handler.sendMessage(msg);*/
		} else {//有新版本  
			return SHOWUPDATEDIALOG;
		}
	}

	/**
	 * 显示是否更新新版本的对话框
	 */
	protected void showUpdateDialog() {
		// TODO Auto-generated method stub
		//对话框的上下文 是Activity的class,AlertDialog是Activity的一部分
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//让用户禁用取消操作
		//builder.setCancelable(false);
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				//取消的事件处理
				//进入主界面
				loadMain();
			}
		})
			   .setTitle("提醒")
		       .setMessage("新版本的具有如下特性：" + parseJson.getDesc())
		       .setPositiveButton("更新", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//更新apk
					System.out.println("更新apk");
					//访问网络，下载新的apk
					Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("https://a1260157543.github.io/ball/Basketball.apk"));
					it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
					startActivity(it);
					//downLoadNewApk();//下载新版本
				}
			}).setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//进入主界面
					loadMain();
				}
			});
		builder.show();//显示对话框
	}

	/**
	 * 新版本的下载安装
	 */
	protected void downLoadNewApk() {
		HttpUtils utils = new HttpUtils();
		//parseJson.getUrl() 下载的url
		// target  本地路径
		System.out.println(parseJson.getUrl());
		File file = new File("/mnt/sdcard/xx.apk");
		file.delete();//删除文件
		utils.download(parseJson.getUrl(), "/mnt/sdcard/xx.apk", new RequestCallBack<File>() {
			
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				pb_download.setVisibility(View.VISIBLE);//设置进度的显示
				pb_download.setMax((int) total);//设置进度条的最大值
				pb_download.setProgress((int) current);//设置当前进度
				super.onLoading(total, current, isUploading);
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				// TODO Auto-generated method stub
				//下载成功
				//在主线程中执行
				Toast.makeText(getApplicationContext(), "下载新版本成功", 1).show();
				//安装apk
				installApk();//安装apk
				pb_download.setVisibility(View.GONE);//隐藏进度条
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				//下载失败
				Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
				pb_download.setVisibility(View.GONE);//隐藏进度条
			}
		});
	}

	/**
	 * 安装下载的新版本
	 */
	protected void installApk() {
		/*<intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:scheme="content" />
        <data android:scheme="file" />
        <data android:mimeType="application/vnd.android.package-archive" />
    </intent-filter>*/
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String type = "application/vnd.android.package-archive";
		Uri data = Uri.fromFile(new File("/mnt/sdcard/xx.apk"));
		intent.setDataAndType(data , type);
		startActivityForResult(intent,0);
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//如果用户取消更新apk，那么直接进入主界面
		loadMain();
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * @param jsonString
	 *            url的json数据
	 * @return url信息封装对象
	 * @throws JSONException
	 */
	protected UrlBean parseJson(StringBuilder jsonString) throws JSONException {
		UrlBean bean = new UrlBean();
		JSONObject jsonObj;
		
		// {"version":"2","url":"http://10.0.2.2:8080/xxx.apk","desc":"增加了防病毒功能"}
		jsonObj = new JSONObject(jsonString + "");
		int versionCode = jsonObj.getInt("version");
		String url = jsonObj.getString("url");
		String desc = jsonObj.getString("desc");
		// 封装结果数据
		bean.setDesc(desc);
		bean.setUrl(url);
		bean.setVersionCode(versionCode);
		

		return bean;
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		setContentView(R.layout.update);
		rl_root = (LinearLayout) findViewById(R.id.rl_splash_root);
		tv_versionName = (TextView) findViewById(R.id.tv_splash_version_name);
		pb_download = (ProgressBar) findViewById(R.id.pb_splash_download_progress);
	}

	/**
	 * 动画显示
	 */
	private void initAnimation() {
		// showAlpha();
		// Alpha动画0.0完全透明
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		// 设置动画播放的时间 毫秒为单位
		aa.setDuration(2000);
		// 界面停留在动画结束状态
		aa.setFillAfter(true);

		RotateAnimation rotate = new RotateAnimation(0, 360,
				// 设置锚点
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 显示时间
		rotate.setDuration(2000);
		// 界面停留在结束状态
		rotate.setFillAfter(true);

		ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
				// 设置锚点
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 显示时间
		sa.setDuration(2000);
		// 界面停留在结束状态
		sa.setFillAfter(true);

		// 创建动画
		AnimationSet as = new AnimationSet(true);

		as.addAnimation(sa);
		//as.addAnimation(rotate);
		//as.addAnimation(aa);
		// 同时播放动画
		rl_root.startAnimation(as);

	}

	/**
	 * Alpha显示
	 */
	private void showAlpha() {
		// Alpha动画0.0完全透明
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		// 设置动画播放的时间毫秒为单位
		aa.setDuration(3000);
		// 界面停留在动画结束状态
		aa.setFillAfter(true);
		// 给组件设置动态
		rl_root.startAnimation(aa);
	}

}
