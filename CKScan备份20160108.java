package com.example.CRC;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.Player.MyPlayer;
import com.mining.app.socket.mySocket;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class CKScan extends Activity{
    
	/*各种控件的定义*/
	private TextView dis;       //接收数据显示句柄
    private ScrollView sView;
    private  EditText barcode;   //条码
    private  EditText KCsl;      //库存
    private Button sendButton;   //确定按钮
   	private TextView btzt;  //蓝牙状态
   	private TextView wc;   //未传数量
    private TextView yc;   //已传数量
    private TextView BTpower; //蓝牙电量
    private Button  scanButton;
   	private Button  ConnectButton;
	private EditText  NUM;
   	private Button  sjsm;
  
    private  EditText DH;        //单号
    private  EditText CKys;      //以扫
    private  EditText CKsj;      //售价
    private String androidID="";
   	private String  Text_of_output="";
   	private String recvMessageserver ="";
   	private String recvMessageClient = "";
   	private static Context mContext=null;
    private String iPString="";
	private String portString="0";
	private String CKString="";
	private String dhString="";
	private String rkNum="";
	float  RKnum=0; 
	private static final String bm = "GBK";
	private static final String BTNAME = "RS-";
	
	private  Vibrator vibrator=null;
	 
	private final static int SCANNIN_GREQUEST_CODE = 1;
	public static final  int REQUEST_CONNECT_DEVICE = 2;    //宏定义查询设备句柄
	private static final int REQUEST_ENABLE_BT = 3;
	private  BluetoothDevice _device = null;     //蓝牙设备
	
	boolean isconnect=false;
	boolean btSent=false;       //是蓝牙
	boolean kcflag=false;
	boolean isSearchinf=false;
	boolean isSearchOK=false;
	private int Running=21;
	private boolean isSent=false;
	private MyPlayer player=null;
	
	private mySocket mysocket=null;
	private String isReScan="";    //是否允许重复扫描；
	private String isNUM="";      //是否需要数量
	private String BH="";
	private String SentData="";
	private String fwq="";
	private SimpleDateFormat  sDateFormat;   //时间
	private String   date="" ;
	
	private ImageButton DHimag;
	private PopupWindow popView;
	private ArrayAdapter<String> mAdapter;  
	private ListView mListView; 
	ArrayList<String> dhdata=null;
	ArrayList<String> ckdata=null;
	private boolean mShowing;
    /*显示资料抬头的*/
	private TextView textView01;
	private TextView textView02;
	private TextView textView03;
	private TextView textView04;
	private TextView textView05;
	private TextView textView06;
	private TextView textView07;
	/*显示内容的*/
	private TextView textView1;    
	private TextView textView2;
	private TextView textView3;
	private TextView textView4;
	private TextView textView5;
	private TextView textView6;
	private TextView textView7;
	/*有可能是蓝牙的名称是空的所以进不去 */
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Running=21;
		mContext=this;
		 TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	     androidID=telephonyManager.getDeviceId();
		 String[] str=null;
		/*读取文件获取员工编号和IP，端口号*/
		 setContentView(R.layout.main_tab_address);
		 btzt=(TextView) findViewById(R.id.ckbt);
		 dis=(TextView) findViewById(R.id.in);
		 yc=(TextView)findViewById(R.id.ckyc);
		 wc=(TextView)findViewById(R.id.ckwc);
		 sView=(ScrollView)findViewById(R.id.ScrollView01);
		 NUM=(EditText)findViewById(R.id.CKNUM);
		
		 DH=(EditText)findViewById(R.id.CKDH);
		 CKsj=(EditText)findViewById(R.id.sj);
		 CKys=(EditText)findViewById(R.id.ckyC);
			
		 vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE); 
		 player=new MyPlayer(mContext,vibrator);

		 BTpower=(TextView)findViewById(R.id.ckdl);
		 BTpower.setText(R.string.power);
		 barcode=(EditText)findViewById(R.id.et_sendmessage);
		 
	
		 ConnectButton=(Button)findViewById(R.id.pjlj);
		 sendButton=(Button)findViewById(R.id.btn_send);
		 ConnectButton=(Button)findViewById(R.id.pjlj);
		 scanButton=(Button)findViewById(R.id.cksjsm);
		 
		 sendButton=(Button)findViewById(R.id.btn_send);
		 Button search=(Button)findViewById(R.id.pjss);
		 sjsm=(Button)findViewById(R.id.cksjsm);
		 
		 Button searchOkButton=(Button) findViewById(R.id.pjsm);
		 searchOkButton.setOnClickListener(DHWC);
		 
		 isReScan=WriteReadSD.read("isReScan",log.fdir);
		 isNUM=WriteReadSD.read("isNUM",log.fdir);
		 if(isNUM.trim().equals("1")){
			 NUM.setText("1");
		 } 
		NUM.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		DHimag=(ImageButton) findViewById(R.id.ckImageButton01);
		DHimag.setOnClickListener(dhin);
		//initDH();
		DH.requestFocus();
		DH.setSelectAllOnFocus(true);	
		DH.addTextChangedListener(textWatcher2);
		barcode.addTextChangedListener(textWatcher);
		barcode.setSelectAllOnFocus(true);
		
        textView01=(TextView) findViewById(R.id.YDZL1);
        textView02=(TextView) findViewById(R.id.YDZL2);
        textView03=(TextView) findViewById(R.id.YDZL3);
        textView04=(TextView) findViewById(R.id.YDZL4);
        textView05=(TextView) findViewById(R.id.YDZL5);
        textView06=(TextView) findViewById(R.id.YDZL6);
        textView07=(TextView) findViewById(R.id.YDZL7);
      //  textView08=(TextView) findViewById(R.id.YDZL8);
        
        textView1=(TextView) findViewById(R.id.YDZL01);
        textView2=(TextView) findViewById(R.id.YDZL02);
        textView3=(TextView) findViewById(R.id.YDZL03);
        textView4=(TextView) findViewById(R.id.YDZL04);
        textView5=(TextView) findViewById(R.id.YDZL05);
        textView6=(TextView) findViewById(R.id.YDZL06);
        textView7=(TextView) findViewById(R.id.YDZL07);
      //  textView8=(TextView) findViewById(R.id.YDZL08);
        
		yc.setText(R.string.Transferall);
		wc.setText(R.string.Transferpovit);
		yc.append(MainActivity.sentpovit+""); 
		wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
		vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
	  	scanButton.setOnClickListener(scanClickListener);
	  	if(!RKScan.Isbt){
	    	btzt.setText(R.string.title_not_connected);
	    }else{
	       btzt.setText(R.string.title_connected_to);
	    }
	  	
	  	Text_of_output="";
		 Text_of_output=WriteReadSD.read("IP",log.fdir);
	  	 if(Text_of_output.length()>0){
	  		str=SYS.splitString(Text_of_output,":");
	  		iPString=str[0].trim();
	  		portString=str[1].trim();
	  	 }else{
	  		 SYS.disPlay(sView, dis, getString(R.string.Noset)+"\n");   //显示数据 
	  	 }
	  	Text_of_output="";
	  	Text_of_output=WriteReadSD.read("FWQM",log.fdir);
	 	 if(Text_of_output.length()>0){
	 		fwq=Text_of_output.trim();
	 	 }else{
	 		fwq="RS";
	 	 }
	  	mysocket=new mySocket(mContext, mHandler,iPString,Integer.parseInt(portString));
	 	if(iPString.length()>0&&mysocket.getState()==mySocket.STATE_NONE){
	  		mysocket.connect();
	  	}else{
	  		ConnectButton.setText(R.string.SKconnected);
	  	}
	  	if(iPString.length()>0){
	  		ConnectButton.setOnClickListener(socketconnect);
	  	}
	
		mHandler.postDelayed(btRunnable, 5000);
		mHandler.postDelayed(socketRunnable,50);
	
		mHandler.postDelayed(socketConnect,1000*60);
	    sendButton.setOnClickListener(sent);
	    btzt.setOnClickListener(Btset);
		search.setOnClickListener(searchon);
		sjsm.setOnClickListener(scanClickListener);
}
/*监听单号框的字符变化，当检测到回车的时候  条码框就获取焦点*/
private TextWatcher textWatcher2=new TextWatcher(){
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO 自动生成的方法存根
		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO 自动生成的方法存根
		
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO 自动生成的方法存根
		
		 if(SYS.indexofString(s.toString().trim(),"\n")){
			 player.playScanOK(R.raw.beep);
			 new Handler().postDelayed(new  Runnable() {
					public void run() {
						barcode.requestFocus();
					}
				},100);
		 }
	}
};	
/*条码框的字符变化监听*/
private TextWatcher textWatcher=new TextWatcher() {
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO 自动生成的方法存根
		
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO 自动生成的方法存根
		
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO 自动生成的方法存根
		for(int i=0;i<s.toString().length();i++){
			System.out.println(s.toString().getBytes()[i]);
		}
	//	System.err.println(s.toString().getBytes()[s.toString().length()-1]);
	/*	 new Handler().postDelayed(new  Runnable() {
				public void run() {
					  
				}
			},100);*/
		if(s.toString().length()>0 && s.toString().getBytes()[s.toString().length()-1]==10){
			  System.err.println("接收到的数据是："+s.toString());
			
			  CKString="CK001";
			//  player.playScanOK(R.raw.beep);	
	            if(DH.getText().toString().trim().length()==0){
					dhString="DH001";
				}else{
					dhString=DH.getText().toString().trim();
				}
				     /*检测到回车之后*/
				     /*判断是否允许重复扫描*/
				 String barcodeString=barcode.getText().toString().replace("\n"," ").trim();
				  if(isReScan.trim().equals("1")){
				      SQLiteDatabase db1=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
				      Cursor cursor2=null;
				      cursor2=db1.rawQuery("select * from CK where BAR=? and CK=? and DH=? ", new String[]{barcodeString,CKString,dhString});
				      if(cursor2.getCount()!=0){
					    	barcode.setText("");  
					    	dis.setText(getString(R.string.RScan)+"\n");
					    	player.playScanOK(R.raw.fail);
					    	cursor2.close();
					    	db1.close();
					    	return ;
				      }
                } 
                 dis.setText("");
   				 sView.scrollTo(0,dis.getMeasuredHeight());
   			
     			textView1.setText("");
     			textView2.setText("");
     			textView3.setText("");
     			textView4.setText("");
     			textView5.setText("");
     			textView6.setText("");
     			textView7.setText("");
     			//textView8.setText(""); 
                 
     			CKsj.setText("0");
     			CKys.setText("0");
     			String bufString="QUERY□"+barcodeString+"□"+dhString+"□"+"YD"+"□"+dhString;
     			if(isconnect){
	               isSearchinf=true;
	            //   barcode.clearFocus();
	               mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
	                }else{
	               isSearchinf=true;
	               System.err.println("没有的数据");
				}

		}
	}
};	
/*查询单号是否完成,跳转到查询界面*/
private OnClickListener DHWC=new OnClickListener() {
	
	@Override
	public void onClick(View v){
		// TODO 自动生成的方法存根
		if(DH.getText().toString().length()>0){		
			Intent intentPk = new Intent(); 
			intentPk.putExtra("CXDH",DH.getText().toString());
			intentPk.setClass(CKScan.this,SearchOk.class);
			startActivityForResult(intentPk, 5);
		}else {
			Toast.makeText(mContext, "单号不能为空，请选择单号",Toast.LENGTH_SHORT).show();
		}
		
		 
	}
};

	/*服务器连接*/	
