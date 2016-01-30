package com.example.CRC;

import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ScanSetting extends Activity{
	 private Context mContext;
	 String Text_of_output="";
	 SYS sys=new SYS();
	 Vibrator vibrator;
	 private	Button blackButton;
	 private  OutputStream os;
	 private InputStream is; 
	 private RadioGroup Scangroup ;
	 private RadioButton SButton;    //允许
	 private RadioButton NButton;   
	
	 private RadioGroup CKcangroup ;
	 private RadioButton SCKButton; 
	 private RadioButton NCKButton; 
	
	 private RadioGroup SLcangroup ;
	 private RadioButton MRButton; 
	 private RadioButton SRButton; 
	 
	 private RadioGroup DHcangroup ;
	 private  RadioButton SDHButton; 
	 private RadioButton NDHButton; 	
	 private RadioButton ADHButton; 	 //
	 
	 
	 private String isReScan="";    //是否允许重复扫描；
	 private String isCK="";       //是否需要仓库
	 private String isDH="";       //是否需要单号
	 private String isNUM="";       //是否需要单号
	 
	 private String isSJ="";       //是否需要单号
	 private RadioGroup SJcangroup ;
	 private RadioButton XGButton;  //修改
	 private RadioButton BGButton;   //不修改
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_setting);
		 getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		 
		 blackButton=(Button)findViewById(R.id.Scan_btn);
		 blackButton.setOnClickListener(blackClickListener);
		 Scangroup =(RadioGroup)findViewById(R.id.xz);
	 	 SButton=(RadioButton)findViewById(R.id.qs);
	 	 NButton=(RadioButton)findViewById(R.id.tj); 
	 	 isReScan=WriteReadSD.read("isReScan",log.fdir);
		 if(isReScan.trim().equals("0")){
			 Scangroup.check(R.id.tj);
		 }else{
			 Scangroup.check(R.id.qs);
		 }
	 	 Scangroup.setOnCheckedChangeListener(CFSM);
	 	 
	 	CKcangroup =(RadioGroup)findViewById(R.id.cksz);
	 	 SCKButton=(RadioButton)findViewById(R.id.xuck);
	 	 NCKButton=(RadioButton)findViewById(R.id.bxck); 
	 	 
	 	isCK=WriteReadSD.read("isCK",log.fdir);
	 	 if(isCK.trim().equals("0")){
	 		CKcangroup.check(R.id.xuck) ;
	 	 }else{
	 		CKcangroup.check(R.id.bxck) ;
	 	 }
	 	CKcangroup.setOnCheckedChangeListener(CK);
		
	 	DHcangroup =(RadioGroup)findViewById(R.id.dhsz);
	 	 SDHButton=(RadioButton)findViewById(R.id.xudh);
	 	 NDHButton=(RadioButton)findViewById(R.id.bxdh); 
		 ADHButton=(RadioButton)findViewById(R.id.zddh); 
		 isDH=WriteReadSD.read("isDH",log.fdir);
	 	 if(isDH.trim().equals("0")){
	 		DHcangroup.check(R.id.xudh) ;
	 	 }else if(isDH.trim().equals("1")){
	 		DHcangroup.check(R.id.bxdh) ;
	 	 }else{
	 		DHcangroup.check(R.id.zddh) ; 
	 	 }
	 	DHcangroup.setOnCheckedChangeListener(DH);
	 	
	 	SLcangroup =(RadioGroup)findViewById(R.id.SL);
	 	 SRButton=(RadioButton)findViewById(R.id.srsl);
	 	 MRButton=(RadioButton)findViewById(R.id.mrsl); 
	 	isNUM=WriteReadSD.read("isNUM",log.fdir);
	 	
	 	if(isNUM.trim().equals("0")){
	 		SLcangroup.check(R.id.srsl) ;
	 	 }else if(isNUM.trim().equals("1")){
	 		SLcangroup.check(R.id.mrsl) ;
	 	 }
	 	SLcangroup.setOnCheckedChangeListener(SL);
	 	
	 	SJcangroup=(RadioGroup) findViewById(R.id.cksetsj);
	 	XGButton=(RadioButton) findViewById(R.id.xysj);
	 	BGButton=(RadioButton) findViewById(R.id.bxsj);
	 	isSJ=WriteReadSD.read("isSj",log.fdir);
	 	if(isSJ.trim().equals("0")){
	 		SJcangroup.check(R.id.xysj);
	 	}else{
	 		SJcangroup.check(R.id.bxsj);
	 	}
	 	SJcangroup.setOnCheckedChangeListener(sj);
	}
