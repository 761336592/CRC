/**
 * (1)�ж��Ƿ����״μ���Ӧ��--��ȡ��ȡSharedPreferences�ķ��� 
 * (2)�ǣ����������activity���������log
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
				  new AlertDialog.Builder(SplashActivity.this).setTitle(R.string.SYS)//���öԻ������  
				     .setPositiveButton(R.string.CJSB,new DialogInterface.OnClickListener() {//���ȷ����ť  
				         @Override  
				           public void onClick(DialogInterface dialog, int which) {//ȷ����ť����Ӧ�¼�  
				             return ;   
				         }  
				     }).show();//�ڰ�����Ӧ�¼�����ʾ�˶Ի���  
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
    // �ж�Ӧ���Ƿ���μ��أ���ȡSharedPreferences�е�guide_activity�ֶ� 
    //**************************************************************** 
    private static final String SHAREDPREFERENCES_NAME = "my_pos"; 
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity"; 
    private boolean isFirstEnter(Context context,String className){ 
        if(context==null || className==null||"".equalsIgnoreCase(className))return false; 
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(KEY_GUIDE_ACTIVITY, "");//ȡ���������� �� com.my.MainActivity 
        if(mResultStr.equalsIgnoreCase("false")) 
            return false; 
        else 
            return true; 
    } 
    //************************************************* 
    // Handler:��ת����ͬҳ�� 
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