private OnClickListener socketconnect=new OnClickListener(){
		@Override
		public void onClick(View v){
			// TODO 自动生成的方法存根
			if(mysocket.getState()==mySocket.STATE_CONNECTED||isconnect){
				mysocket.stop();
				isconnect=false;
			}else if(mysocket.getState()==mySocket.STATE_NONE||mysocket.getState()==mySocket.STATE_LISTEN || !isconnect){
				isSent=false;
				mysocket.connect();
			}
		}
	};	
	

private OnClickListener searchon =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			btSent=false;
			CKString="CK001";
            if(DH.getText().toString().trim().length()==0){
				dhString="DH001";
			}else {
				dhString=DH.getText().toString().trim();
			}
            String barcodeString=barcode.getText().toString().replace("\n"," ").trim();
            if(isReScan.trim().equals("1")){
	            	 SQLiteDatabase db1=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
				      Cursor cursor2=null;
				      cursor2=db1.rawQuery("select * from CK where BAR=? and CK=? and DH=? ", new String[]{barcodeString,CKString,dhString});	      
				      if(cursor2.getCount()!=0){
				    	  dis.setText(getString(R.string.RScan)+"\n");
				    	  player.playScanOK(R.raw.fail);
				    	  cursor2.close();
				    	  db1.close();
				    	  return ;
				      }
           } 
           /**/
			textView1.setText("");
			textView2.setText("");
			textView3.setText("");
			textView4.setText("");
			textView5.setText("");
			textView6.setText("");
			textView7.setText("");
			CKsj.setText("0");
			CKys.setText("0");
			
            String bufString="QUERY□"+barcodeString+"□"+dhString+"□"+"YD"+"□"+dhString;
            barcodeString=barcode.getText().toString().trim();
              /*发送查询指令*/
          	  if(isconnect){
          		  isSearchinf=true;
          		  mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
          	  }else{
          		 isSearchinf=false;
          	  }
		}
};
	