private OnCheckedChangeListener sj=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO 自动生成的方法存根
			int id=group.getCheckedRadioButtonId();
			switch(group.getCheckedRadioButtonId()){
			case R.id.xysj :
				WriteReadSD.writex("0","isSj",log.fdir);
			    break;
			case R.id.bxsj:
				WriteReadSD.writex("1","isSj",log.fdir);
			    break;
			}

		}
	};
	/*是否允许重复扫描*/
	private OnCheckedChangeListener CFSM=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO 自动生成的方法存根
			int id=group.getCheckedRadioButtonId();
			switch(group.getCheckedRadioButtonId()){
			case R.id.qs :
				 WriteReadSD.writex("1","isReScan",log.fdir);
			 //   sys.My_WriteFile("isReScan", "不允许", ScanSetting.this, os);
			    break;
			case R.id.tj:
				WriteReadSD.writex("0","isReScan",log.fdir);
				  //sys.My_WriteFile("isReScan", "允许", ScanSetting.this, os);
			    break;
			}

		}
	};
	/*是否需要仓库号*/
private OnCheckedChangeListener CK=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO 自动生成的方法存根
			int id=group.getCheckedRadioButtonId();
			switch(group.getCheckedRadioButtonId()){
			case R.id.xuck :
				WriteReadSD.writex("0","isCK",log.fdir);
			    break;
			case R.id.bxck:
				WriteReadSD.writex("1","isCK",log.fdir);
			    break;
			}

		}
	};
private OnCheckedChangeListener SL=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO 自动生成的方法存根
			int id=group.getCheckedRadioButtonId();
			switch(group.getCheckedRadioButtonId()){
			case R.id.mrsl :
				WriteReadSD.writex("1","isNUM",log.fdir);
			   // sys.My_WriteFile("isNUM", "不需要", ScanSetting.this, os);
			    break;
			case R.id.srsl:
				WriteReadSD.writex("0","isNUM",log.fdir);
				  //sys.My_WriteFile("isNUM", "需要", ScanSetting.this, os);
			    break;
			}

		}
	};	
	/*是否需要单号*/
private OnCheckedChangeListener DH=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO 自动生成的方法存根
			int id=group.getCheckedRadioButtonId();
			switch(group.getCheckedRadioButtonId()){
			case R.id.xudh :
				WriteReadSD.writex("0","isDH",log.fdir);
			   // sys.My_WriteFile("isDH", "输入", ScanSetting.this, os);
			    break;
			case R.id.bxdh:
				WriteReadSD.writex("1","isDH",log.fdir);
				  //sys.My_WriteFile("isDH", "不需要", ScanSetting.this, os);
			    break;
			case R.id.zddh:
				WriteReadSD.writex("2","isDH",log.fdir);
				 // sys.My_WriteFile("isDH", "自动", ScanSetting.this, os);
			    break;    
			}
		}
	};
	
	
	 private OnClickListener blackClickListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				  ScanSetting.this.finish();
				  //System.exit(1);
			}
		};	
public boolean onKeyDown(int keyCode, KeyEvent event)  {  
		
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
        	 ScanSetting.this.finish();
             // SysApplication.getInstance().exit();
        }else if(keyCode == KeyEvent.KEYCODE_MENU) {
           return false;
        }
        return true; 
    } 
}
