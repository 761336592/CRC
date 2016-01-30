package com.example.CRC;
import java.io.File;

import java.io.InputStream;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;




import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Welcome extends Activity{
	private	Button SY;
	 private	Button registbButton;
	 public  OutputStream os;
	    public InputStream is; 	
	    // private  Handler mHandler;
	    String Text_of_output="";
	    SYS sys=new SYS();
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xz);
		SysApplication.getInstance().addActivity(this);
		//Interface="Welcome";  //��ʾ�ڻ�ӭ����
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);	//���ܺ���
		
		/*��ȡע������ð���*/
		SY=(Button)findViewById(R.id.main_login_btn);       
		registbButton=(Button)findViewById(R.id.main_regist_btn);
		/*��������*/
		registbButton.setOnClickListener(regist);
		SY.setOnClickListener(login);
		
		
		
	}  
	/*ע�ᰴ�������¼�:�����ע������¾���ת��ע�����*/
	private OnClickListener regist=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
			Welcome.this.finish();
			 Intent intent=new Intent();
			
			     
			intent.setClass(Welcome.this, Register.class);
				startActivity(intent);
		}
	};
	/*���ü����£���Ҫ�ж��Ƿ��ڣ�������ھ���ʾ���ڣ�����ͽ��뵽������*/
	private OnClickListener login=new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
			int number=0;
			SimpleDateFormat   sDateFormat;   //ʱ��
			String str="";
			 SQLiteDatabase db =LoginOpenHelper.getInstance(Welcome.this,log.DB_NAME).getReadableDatabase();
			Cursor cursor=null;
			cursor = db.query("ZC",null,null, null, null, null,null);
			if(cursor.getCount()==0){  //���ݿ��ļ���ɾ����
				 mHandler.sendEmptyMessage(5);  //��ʾʱ�䵽 
			}else{
				cursor.moveToNext();
				str=cursor.getString(cursor.getColumnIndex("d")).toString().trim();
				sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd");
				String date = sDateFormat.format(new   java.util.Date());
				try {
					Date now=sDateFormat.parse(date);
					Date last=sDateFormat.parse(str);
					 System.out.print(last);
					 long timeLong = now.getTime() - last.getTime();
					 timeLong=timeLong/1000/ 60 / 60 / 24;
					 System.out.print(timeLong);
					 if(timeLong>=10){
						 mHandler.sendEmptyMessage(5);  //��ʾʱ�䵽 
				     }
				      else{
				 			Intent intent=new Intent(); 
						    intent.setClass(Welcome.this, YGDL.class);
							startActivity(intent);	  
					  }
				} catch (ParseException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				
			}
		/*	Text_of_output="";
			Text_of_output=sys.My_ReadFile(is,"Date",Welcome.this);
			//Toast.makeText(Welcome.this,  Text_of_output.length()+"", Toast.LENGTH_SHORT).show();
			String numString="";
			if(Text_of_output.trim().length()==0){
				number+=1;
				numString=number+"";
				sys.My_WriteFile("Date",numString,Welcome.this,os);
				sys.My_WriteFile("SYFG", "N",Welcome.this,os);
				Welcome.this.finish();
				Intent intent=new Intent();
				 intent.setClass(Welcome.this,YGDL.class);
				startActivity(intent);	
				//mHandler.sendEmptyMessage(2);
			}else if(Text_of_output.length()>0){
				if(Integer.parseInt(Text_of_output.trim())>30){
					 mHandler.sendEmptyMessage(5);  //��ʾʱ�䵽 
			   }
			   else{
 
			    	     numString="";
			    	     number=Integer.parseInt(Text_of_output.trim())+1;
			    	     numString=number+"";
			    	     sys.My_WriteFile("Date",numString,Welcome.this,os);
			 			sys.My_WriteFile("SYFG", "N",Welcome.this,os);
			 			
			 			Intent intent=new Intent();
						 
					    intent.setClass(Welcome.this, YGDL.class);
						startActivity(intent);	
					   
				 }
			}	*/
		  }
		
	};
	public void gotoTs(){
		setContentView(R.layout.ts);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);	//���ܺ���
	
		Button registButton=(Button)findViewById(R.id.exitBtn0);
		Button exitButton=(Button)findViewById(R.id.exitBtn1);
		registButton.setOnClickListener(regist);
		exitButton.setOnClickListener(exit);
	}
	/*�˳��������¼�*/
	private OnClickListener exit=new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
		//	System.exit(1);
			SysApplication.getInstance().exit();
		}

	};
	Handler mHandler = new Handler() {
		// ʵ����Handler��������Ҫ����handleMessage�����������յ�����Ϣ
		
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case 5:
				  gotoTs();
				  break;
			}
		}
	};
public boolean onKeyDown(int keyCode, KeyEvent event)  {  
		
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
        	SysApplication.getInstance().exit();

        }else if(keyCode == KeyEvent.KEYCODE_MENU) {
           return false;
        }
        return true; 
    } 
}