private OnClickListener scanClickListener=new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent1 = new Intent();
		intent1.setClass(CKScan.this, MipcaActivityCapture.class);
		intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent1, SCANNIN_GREQUEST_CODE);
	}
};
/*条码扫描返回的结果处理*/
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    String bufString="";
    switch (requestCode) {
	case SCANNIN_GREQUEST_CODE:
		if(resultCode == RESULT_OK){
			Bundle bundle = data.getExtras();
			 barcode.setText(bundle.getString("result"));
			 btSent=false;
			CKString="CK001";	
	            if(DH.getText().toString().trim().length()==0){
					dhString="DH001";
				}else {
					dhString=DH.getText().toString().trim();
				}
	            player.playScanOK(R.raw.beep);
	             barcode.getText().toString().trim();
	             if(isReScan.trim().equals("1")){
	            	 SQLiteDatabase db1=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
				      Cursor cursor2=null;
				      cursor2=db1.rawQuery("select * from CK where BAR=? and CK=? and DH=? ", new String[]{barcode.getText().toString().trim(),CKString,dhString}); 
				      if(cursor2.getCount()!=0){
				    	  dis.setText(getString(R.string.RScan)+"\n");
				    	  player.playScanOK(R.raw.fail);
				    	  cursor2.close();
				    	  db1.close();
				    	  return ;
				      }
                }
    			CKString="CK001";	
                if(DH.getText().toString().trim().length()==0){
					dhString="DH001";
				}else {
					dhString=DH.getText().toString().trim();
				}
                bufString="QUERY□"+barcode.getText().toString()+"□"+dhString+"□"+"YD"+"□"+dhString;
                if(isconnect){
                	 isSearchinf=true;
                	 btSent=true;
               		mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
                }else{
                	isSearchinf=true;
   					   if(isNUM.trim().equals("1")){
	   						RK(barcode.getText().toString(),dhString,CKString,"1");
	   						KCsl.setText(Double.parseDouble(KCsl.getText().toString())-1+"");
	   						CKys.setText(Double.parseDouble(CKys.getText().toString())+1+"");
	   						MainActivity.sentall++;
							yc.setText(R.string.Transferall);
						 	wc.setText(R.string.Transferpovit);
						 	yc.append(MainActivity.sentpovit+""); 
						 	wc.append((MainActivity.sentall-MainActivity.sentpovit)+""); 
   					  }else if(isNUM.trim().equals("0")){
	   						NUM.setText("");
	   						NUM.setFocusable(true);
	   	                    NUM.setFocusableInTouchMode(true);
	   	                    NUM.requestFocus();
	   	                    InputMethodManager inputManager=(InputMethodManager)NUM.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	   	                    inputManager.showSoftInput(NUM, 0);
   					 }
                } 
		}
		break;
	case REQUEST_CONNECT_DEVICE:
		if(resultCode==RESULT_OK){
			 String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
              WriteReadSD.writex(address, "BTMAC",log.fdir);
			 _device= MainActivity._bluetooth.getRemoteDevice(address);
			 RKScan.mChatService.connect(_device);
		}
		break;
	case REQUEST_ENABLE_BT:
         // 判断蓝牙是否启用
         if (resultCode == Activity.RESULT_OK) {
             setupChat();
         } else{
             finish();
         }
         break;
	case 4:
		if(resultCode==RESULT_OK){
			String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
			_device= MainActivity._bluetooth.getRemoteDevice(address);
			WriteReadSD.writex(address, "PTMAC",log.fdir); //存放打印机的地址
			RKScan.printService.connect(_device);
		}
		break;
	case 5:
		if(resultCode==RESULT_OK){
			String string=data.getExtras().getString("result");
			if(string.equals("fail")){
				Toast.makeText(mContext,"查询失败",Toast.LENGTH_SHORT).show();
			}else if(string.equals("OVER")){
				Toast.makeText(mContext,"该订单已经完成",Toast.LENGTH_SHORT).show();
			}
		}	
		
		break; 
   }
}


