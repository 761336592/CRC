/**
 * (1)判断是否是首次加载应用--采取读取SharedPreferences的方法 
 * (2)是，则进入引导activity；否，则进入log
 */
package com.example.CRC;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * @author Administrator
 *
 */
public class SplashActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    if(!log.pdir.exists()){
			  if(!log.pdir.mkdir()){
				  new AlertDialog.Builder(SplashActivity.this).setTitle(R.string.SYS)//设置对话框标题  
				     .setPositiveButton(R.string.CJSB,new DialogInterface.OnClickListener() {//添加确定按钮  
				         @Override  
				           public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
				             return ;   
				         }  
				     }).show();//在按键响应事件中显示此对话框  
				  this.finish();
				  return ;
			  }
			} 
			if(!log.fdir.exists()){
				log.fdir.mkdir();
			}
	    SysApplication.getInstance().addActivity(this);
	    boolean mFirst = isFirstEnter(SplashActivity.this,SplashActivity.this.getClass().getName()); 
	    if(mFirst) 
            mHandler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY,500); 
        else 
            mHandler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY,500); 
	  
	}
	//**************************************************************** 
    // 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段 
    //**************************************************************** 
    private static final String SHAREDPREFERENCES_NAME = "my_pos"; 
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity"; 
    private boolean isFirstEnter(Context context,String className){ 
        if(context==null || className==null||"".equalsIgnoreCase(className))return false; 
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(KEY_GUIDE_ACTIVITY, "");//取得所有类名 如 com.my.MainActivity 
        if(mResultStr.equalsIgnoreCase("false")) 
            return false; 
        else 
            return true; 
    } 
    //************************************************* 
    // Handler:跳转至不同页面 
    //************************************************* 
    private final static int SWITCH_MAINACTIVITY = 1000; 
    private final static int SWITCH_GUIDACTIVITY = 1001; 
    public Handler mHandler = new Handler(){ 
        public void handleMessage(Message msg) { 
            switch(msg.what){ 
            case SWITCH_MAINACTIVITY: 
                Intent mIntent = new Intent(); 
                mIntent.setClass(SplashActivity.this, log.class); 
                SplashActivity.this.startActivity(mIntent); 
                SplashActivity.this.finish(); 
                break; 
            case SWITCH_GUIDACTIVITY: 
                mIntent = new Intent(); 
                mIntent.setClass(SplashActivity.this,GuideActivity.class); 
                SplashActivity.this.startActivity(mIntent); 
                SplashActivity.this.finish(); 
                break; 
            } 
            super.handleMessage(msg); 
        } 
    };
	
}
