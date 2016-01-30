package com.example.CRC;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.io.OutputStream;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity{
  EditText	pass;
  public static final String bm = "GBK";
  private Context mContext;
  private   String	androidID="";
  public  OutputStream os;
  public InputStream is; 	
  
	protected void onCreate(Bundle savedInstanceState){
	   
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zc);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);	//���ܺ���
		 TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	     androidID=telephonyManager.getDeviceId();
		Button regisButton=(Button)findViewById(R.id.zc);
		Button returButton=(Button)findViewById(R.id.fh);
		EditText user=(EditText)findViewById(R.id.login_user_edit);
		 
		user.setText(androidID);
		//user.setEnabled(false);
		 pass=(EditText)findViewById(R.id.login_passwd_edit);
		returButton.setOnClickListener(returnClickListener);
		regisButton.setOnClickListener(regisClickListener);
	
	}
	/*���ذ�������*/
	   private OnClickListener returnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.exit(1);
			Intent intent=new Intent();
			
		     
			intent.setClass(Register.this, Welcome.class);
				startActivity(intent);
			// TODO �Զ����ɵķ������
			//mHandler.sendEmptyMessage(3);
		}
	};

	/*ע�ᰴ������*/
	private OnClickListener regisClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
			
			String string="";
			int len=0;
			string=pass.getText().toString();
			if(string.length()<0)
			{
				Toast.makeText(Register.this, "ע���벻��Ϊ�գ�", Toast.LENGTH_SHORT)
				.show();
				
			}else{
				try {
					 len = SYS.crc_r(androidID.getBytes(bm),androidID.getBytes(bm).length);
					 len=len%99;
				} catch (UnsupportedEncodingException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				
				String s=String.format("%02d",len); 
				String pasString=androidID.substring(1, 2)+s+androidID.substring(9, 10)+androidID.substring(5,6)+androidID.substring(6, 7);
				System.out.println("����:"+pasString);
				if(pasString.equals(string)){
				
					SQLiteDatabase dw =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
					 ContentValues values=new ContentValues(); 
					 values.put("mm", string);	
					 String where = "zcm = ? " ;
						String[] whereValue = {androidID};
						dw.update("ZC", values, where, whereValue);
					log.isRes=true;
					Register.this.finish();
					Intent intent=new Intent();
					intent.setClass(Register.this,YGDL.class);
					startActivity(intent);
				}else{
					Toast.makeText(Register.this, "ע�������", Toast.LENGTH_SHORT)
					.show();
					
				}
			}
		}
	};
public boolean onKeyDown(int keyCode, KeyEvent event)  {  
		
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
        	System.exit(1);

        }else if(keyCode == KeyEvent.KEYCODE_MENU) {
           return false;
        }
        return true; 
    } 
	
	
	
}