private OnClickListener Btset =new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		RKScan.Isbt=false;
		//mHandler.removeCallbacks(btRunnable); 
		Intent serverIntent = new Intent(CKScan.this, DeviceListActivity.class); //跳转程序设置
		startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);  //设置返回宏定义*/
	}
};
private OnClickListener sent=new OnClickListener() {
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if(barcode.getText().toString().length()==0){
			new AlertDialog.Builder(CKScan.this).setTitle(R.string.SYS).setPositiveButton(R.string.nosent,new DialogInterface.OnClickListener() {//添加确定按钮  
		         @Override  
		         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
		              return ;   
		         }  
		     }).show();//在按键响应事件中显示此对话框  
			return ;
		}
				if(NUM.getText().toString().trim().length()>0){
					rkNum=NUM.getText().toString().trim();
				}else{
					rkNum="1";
				}
				try{
					Double.parseDouble(rkNum.trim());
				}catch(Exception e){
					/*提示数量输入错误*/
					new AlertDialog.Builder(CKScan.this).setTitle(R.string.SYS)//设置对话框标题  
				     .setPositiveButton(R.string.BXSZ,new DialogInterface.OnClickListener() {//添加确定按钮  
				         @Override  
				         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
				             return ;   
				         }  
				     }).show();//在按键响应事件中显示此对话框  
					return ;
				}
				CKString="CK001";
				if(DH.getText().toString().trim().length()>0){
					dhString=DH.getText().toString().trim();
				}else{
					dhString="DH001";
				}
				String barcodeString=barcode.getText().toString().replace("\n"," ").trim();	
				if(RK(barcodeString,dhString,CKString,rkNum)==0){
				    dis.setText(barcodeString+"\n");
			    	barcode.setText("");
			    	barcode.requestFocus();
			    	return ;
				}		
				CKys.setText(Integer.parseInt(CKys.getText().toString())+Integer.parseInt(rkNum)+"");
				MainActivity.sentall++;
				yc.setText(R.string.Transferall);
				wc.setText(R.string.Transferpovit);
				yc.append(MainActivity.sentpovit+""); 
				wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
				barcode.setText("");
				barcode.requestFocus();
				NUM.setText("");
	}
};
	
private Runnable printRunnable=new Runnable() {
	
	@Override
	public void run() {
		
		// TODO 自动生成的方法存根
	/*	 if(!RKScan.isPrint && RKScan.printService.getState()!=BluetoothChatService.STATE_CONNECTING){
			 connectprint();
		 }else {
			 
		 };
		 mHandler.postDelayed(this, 5000); */
	}
};

/* 检测蓝牙连接*/	
private Runnable btRunnable=new Runnable() {
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		 synchronized (this){
			 if(!RKScan.Isbt){
			    //  btzt.setText("扫描枪未开启");
			      if(RKScan.mChatService.getState()!=BluetoothChatService.STATE_CONNECTING)
			           connect();
			    }else{
			    
			    } 
			  mHandler.postDelayed(this, 5000); 
		 }
	}
};

/*定时检测数据库*/
private Runnable socketRunnable=new Runnable() {
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		 synchronized (this){
	
		 if(isconnect && MainActivity.sentall>MainActivity.sentpovit){
			Message msg1 = new Message();
			msg1.what =38;
			mHandler.sendMessage(msg1);
		 }
		  mHandler.postDelayed(this, 50); 
	   }
	}
};
/*定时检测 服务器是否连接 */
private Runnable socketConnect=new Runnable() {
	public void run() {
		if(mysocket.getState()==mySocket.STATE_NONE||mysocket.getState()==mySocket.STATE_LISTEN || !isconnect){
			mysocket.connect();
		}else{
			
		}
		mHandler.postDelayed(this,1000*60);
	}
};
/***********************************************************
 * 
 * 
 *                 消息处理
 * 
 * 
 * ***********************************************************/
Handler mHandler = new Handler(){
	 public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch(msg.what)
			{
			   case 38:
		    	 Log.i("进来",Running+"");
		    	 System.out.println(isSent);
		    	 SentData="";
		    	 BH="";
	             SQLiteDatabase db=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
				 Cursor cursor = null;
			      try{
						cursor =  db.query("SENT", new String[]{"BAR","ISsent","BH"}, "ISsent= ?", new String[]{"1"}, null, null, null);
					    if(cursor != null){
					    	   if(cursor.getCount()!=0 && cursor.moveToNext()){
					    		   SentData=cursor.getString(cursor.getColumnIndex("BAR"));
					    		   BH=cursor.getString(cursor.getColumnIndex("BH"));
								  if(MainActivity.sentall>MainActivity.sentpovit &&isconnect&& !isSent){
									 Log.i("发送的数据:"+isSent+"***", SentData+":"+BH);
									 mysocket.writeData(SYS.SenData(SentData,fwq,androidID,Running));
									 isSent=true;
								  }else{
									   System.out.println("我走了");
									   cursor.close();
									   db.close();
									   return ;
								  }	 
							 } 
					    }else{
					    	 Log.i("没有啊","*****");     
					    }
					}catch(Exception e){
						 Log.i("也没有啊","*****");
						
					}finally{
					    if(cursor != null){
					        cursor.close();
					    }
					}
			      db.close();
		    	  break;
			 case 16:
		    	 switch (msg.arg1) {
				case 0:
					ConnectButton.setText(R.string.Noconnected);
					dis.setText("");
					SYS.disPlay(sView, dis, getString(R.string.dk)+"\n");
					isconnect=false; //网络连接失败
					break;
				case 1:
					dis.setText("");
					ConnectButton.setText(R.string.Noconnected);
					SYS.disPlay(sView, dis, getString(R.string.dk)+"\n");
					isconnect=false;
					break;
				case 2:
					dis.setText("");
					SYS.disPlay(sView, dis, getString(R.string.connectting)+"\n");
					ConnectButton.setText(R.string.connectting);
					break;	
				case 3:
					dis.setText("");
					SYS.disPlay(sView, dis, getString(R.string.Sockted)+"\n");
					ConnectButton.setText(R.string.SKconnected);
					isconnect=true;
					break;	
				case 7:
					dis.setText("");
					ConnectButton.setText(R.string.Noconnected);
					SYS.disPlay(sView, dis, getString(R.string.IP)+"\n");
					isconnect=false;
					break;
				case 6:  //接收数据超时
				
					if(isSent){
						 System.out.println("接收流水超时了");
						 isSent=false;
						 if(Running>20) Running++;
					}
					break;
				  default:
					break;
				}
		    	 break; 
			 case 1:            //这个是蓝牙状态的,用蓝牙名称来区分是蓝牙枪还是打印机
	    	     String btname =""; 
	    	     btname=msg.getData().getString(MainActivity.DEVICE_NAME);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
				  if(SYS.indexofString(btname,BTNAME)||SYS.indexofString(btname,"HC")){
					 btzt.setText(R.string.title_connected_to);
					 RKScan.Isbt=true;
				  }	 
				/*  else{
					  RKScan.isPrint=true;
						 printTextView.setText(R.string.printer_connected_to);
				  }*/
					 break;
				case BluetoothChatService.STATE_CONNECTING:
					if(SYS.indexofString(btname,BTNAME)||SYS.indexofString(btname,"HC"))
					 btzt.setText(R.string.title_connecting);
					/*else{	
						 printTextView.setText(R.string.printer_connecting);
					 }*/
					 break;
				case BluetoothChatService.STATE_LISTEN:
					if(SYS.indexofString(btname,BTNAME)||SYS.indexofString(btname,"HC")){
						RKScan.Isbt=false;
					    btzt.setText(R.string.title_not_connected);
					} 
					/*else{
						RKScan.isPrint=false;
						  printTextView.setText(R.string.printer_not_connected);
					}*/
					 break;
				case BluetoothChatService.STATE_NONE:
					if(SYS.indexofString(btname,BTNAME)||SYS.indexofString(btname,"HC")){
				       btzt.setText(R.string.title_not_connected);
				       RKScan.Isbt=false;
					}
					 break;
				}
				break;	
		     case 4:
					// 保存该连接装置的名字
					String name1 = "";
					name1=msg.getData().getString(MainActivity.DEVICE_NAME);
					System.out.println("蓝牙名称是:"+name1);
					if(SYS.indexofString(name1,BTNAME)||SYS.indexofString(name1,"HC")){
						btzt.setText(R.string.title_connected_to);
						RKScan.Isbt=true;
					}
					break;
		     case 54:
	                byte[] writeBuf = (byte[]) msg.obj;
	                String writeMessage = new String(writeBuf);
	                System.out.println(writeMessage);
	                break;	
		     case 17: //获取接收到的数据
		       
		    	 String barcodeString=barcode.getText().toString().replace("\n"," ").trim();
		    	 String name = msg.getData().getString("REC");
		    	 String num="";
		    	 int len=0;
				 String sqlString=String.format("talk%s%s%s%s,","□",androidID,"□",barcodeString);
		    	 String [] string=null;
		    	 String [] buf=null;
		    	// System.out.println("收到的消息:"+name);
		          if(isSearchinf){
		        	  isSearchinf=false;
		        	  System.out.println("资料:"+name);
		        	  if(SYS.indexofString(name,sqlString)){
		        		  buf=SYS.splitString(name.trim(),"■");
		        		  if(!SYS.indexofString(buf[0].trim(),sqlString))//保证接收到的不是流水号+1
		        			  return ;
		        		  try {
								len=SYS.crc_r(buf[0].getBytes(bm),buf[0].getBytes(bm).length);
								len=len % 99;
								if(len==Integer.parseInt(buf[1].trim())){
									string=SYS.splitString(buf[0],",");
									if(string[1].equals("无--")){
									   textView1.setText(R.string.NOinft);
									   CKsj.setText(string[3].trim());
									   if(Integer.parseInt(string[3].trim())==0){
											player.playScanOK(R.raw.fail);
											dis.setText(barcodeString);
											barcode.setText("");
											new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
										     .setPositiveButton(R.string.NOBAR,new DialogInterface.OnClickListener() {//添加确定按钮  
										         @Override  
										         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件   
										             return ;   
										         }  
										     }).show();	
											barcode.requestFocus();
											return ;
										}
									   CKys.setText(string[5].trim());
									}else{
										dis.setText("");
										textView01.setText(string[1]);
										textView02.setText(string[3]);
										textView03.setText(string[5]);
										textView04.setText(string[7]);
										textView05.setText(string[9]);
										textView06.setText(string[11]);
										textView07.setText(string[13]);
										
										textView1.setText("  "+string[2]);
										textView2.setText("  "+string[4]);
										textView3.setText("  "+string[6]);
										textView4.setText("  "+string[8]);
										textView5.setText("  "+string[10]);
										textView6.setText("  "+string[12]);
										textView7.setText("  "+string[14]);
										/*同时把商品资料也写进数据库里面*/
										 SQLiteDatabase db1=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
								         Cursor cursor2=null;
								         cursor2=db1.rawQuery("select * from JBZL where INF1=?", new String[]{barcodeString});
								         if(cursor2.getCount()==0){
								        	String SqlString="insert into JBZL (INF1,INF2,INF3,INF4,INF5,INF6,INF7,INF8) values(?,?,?,?,?,?,?,?)";
								        	db1.execSQL(SqlString,new String[]{barcodeString,string[2],string[4],string[6],string[8],string[10],string[12],string[14]}) ;
								         }else{
								        	 String SqlString="UPDATE JBZL set INF2 = ?,INF3 = ?,INF4 = ?,INF5 = ?,INF6 = ?,INF7 = ?,INF8 = ? where INF1=?";
								        	 db1.execSQL(SqlString,new String[]{string[2],string[4],string[6],string[8],string[10],string[12],string[14],barcodeString}) ; 
								         }
								         cursor2.close();
								         db1.close();
										CKsj.setText(string[string.length-4]);
										/*当应扫是0的时候表示 这个条码不在单号里面,报警*/
										if(Integer.parseInt(string[string.length-4])==0){
											player.playScanOK(R.raw.fail);
											dis.setText(barcodeString);
											barcode.setText("");
											new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
										     .setPositiveButton(R.string.NOBAR,new DialogInterface.OnClickListener() {//添加确定按钮  
										         @Override  
										         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件   
										             return ;   
										        }  
										     }).show();	
											barcode.requestFocus();
											return ;
										}
										CKys.setText(string[string.length-2]);
									}
								   isSearchOK=true;
								}else{
									 /*查找失败*/
									dis.setText("");
									dis.setText("查询失败1\n");
									isSearchOK=false;
								}
							}catch (UnsupportedEncodingException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
								isSearchOK=false;
							}
		        	  }else{
		        		  dis.setText("");
						  dis.setText("查询失败2\n");
						  isSearchOK=false;
		        	  }
		        	  if(!isSearchOK){
		        		/*  seaCK(barcodeString,CKString);
		        		    Searsj(dhString,barcodeString);
	        				SearchINF(barcodeString);	 
	        				Searchys(barcodeString,dhString,CKString); 
	        			*/
		        	  }
		        	  if(isNUM.trim().equals("1")&&barcodeString.length()>0){	  	  
      				    if(RK(barcodeString,dhString,CKString,"1")==0) {
      				    	dis.setText(barcodeString+"\n");
      				    	barcode.setText("");
      				    	barcode.requestFocus();
      						return ;
      				    }
      				    dis.setText(barcodeString+"\n");
  						MainActivity.sentall++;
  						yc.setText(R.string.Transferall);
  					 	wc.setText(R.string.Transferpovit);
  					 	yc.append(MainActivity.sentpovit+""); 
  					 	wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
  					    CKys.setText(Integer.parseInt(CKys.getText().toString())+1+"");
  					 	barcode.setText("");
  				    	barcode.requestFocus();
  					}else  if(isNUM.trim().equals("0")){
  						NUM.setText("");
  						NUM.setFocusable(true);
  	                    NUM.setFocusableInTouchMode(true);
  	                    NUM.requestFocus();
  	                    InputMethodManager inputManager =
  	                       (InputMethodManager)NUM.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
  	                    inputManager.showSoftInput(NUM, 0);
  					}
		        	
		          }else{
		        	  isSent=false;
		        	  if(Running>=20) num="01";
				     	else num=String.format("%02d",Running+1);
				     	String bufx="talk"+ "□"+androidID+ "□"+num+"□";
				     	try{
				     		len=SYS.crc_r(bufx.getBytes(bm),bufx.getBytes(bm).length);
				     		len=len%99;
				     		bufx=bufx+"■"+String.format("%02d",len)+"■";
				     		if(bufx.equals(name)){
								 SQLiteDatabase dbe=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
								 ContentValues values = new ContentValues();
								 values.put("ISsent", "0");   //这个表示的是已传
								 dbe.update("SENT",values,"BAR=? and BH=?",new String[]{SentData,BH});
								 Log.e("条码:",SentData);
								 SentData="";
								 BH="";
								 if(Running>=20) Running=1;
								    else 
								       Running++;
								 if(MainActivity.sentpovit<MainActivity.sentall)
									 MainActivity.sentpovit++;
								     yc.setText("已传:"+MainActivity.sentpovit+"");
							 	     wc.setText("未传:"+(MainActivity.sentall-MainActivity.sentpovit)+"");
				     		}else{
				     			 if(Running>20) Running++;
				     		}
				     	}catch (UnsupportedEncodingException e){
				     		// TODO 自动生成的 catch 块
				     		e.printStackTrace();
				     		 if(Running>20) Running++;
				     	}
		          }
		    	 break;

		     case 13:
		    	 
		    	// byte[] readBuf = (byte[]) msg.obj;
		    	 try {
				//	String s= new String(readBuf, 0, msg.arg1,bm);
					recvMessageserver=(String) msg.obj;
				//	if(recvMessageserver.substring(recvMessageserver.length()-1,recvMessageserver.length()).equals("\n")){
						String bar="";
						String bufString="";
						bar=SYS.BTistrue(recvMessageserver);
						recvMessageClient=recvMessageserver;	
						recvMessageserver="";
						if(bar.length()>0){
							btSent=true;
							player.playScanOK(R.raw.beep);
							if(DH.isFocused()){
								DH.setText(bar);
							    new Handler().postDelayed(new  Runnable(){
									public void run(){	
										barcode.requestFocus();
									}
								},100);
							}
							if(barcode.isFocused()){
							    barcode.setText(bar);
							}
		                   
		                //  barcodeString=bar;
		        			CKString="CK001";	
		                    if(DH.getText().toString().trim().length()==0){
								dhString="DH001";
							}else {
								dhString=DH.getText().toString().trim();
							}
		                  if(isReScan.trim().equals("1")){
		                    SQLiteDatabase db1=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
						      Cursor cursor2=null;
						      cursor2=db1.rawQuery("select * from CK where BAR=? and CK=? and DH=? ", new String[]{barcode.getText().toString().trim(),CKString,dhString});
						      if(cursor2.getCount()!=0){
						    	  dis.setText(getString(R.string.RScan)+"\n");
						    	  player.playScanOK(R.raw.fail);
						    	  cursor2.close();
						    	  db1.close();
						    	  return ;
						      }
		                  }   
		                    dis.setText("");
			   				sView.scrollTo(0,dis.getMeasuredHeight());
		                    dis.setText("");   
		                    bufString="QUERY□"+barcode.getText().toString()+"□"+dhString+"□"+"YD"+"□"+dhString;
		                    if(isconnect && barcode.getText().toString().length()>0){
		                    	isSearchinf=true;
		                   		mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
		                    }else{
		                    	isSearchinf=true;
		       				/*	if(isNUM.trim().equals("1")){
		       						RK(barcode.getText().toString(),dhString,CKString,"1");
		       						KCsl.setText(Double.parseDouble(KCsl.getText().toString())-1+"");
		       						CKys.setText(Double.parseDouble(CKys.getText().toString())+1+"");
		       						MainActivity.sentall++;
		    						
		    						yc.setText(R.string.Transferall);
		    					 	wc.setText(R.string.Transferpovit);
		    					 	yc.append(MainActivity.sentpovit+""); 
		    					 	wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
		       					}else if(isNUM.trim().equals("0")){
		       						NUM.setText("");
		       						NUM.setFocusable(true);
		       	                    NUM.setFocusableInTouchMode(true);
		       	                    NUM.requestFocus();
		       	                    InputMethodManager inputManager =(InputMethodManager)NUM.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		       	                    inputManager.showSoftInput(NUM, 0);
		       					}*/
		                   }
						}else{
							String	btpowerString=SYS.BTisPOWER(recvMessageClient);
							if(btpowerString.length()>0){
								MainActivity.BTPOWER=btpowerString;
								BTpower.setText(R.string.power);
								BTpower.append(MainActivity.BTPOWER+"");
								BTpower.append("%");
							}else{
								player.playScanOK(R.raw.beep);
								dis.setText("");
					   			sView.scrollTo(0,dis.getMeasuredHeight());
					   			SYS.disPlay(sView, dis,getString(R.string.SB)+"\n");
								return ;
							}						
						}
						BTpower.setText(R.string.power);
						BTpower.append(MainActivity.BTPOWER+"");
						BTpower.append("%");
					//}		
				} catch (UnsupportedEncodingException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
		    	 break;
		    	
			   default:
				break;
			}
	 }
};	


public void connect(){
		Text_of_output="";
		Text_of_output=WriteReadSD.read("BTMAC",log.fdir);
		_device =null;
		/*有就直接连接*/
		if(Text_of_output.length()>0){
			_device = MainActivity._bluetooth.getRemoteDevice(Text_of_output.trim());
			RKScan.mChatService.connect(_device);
		}
	return;
}  

public void connectprint(){

	Text_of_output="";
	Text_of_output=WriteReadSD.read("PTMAC",log.fdir);
		_device =null;
		/*有就直接连接*/
		if(Text_of_output.length()>0){
			_device = MainActivity._bluetooth.getRemoteDevice(Text_of_output.trim());
			RKScan.printService.connect(_device);
		}
	return;
} 
public void onStart() {
    super.onStart();
    Log.i("CK", "onStart");
   /* if(!MainActivity._bluetooth.isEnabled()){
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    } else {*/
        if (RKScan.mChatService == null) setupChat();
        else{
        	RKScan.mChatService.setHandler(mHandler);
        }
     /*   if(RKScan.printService==null)  startPrint();
        else{
        	RKScan.printService.setHandler(mHandler);
        }*/
  //  }
}
private void setupChat() {
	
       // 初始化BluetoothChatService进行蓝牙连接
	RKScan.mChatService = new BluetoothChatService(this, mHandler);
	RKScan.mChatService.setHandler(mHandler);
       connect();
      
}
public void onDestroy() {
    super.onDestroy();
    Log.i("CK", "onDestroy");
    // 停止蓝牙
 //   if (mChatService != null) mChatService.stop();
    if(mysocket!=null) mysocket.stop();
  //  if ( printService != null) printService.stop();
}
public synchronized void onResume() {
		super.onResume();
		 Log.i("CK", "onResume");
		if(mysocket!=null){
			if(mysocket.getState()==mySocket.STATE_NONE || mysocket.getState()==mySocket.STATE_LISTEN || mysocket.getState()==mySocket.STATE_FAILE){
				mysocket.connect();
			}
		}
		if (RKScan.mChatService != null){
			if (RKScan.mChatService.getState()==BluetoothChatService.STATE_NONE){
				// 启动蓝牙聊天服务
				RKScan.mChatService.start(); 
			}
		}	
	    mHandler.postDelayed(btRunnable, 5000);	  
		mHandler.postDelayed(socketRunnable,50);
		mHandler.postDelayed(socketConnect,1000*60);
	}
public void onStop() {
    super.onStop();
    Log.i("CK", "onStop");
    mHandler.removeCallbacks(btRunnable);
    mHandler.removeCallbacks(socketConnect); 
 }
public synchronized void onPause() {
    super.onPause();
    Log.e("CK", "- ON PAUSE -");
}
public boolean onKeyDown(int keyCode, KeyEvent event)  {  
	
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
    	mHandler.removeCallbacks(btRunnable); 
    	
    	mHandler.removeCallbacks(socketRunnable);
    	mHandler.removeCallbacks(socketConnect);
    	CKScan.this.finish();
    }else if(keyCode == KeyEvent.KEYCODE_MENU){
       return false;
    }
    return true; 
} 

/*****************************************************************************
 * 
 * 
 *                    本地数据库操作
 * 
 * 
 * ***************************************************************************/
/*入库*/

private  int RK(String bar,String DH,String CK,String rkNum){
	/*当已扫>应扫的时候     报警 不给入库*/
//	CKsj.setText(string[string.length-4]);
	//CKys.setText(Integer.parseInt(CKys.getText().toString())+1);
	if(Integer.parseInt(rkNum)+Integer.parseInt(CKys.getText().toString())>Integer.parseInt(CKsj.getText().toString())){
		player.playScanOK(R.raw.fail);
		
		new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
	     .setPositiveButton(R.string.bBAR,new DialogInterface.OnClickListener() {//添加确定按钮  
	         @Override  
	         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件   
	             return;   
	         }  
	     }).show();	
		return 0;
	}
	/*保存的数据       条码      单号      应扫数量           已扫数量*/
	sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd ");
	date=sDateFormat.format(new   java.util.Date());  
	Cursor cursor=null;
    SQLiteDatabase db=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
    try{
		  cursor=db.query("CK", new String[]{"BAR","NUM","CK","DH"}, "BAR=? and CK=? and DH=?",new String[]{bar.trim(),CK,DH},null, null, null);
		  if(cursor.getCount()!=0){ 
			   double CKnum = 0;
			   while (cursor.moveToNext()){
				   CKnum = Double.parseDouble(cursor.getString(cursor.getColumnIndex("NUM")));
			 }
			 ContentValues values=new ContentValues(); 
			 values.put("YGBH", YGDL.YG);
		
			 values.put("NUM", CKnum+Double.parseDouble(rkNum));
			 values.put("TIME", date);
			 String where = "BAR = ? and CK =? and DH=?" ;
			 String[] whereValue = {bar,CK,DH};
			 db.update("CK", values, where, whereValue);
			 
		}else{	  
			
			  ContentValues va=new ContentValues();
			  va.put("BAR",bar.trim());
			  va.put("NUM",rkNum.trim());
			  va.put("CK",CK);
			  va.put("DH",DH);
			  va.put("SJ",CKsj.getText().toString());
			  va.put("TIME",date);
			  va.put("YGBH", YGDL.YG);
			  db.insert("CK", null, va);  
		}		   
    }catch (Exception e){
			e.printStackTrace();
			cursor.close();
	}finally{
		    if(cursor != null){
		        cursor.close();
		    }
	}
    
    cursor = null;
    cursor=db.query("DHCX",new String[]{"DH","Flag"},"Flag=? and DH=?",new String[]{"CK",DH},null, null, null);
    if(cursor.getCount()==0){
    	
		  ContentValues va=new ContentValues();
		  va.put("Flag","CK");
		  va.put("DH",DH);
		  db.insert("DHCX", null, va);
    }
	cursor.close();
	  String  rkString=""; 
	  rkString ="YD-"+DH+"|"+bar.trim()+","+rkNum.trim()+","+date+","+YGDL.YG; 
	  ContentValues cv= new ContentValues();
	  cv.put("BAR", rkString);
	  cv.put("ISsent","1");  //表示未传
	  db.insert("SENT", null, cv);
	  
	  db.close();
	  return 1;
}
   /*关于仓库和单号的操作*/
	private int initDH(){
	   	SQLiteDatabase db =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
		Cursor cursor=null;
		try{
			cursor=db.query("CK", new String[]{"DH"}, null,null,null, null, null);
			if(cursor.getCount()==0){
				/*dhdata=new  ArrayList<String>();
				dhdata.add("DH001");
				 DH.setText("DH001");*/
				cursor.close();
				return 0;
			}else{
				int i=0;
				boolean eq=false;
				dhdata=new  ArrayList<String>(); 
				while (cursor.moveToNext())
				{
					eq=false;
					for(i=0;i<dhdata.size();i++){
						if(dhdata.get(i).equals(cursor.getString(cursor.getColumnIndex("DH")))){
							eq=true;
							break;
						}	
					}
					if(!eq){
					  dhdata.add(cursor.getString(cursor.getColumnIndex("DH")));
					}  
				}
				String name=dhdata.get(dhdata.size()-1);
				 DH.setText(name);
			//	DH.setSelection(dhdata.size());
				cursor.close();
				return 1;
			} 
		}catch(Exception e){
			cursor.close();
		}finally{
			cursor.close();
			
		}
		return 0;
	}
	/*将所有的登录名都放到ListView*/
	private void initDHPopup() {  
		
		    if(initDH()==1){
			mAdapter = new ArrayAdapter<String>(this,  
			android.R.layout.simple_dropdown_item_1line,dhdata);  
			mListView =new ListView(mContext); 
			mListView.setAdapter(mAdapter);  
			
			int height = ViewGroup.LayoutParams.WRAP_CONTENT;  
			int width =  DH.getWidth();  
			//System.out.println(width);  
			popView = new PopupWindow(mListView, width, height, true);  
			popView.setOutsideTouchable(true);  
			popView.setBackgroundDrawable(getResources().getDrawable(R.color.white));
			popView.setOnDismissListener(oClickListener); 
			mListView.setOnItemClickListener(onItem); 
		   }
		}  
	private OnDismissListener oClickListener=new OnDismissListener() {
		@Override
		public void onDismiss() {
			// TODO 自动生成的方法存根
			mShowing = false;
		}
	};
	
	private OnItemClickListener onItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			// Log.e(TAG, "- 啊啊啊啊啊啊啊 -");
			DH.setText(dhdata.get(arg2));  
			 popView.dismiss(); 
		}
	};
/*这里是点击那个向下*/	
   private OnClickListener dhin =new OnClickListener() {
	
	@Override
	public void onClick(View v){
		initDHPopup();  
		//	}  
			if (popView!= null){  
			if (!mShowing) {  
				popView.showAsDropDown(DH,0,-5);  
			mShowing = true;  
			}else{  
				popView.dismiss();  
			}  
		 }  
	}
};	
   



public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
	return true;
}
public boolean onOptionsItemSelected(MenuItem item) {  
    switch (item.getItemId()) {  
    case R.id.DHLL:  
    	Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("dh","CK");
		resultIntent.putExtras(bundle);
		resultIntent.setClass(CKScan.this,SearchDH.class);
		startActivity(resultIntent); 
        return true;  
     
    }  
    return false;  
}
}
