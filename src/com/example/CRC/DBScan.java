package com.example.CRC;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.mining.app.socket.mySocket;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class DBScan extends Activity {
	private TextView dis;       //接收数据显示句柄
    private ScrollView sView;
    private  EditText barcode;   //条码
    private  EditText KCsl;      //库存
    private Button sendButton;   //确定按钮
   	private TextView btzt;  //蓝牙状态
   	private TextView wc;   //未传数量
    private TextView yc;   //已传数量
    private TextView BTpower; //蓝牙电量
    private Button  ConnectButton;
   	private EditText  NUM;
   	private Button  sjsm;
    private  EditText DCCK;      //库存
    private  EditText DRCK;  
    private  EditText DH;        //单号
    private  EditText CKys;      //以扫
    private String  Text_of_output="";
   	private String recvMessageserver ="";
   	private String recvMessageClient = "";
   	private Context mContext=null;
    private String CKString="";
	private String dhString="";
	private String YSSL=""; 
	private String fwq="";
	private String SentData="";
	private String BH="";
	private String barcodeString="";
	private String YCK="";
	private String MDCK="";
	private String androidID="";
	float  RKnum=0; 
	long start=0;
	long end=0;
	 private static final String bm = "GBK";
	// private BluetoothChatService mChatService = null;
	 private mySocket mysocket=null;
	 Vibrator vibrator=null;
	 
	private final static int SCANNIN_GREQUEST_CODE = 1;
	public static final  int REQUEST_CONNECT_DEVICE = 2;    //宏定义查询设备句柄
	private static final int REQUEST_ENABLE_BT = 3;
	private  BluetoothDevice _device = null;     //蓝牙设备
	//boolean Isbt=false;
	boolean isconnect=false;
	boolean btSent=false;   //是蓝牙传输过来的，或者是手机扫描的
	boolean isSearchinf=false;
	boolean isSearchOK=false;
	private int Running=21;
	private boolean isSent=false;
	
	private String isReScan="";    //是否允许重复扫描；
	private String isNUM="";      //是否需要数量
	private String isDH="";
	private String iPString="";
	private String portString="0";
	private String rkNum="";
	private SimpleDateFormat   sDateFormat;   //时间
	private String   date="" ;
	private ImageButton CK1imag;
	private ImageButton CK2imag;
	private ImageButton DHimag;
	private PopupWindow popView;
	private ArrayAdapter<String> mAdapter;  
	private ListView mListView; 
	private ArrayList<String> dhdata=null;
	private	ArrayList<String> ck1data=null;
	
	private boolean mShowing;
    protected void   onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.dbsm);
    	Running=21;
		mContext=this;
		 TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	     androidID=telephonyManager.getDeviceId();
	//	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//解决了软件软键盘的问题
	//	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	btzt=(TextView) findViewById(R.id.dbbt);
    	ConnectButton=(Button)findViewById(R.id.dblj);
		
		BTpower=(TextView)findViewById(R.id.dbdl);
		BTpower.setText(R.string.power);
		dis=(TextView)findViewById(R.id.dbtextview);   
		sView=(ScrollView)findViewById(R.id.dbQSview);
		barcode=(EditText)findViewById(R.id.dbqs_sendmessage);
		NUM=(EditText)findViewById(R.id.dbqsr);
		DH=(EditText)findViewById(R.id.dbdh);
		yc=(TextView)findViewById(R.id.dbyc);
		wc=(TextView)findViewById(R.id.dbwc);
		DCCK=(EditText)findViewById(R.id.dcck);
		DRCK=(EditText)findViewById(R.id.drck);
		sendButton=(Button)findViewById(R.id.dbqs_send);
		sendButton.setOnClickListener(sent);
		Button search=(Button)findViewById(R.id.dbear);
		search.setOnClickListener(searchon);
		sjsm=(Button)findViewById(R.id.dbsjsm);
		sjsm.setOnClickListener(scanClickListener);
		btzt.setOnClickListener(Btset);
		CKys=(EditText)findViewById(R.id.dbyy);
		KCsl=(EditText)findViewById(R.id.dbKC);
		barcode.setText("");
	  	barcode.requestFocus();	
	  	
		DHimag=(ImageButton) findViewById(R.id.dbImageButton01);
		CK1imag=(ImageButton) findViewById(R.id.dbImageButton02);
		CK2imag=(ImageButton) findViewById(R.id.dbImageButton03);
		
		isReScan=WriteReadSD.read("isReScan",log.fdir);
		isNUM=WriteReadSD.read("isNUM",log.fdir);
		if(isNUM.trim().equals("1")){
		   		NUM.setText("1");
		  		//NUM.setEnabled(false);
		}
		NUM.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		isDH=WriteReadSD.read("isDH",log.fdir);
		if(isDH.trim().equals("1")||isDH.trim().equals("2")){
	   		DH.setEnabled(false);
	   		if(isDH.trim().equals("2")){
	   			Text_of_output="";
	   			Text_of_output=WriteReadSD.read( "YKDH",log.fdir);
	   			System.out.println("单号是:"+Text_of_output.trim());
	   			if(Text_of_output.trim().length()==0){
	   				sDateFormat   =   new   SimpleDateFormat("yyyyMMddHHMMSS");
		    	    date=sDateFormat.format(new java.util.Date());
	   			    String sql="YK"+date;
	   			    DH.setText(sql);
	   			    WriteReadSD.writex(sql, "YKDH",log.fdir);   
	   			}else{
	   				DH.setText(Text_of_output.trim());
	   			}
	   			Button dhButton=(Button) findViewById(R.id.Button03);
	   			dhButton.setOnClickListener(dhClickListener);
	   		}else{
	   			DH.setText("DH001");
	   		}
	   	}
		if(isDH.trim().equals("0")||isDH.trim().equals("2")){
	   		if(isDH.trim().equals("0")){
	   			initDH();
	   		}
	   	//	DHimag=(ImageButton) findViewById(R.id.pdImageButton01);
	   		DHimag.setOnClickListener(dhin);
	   	}
		/*else{
	   		DHimag.setOnClickListener(dhin);
	   	}*/
		 initCK();
   		 CK1imag.setOnClickListener(ckin); 
   		CK2imag.setOnClickListener(drckin);
   		
   		yc.setText(R.string.Transferall);
		wc.setText(R.string.Transferpovit);
		yc.append(MainActivity.sentpovit+""); 
		wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
		
		String[] str =null;
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
	 	if(!RKScan.Isbt){
	    	btzt.setText(R.string.title_not_connected);
	    }else{
	    	 btzt.setText(R.string.title_connected_to);
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
    }
    /*单号切换*/
private OnClickListener dhClickListener =new OnClickListener() {
    	
    	@Override
    	public void onClick(View v) {
    		// TODO 自动生成的方法存根
    		 sDateFormat   =   new   SimpleDateFormat("yyyyMMddHHMMSS");
     	    date   =   sDateFormat.format(new   java.util.Date());
     	   String sql="YK"+date;
    	   DH.setText(sql);
    	   WriteReadSD.writex(sql, "YKDH",log.fdir);    
    	}
    };     
/*蓝牙连接*/
private OnClickListener Btset =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			RKScan.Isbt=false;
			mHandler.removeCallbacks(btRunnable); 
			Intent serverIntent = new Intent(DBScan.this, DeviceListActivity.class); //跳转程序设置
			startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);  //设置返回宏定义*/
		}
	};
	/*服务器连接*/	
private OnClickListener socketconnect=new OnClickListener() {
		
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
	private OnClickListener scanClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			Intent intent1 = new Intent();
			intent1.setClass(DBScan.this, MipcaActivityCapture.class);
			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent1, SCANNIN_GREQUEST_CODE);
		}
	};
	/*搜索资料*/
	private OnClickListener searchon =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			btSent=false;
			if(DCCK.getText().toString().trim().length()==0){
				YCK="CK001";
			}else{
					YCK=DCCK.getText().toString().trim();
			}
	        if(DH.getText().toString().trim().length()==0){
				dhString="DH001";
			}else {
				dhString=DH.getText().toString().trim();
			}
	        if(DRCK.getText().toString().trim().length()==0){
				MDCK="CK001";
			}else{
					MDCK=DRCK.getText().toString().trim();
			}
	        CKys.setText("");
	        KCsl.setText("");
	        if(isReScan.trim().equals("1")){
	          	// LoginOpenHelper helper=new LoginOpenHelper(mContext,log.DB_NAME, null, 1);
				      SQLiteDatabase db1=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
				      Cursor cursor2=null;
				      try{
				    	  cursor2=db1.query("RK", new String[]{"BAR","NUM","CK","DH"}, 
								    "BAR=? and CK=? and DH=?",new String[]{barcode.getText().toString().trim(),CKString,dhString},null, null, null);
				    	   if(cursor2.getCount()!=0){
				    		//   vibrator.vibrate(800);
						       dis.setText(getString(R.string.RScan)+"\n");
				    		   return ;
				    	   }
				      }catch(Exception e){
						  e.printStackTrace();
						  cursor2.close();
						}finally{
						    if(cursor2 != null){
						        cursor2.close();
						    }
					   }
	          } 
	        String  bufString="QUERY□"+barcode.getText().toString()+"□"+YCK+"□"+"YK"+"□"+MDCK+"□"+dhString;
	       // String bufString="QUERY□"+barcode.getText().toString()+"□"+CKString+"□"+"RK"+"□"+dhString;
	        barcodeString=barcode.getText().toString().trim();
	          /*发送查询指令*/
	      	  if(isconnect){
	      		  isSearchinf=true;
	      		mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
	      	  }else{
	      		 isSearchinf=false;
	      		 seaCK(barcode.getText().toString(),YCK);
				 SearchINF(barcode.getText().toString());
				/*查已扫数量*/ 
				 Searchys(barcode.getText().toString(),dhString,YCK,MDCK);
	      	  }
		}
	};
	
	/*数据发送*/
	private  OnClickListener sent=new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			
				YCK=DCCK.getText().toString().trim();
			    MDCK=DRCK.getText().toString().trim();
			if(DH.getText().toString().trim().length()>0){
				dhString=DH.getText().toString().trim();
			}else{
				dhString="DH001";
			}
			if(barcode.getText().toString().length()==0){
				new AlertDialog.Builder(DBScan.this).setTitle(R.string.SYS)//设置对话框标题  
			     .setPositiveButton(R.string.nosent,new DialogInterface.OnClickListener() {//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
			             return ;   
			         }  
			     }).show();//在按键响应事件中显示此对话框  
				return ;
			}
			if(YCK.length()==0||MDCK.length()==0){
				new AlertDialog.Builder(DBScan.this).setTitle(R.string.SYS)//设置对话框标题  
			     .setPositiveButton(R.string.CKBNSK,new DialogInterface.OnClickListener() {//添加确定按钮  
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
				new AlertDialog.Builder(DBScan.this).setTitle(R.string.SYS)//设置对话框标题  
			     .setPositiveButton(R.string.BXSZ,new DialogInterface.OnClickListener() {//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
			             return ;   
			         }  
			     }).show();//在按键响应事件中显示此对话框  
				return ;
			}
			new AlertDialog.Builder(DBScan.this).setTitle(R.string.SYS)//设置对话框标题  
			  
		     .setMessage(R.string.GBKC)//设置显示的内容  
		  
		     .setPositiveButton(R.string.S,new DialogInterface.OnClickListener() {//添加确定按钮  
		    	
		         @Override  
		     	
		         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
		             // TODO Auto-generated method stub  
		        	 RK(barcode.getText().toString(),dhString,YCK,MDCK,rkNum);
		 			KCsl.setText(Double.parseDouble(KCsl.getText().toString())-Double.parseDouble(rkNum)+"");
		 			CKys.setText(Double.parseDouble(CKys.getText().toString())+Double.parseDouble(rkNum)+"");
		 			MainActivity.sentall++;
		 			yc.setText(R.string.Transferall);
		 		 	wc.setText(R.string.Transferpovit);
		 		 	yc.append(MainActivity.sentpovit+""); 
		 		 	wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
		 		 	barcode.setText("");
		 		  	barcode.requestFocus();
		 		  	KCsl.setText("0");
		 			CKys.setText("0");
		 	  		NUM.setText("1");
		         }  
		  
		     }).setNegativeButton(R.string.N,new DialogInterface.OnClickListener() {//添加返回按钮  

		         @Override  
		         public void onClick(DialogInterface dialog, int which) {//响应事件  
		        	 return ;
		         }  
		  
		     }).show();//在
        
		    
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
				YCK=DCCK.getText().toString().trim();
				MDCK=DRCK.getText().toString().trim();	
					if(DH.getText().toString().trim().length()>0){
						dhString=DH.getText().toString().trim();
					}else{
						dhString="DH001";
					}
					if(YCK.length()==0||MDCK.length()==0){
	                	new AlertDialog.Builder(DBScan.this).setTitle(R.string.SYS).setPositiveButton(R.string.CKBNSK,new DialogInterface.OnClickListener() {//添加确定按钮  
		       		         @Override  
		       		         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
		       		             return;   
		       		         }  
	       		        }).show();//在按键响应事件中显示此对话框  
	       			   return ;
	                }    
					 if(isReScan.trim().equals("1")){
          			   SQLiteDatabase db1=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
          				Cursor cursor2=null;
          				  try{
          				    	cursor2=db1.query("DB", new String[]{"BAR","NUM","原仓库","DH","目的仓库"}, 
          								"BAR=? and 原仓库=? and 目的仓库 =? and DH=?",new String[]{barcode.getText().toString().trim(),YCK,MDCK,dhString},null, null, null);
          				    	if(cursor2.getCount()!=0){
          				    		   vibrator.vibrate(800);
          						       dis.setText(getString(R.string.RScan)+"\n");
          				    		   return ;
          				    	}
          				 }catch(Exception e){
          						  e.printStackTrace();
          						  cursor2.close();
          						}finally{
          						    if(cursor2 != null){
          						        cursor2.close();
          						    }
          					}
                         }   
		                    dis.setText("");
		   				 sView.scrollTo(0,dis.getMeasuredHeight());
		   				 CKys.setText("");
		                 KCsl.setText("");
		               bufString="QUERY□"+barcode.getText().toString()+"□"+YCK+"□"+"YK"+"□"+MDCK+"□"+dhString;
                         /*发送查询指令*/
		               	  if(isconnect){
		               		  isSearchinf=true;
		               		mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
		               		System.out.println( bufString);
		               	  }else{
		               		seaCK(barcode.getText().toString(),YCK);
		               		SearchINF(barcode.getText().toString());
		               		Searchys(barcode.getText().toString(),dhString,YCK,MDCK);
	        				if(isNUM.trim().equals("1")){
	        					RK(barcode.getText().toString(),dhString,YCK,MDCK,"1");
	        					KCsl.setText(Double.parseDouble(KCsl.getText().toString())-1+"");
	    						CKys.setText(Double.parseDouble(CKys.getText().toString())+1+"");
	    						MainActivity.sentall++;
	    						
	    						yc.setText(R.string.Transferall);
	    					 	wc.setText(R.string.Transferpovit);
	    					 	yc.append(MainActivity.sentpovit+""); 
	    					 	wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
	    					}else{
	    						NUM.setText("");
	    						NUM.setFocusable(true);
	    	                    NUM.setFocusableInTouchMode(true);
	    	                    NUM.requestFocus();
	    	                    InputMethodManager inputManager =
	    	                                (InputMethodManager)NUM.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
						// 试图连接到装置
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
	   }
	}
	
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

	public void onStart() {
	    super.onStart();
	    if(!MainActivity._bluetooth.isEnabled()){
	        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	    } else {
	        if (RKScan.mChatService == null) setupChat();
	        else{
	        	RKScan.mChatService.setHandler(mHandler);
	        }
	      
	    }
	}
	private void setupChat() {
		
	       // 初始化BluetoothChatService进行蓝牙连接
		RKScan.mChatService = new BluetoothChatService(this, mHandler);
		RKScan.mChatService.setHandler(mHandler);
	       connect();
	       new StringBuffer("");
	}
	
	public void onDestroy() {
	    super.onDestroy();
	    Log.i("socket", "onDestroy");
	    // 停止蓝牙
	   // if (mChatService != null) mChatService.stop();
	    if(mysocket!=null) mysocket.stop();
	    
	}
	public synchronized void onResume() {
			super.onResume();
			 Log.i("socket", "onResume");
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
			mHandler.postDelayed(socketRunnable,100);
			mHandler.postDelayed(socketConnect,1000*60);
		}
	public void onStop() {
	    super.onStop();
	    Log.i("socket", "onStop");
	    mHandler.removeCallbacks(btRunnable);
	    mHandler.removeCallbacks(socketConnect); 
	 }
	public synchronized void onPause() {
	    super.onPause();
	    Log.e("socket", "- ON PAUSE -");
	}
	public boolean onKeyDown(int keyCode, KeyEvent event)  {  
		
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
	    	mHandler.removeCallbacks(btRunnable); 
	    	mHandler.removeCallbacks(socketRunnable);
	    	mHandler.removeCallbacks(socketConnect);
	    	DBScan.this.finish();
	    }else if(keyCode == KeyEvent.KEYCODE_MENU){
	       return false;
	    }
	    return true; 
	} 

	/*定时检测蓝牙*/	
	private Runnable btRunnable=new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			 synchronized (this){
				 if(!RKScan.Isbt){
				     // btzt.setText("扫描枪未开启");
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
			// synchronized (this){
			
			if(isconnect && MainActivity.sentall>MainActivity.sentpovit){
				Message msg1 = new Message();
				msg1.what =38;
				mHandler.sendMessage(msg1);
			}
			mHandler.postDelayed(this, 100); 
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
	Handler mHandler = new Handler(){
		 public void handleMessage(Message msg){
			  super.handleMessage(msg);
			  switch(msg.what){
			  case 38:
			    	 Log.i("进来",Running+"");
			    	 SentData="";
			    	 BH="";
		             SQLiteDatabase db=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
					 Cursor cursor = null;
				      try{
							cursor =  db.query("SENT", new String[]{"BAR","ISsent","BH"}, "ISsent= ?", new String[]{"1"}, null, null, null);
						    if(cursor != null){
						    	   if( cursor.getCount()!=0 && cursor.moveToFirst()){
						    		   SentData=cursor.getString(cursor.getColumnIndex("BAR"));
						    		   BH=cursor.getString(cursor.getColumnIndex("BH"));
									  if(MainActivity.sentall>MainActivity.sentpovit &&isconnect&& !isSent){
											  Log.i("发送的数据:", SentData+":"+BH);
											  mysocket.writeData(SYS.SenData(SentData,fwq,androidID,Running));
											  isSent=true;
											  isSearchinf=false; 
									  }else{
										  return ;
									  }	 
								 } 
						    }else{
						    	 Log.i("没有啊","*****");
						    }
						}catch(Exception e){
							 Log.i("也没有啊","*****");
							// return ;
						}finally{
						    if(cursor != null){
						        cursor.close();
						    }
						}
			    	  break;
			  case 17: //获取接收到的数据
			    	 String name = msg.getData().getString("REC");
			    	 String num="";
			    	 int len=0;
			    	 String sqlString=new String().format("talk%s%s%s%s,","□",androidID,"□",barcodeString);
			    	 String [] string=null;
			    	 String [] buf=null;
			    	 System.out.println(name);
			          if(isSearchinf){
			        	  isSearchinf=false;
			        	  if(SYS.indexofString(name,sqlString)){
			        		  buf=SYS.splitString(name.trim(),"■");
			        		  if(!SYS.indexofString(buf[0].trim(),sqlString))
			        			  return ;
			        		  try {
									len=SYS.crc_r(buf[0].getBytes(bm),buf[0].getBytes(bm).length);
									len=len % 99;
									if(len==Integer.parseInt(buf[1].trim())){
										string=SYS.splitString(buf[0],",");
										
										if(string[1].equals("无--")){
										   KCsl.setText(string[3].trim());
										  CKys.setText(string[5].trim());
										   SearchINF(barcode.getText().toString()); 
										}else{
											dis.setText("");
											for(int i=1;i<string.length-5;i+=2){
												SYS.disPlay(sView, dis,string[i]+":"+string[i+1]+"\n");
											}
										   KCsl.setText(string[string.length-4]);
										   CKys.setText(string[string.length-2]);
										}
									   isSearchOK=true;
									}else{
										 /*查找失败*/
										dis.setText("");
										dis.setText("查询失败1\n");
										isSearchOK=false;
									}
								} catch (UnsupportedEncodingException e) {
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
			        		    seaCK(barcodeString,YCK);
		        				SearchINF(barcodeString);
		        				/*查已扫数量*/ 
		        				Searchys(barcodeString,dhString,YCK,MDCK); 
			        	  }
			        	  /**/
			        	  if(isNUM.trim().equals("1") && btSent){
	      					RK(barcode.getText().toString(),dhString,YCK,MDCK,"1");
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
			     
			  case 1:            //这个是蓝牙状态的,用蓝牙名称来区分是蓝牙枪还是打印机
				switch (msg.arg1) {
					case BluetoothChatService.STATE_CONNECTED:
						 btzt.setText(R.string.title_connected_to);
						 RKScan.Isbt=true;
						 break;
					case BluetoothChatService.STATE_CONNECTING:
						 btzt.setText(R.string.title_connecting);
						 break;
					case BluetoothChatService.STATE_LISTEN:
						RKScan.Isbt=false;
						  btzt.setText(R.string.title_not_connected);
						 break;
					case BluetoothChatService.STATE_NONE:
					      btzt.setText(R.string.title_not_connected);
					      RKScan.Isbt=false;
						  break;
					}
					break;	
			     case 4:
				        btzt.setText(R.string.title_connected_to);
				        RKScan.Isbt=true;
						break;
			     case 16:
			    	 switch (msg.arg1) {
					case 0:
						ConnectButton.setText(R.string.Noconnected);
						//vibrator.vibrate(800);
						dis.setText("");
						SYS.disPlay(sView, dis, getString(R.string.dk)+"\n");
						isconnect=false; //网络连接失败
						break;
					case 1:
						dis.setText("");
						ConnectButton.setText(R.string.Noconnected);
					//	vibrator.vibrate(800);
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
						if(isSearchinf){
					          isSearchinf=false;
				        	  seaCK(barcodeString,YCK);
			        		  SearchINF(barcodeString);
			        		  /*查已扫数量*/ 
			        		  Searchys(barcodeString,dhString,YCK,MDCK); 
				        	  /**/
				        	  if(isNUM.trim().equals("1") && btSent){
		    					RK(barcode.getText().toString(),dhString,YCK,MDCK,"1");
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
							}
						}
						if(isSent){
							 isSent=false;
							 if(Running>20) Running++;
						}else{
							return;
						}
						break;
			    	 }	
					break;
			     case 13:
				byte[] readBuf = (byte[]) msg.obj;
				    	 try {
							String s= new String(readBuf, 0, msg.arg1,bm);
							recvMessageserver+=s ;
							if(recvMessageserver.substring(recvMessageserver.length()-1,recvMessageserver.length()).equals("\n")){
								System.out.println(recvMessageserver);
								String bar="";
								String bufString="";
								bar=SYS.BTistrue(recvMessageserver);
								recvMessageClient=recvMessageserver;
								recvMessageserver="";
								if(bar.length()>0){							
				                    barcode.setText(bar);
				                    MainActivity.playScanOK(mContext);
				                    barcodeString=bar;
				                    btSent=true;
				                    YCK=DCCK.getText().toString().trim();
				                    MDCK=DRCK.getText().toString().trim();
				                    if(DH.getText().toString().trim().length()==0){
				                    	dhString="DH001"; 
				                    }else{
				                    	dhString=DH.getText().toString().trim();
				                    }
				                if(YCK.length()==0||MDCK.length()==0){
				                	new AlertDialog.Builder(DBScan.this).setTitle(R.string.SYS).setPositiveButton(R.string.CKBNSK,new DialogInterface.OnClickListener() {//添加确定按钮  
					       		         @Override  
					       		         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
					       		             return;   
					       		         }  
				       		        }).show();//在按键响应事件中显示此对话框  
				       			   return ;
				                }    
				                 /*这里要发出声音*/
		                         MainActivity.playScanOK();
		                        /*是否允许重复扫描*/
		                         if(isReScan.trim().equals("1")){
		            			   SQLiteDatabase db1=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
		            				Cursor cursor2=null;
		            				  try{
		            				    	cursor2=db1.query("DB", new String[]{"BAR","NUM","原仓库","DH","目的仓库"}, 
		            								"BAR=? and 原仓库=? and 目的仓库 =? and DH=?",new String[]{barcode.getText().toString().trim(),YCK,MDCK,dhString},null, null, null);
		            				    	if(cursor2.getCount()!=0){
		            				    		   vibrator.vibrate(800);
		            						       dis.setText(getString(R.string.RScan)+"\n");
		            				    		   return ;
		            				    	}
		            				 }catch(Exception e){
		            						  e.printStackTrace();
		            						  cursor2.close();
		            						}finally{
		            						    if(cursor2 != null){
		            						        cursor2.close();
		            						    }
		            					}
		                           }   
				                    dis.setText("");
				   				 sView.scrollTo(0,dis.getMeasuredHeight());
				   				 CKys.setText("");
				                 KCsl.setText("");
				               bufString="QUERY□"+barcode.getText().toString()+"□"+YCK+"□"+"YK"+"□"+MDCK+"□"+dhString;
		                           /*发送查询指令*/
				               	  if(isconnect){
				               		  isSearchinf=true;
				               		mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
				               		System.out.println( bufString);
				               	  }else{
				               		seaCK(barcode.getText().toString(),YCK);
				               		SearchINF(barcode.getText().toString());
				               		Searchys(barcode.getText().toString(),dhString,YCK,MDCK);
			        				if(isNUM.trim().equals("1")){
			        					RK(barcode.getText().toString(),dhString,YCK,MDCK,"1");
			        					KCsl.setText(Double.parseDouble(KCsl.getText().toString())-1+"");
			    						CKys.setText(Double.parseDouble(CKys.getText().toString())+1+"");
			    						MainActivity.sentall++;
			    						yc.setText(R.string.Transferall);
			    					 	wc.setText(R.string.Transferpovit);
			    					 	yc.append(MainActivity.sentpovit+""); 
			    					 	wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
			    					}else{
			    						NUM.setText("");
			    						NUM.setFocusable(true);
			    	                    NUM.setFocusableInTouchMode(true);
			    	                    NUM.requestFocus();
			    	                    InputMethodManager inputManager =(InputMethodManager)NUM.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			    	                    inputManager.showSoftInput(NUM, 0);
			    					}
				               	  }
								}else{
									String	btpowerString=SYS.BTisPOWER(recvMessageClient);
									if(btpowerString.length()>0){
										MainActivity.BTPOWER=btpowerString;
										BTpower.setText(R.string.power);
										BTpower.append(MainActivity.BTPOWER+"");
										BTpower.append("%");
									}else{
										dis.setText("");
							   			sView.scrollTo(0,dis.getMeasuredHeight());
										SYS.disPlay(sView, dis,getString(R.string.SB)+"\n");
										return;
									}						
								}
								BTpower.setText(R.string.power);
								BTpower.append(MainActivity.BTPOWER+"");
								BTpower.append("%");
							}		
						} catch (UnsupportedEncodingException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
				    	 break;	
			  }

		 }

	};
	/*****************************************************************************
	 * 
	 * 
	 *                    本地数据库操作
	 * 
	 * 
	 * ***************************************************************************/
	/*入库*/
	private void RK(String bar,String DH,String CK1,String CK2,String rkNum){
		
		  sDateFormat =new SimpleDateFormat("yyyy-MM-dd ");
		  date=sDateFormat.format(new   java.util.Date());  
	      SQLiteDatabase db=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();  	   
		  SQLiteDatabase dbw =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
		  ContentValues va=new ContentValues();
		  va.put("BAR",bar.trim());
		  va.put("NUM",rkNum.trim());
		  va.put("原仓库",CK1.trim());
		  va.put("目的仓库",CK2.trim());
		  va.put("DH",DH);
		  va.put("TIME",date);
		  va.put("YGBH", YGDL.YG);
		  dbw.insert("DB", null, va);
	    Cursor  cu = null;
	 
	    cu=db.query("DHCX", new String[]{"DH","Flag"},"Flag=? and DH=?",new String[]{"DB",DH.trim()},null, null, null);
	    if(cu.getCount()==0){
	    	 SQLiteDatabase dbwx =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
			 va=new ContentValues();
			 va.put("Flag","DB");
			 va.put("DH",DH.trim());
			 dbwx.insert("DHCX", null, va);
	    }
	   
	    cu.close();
	    cu=null;
	    String sql="select* from KC where BAR=? AND CK=?";
	    cu=db.rawQuery(sql, new String[]{bar.trim(),CK1.trim()});
	    System.out.println(cu.getCount());
		if(cu.getCount()==0){
			  sql="insert into KC (CK,KCNUM,RKNUM,CKNUM,BAR) values (?,?,?,?,?)";
			  dbw.execSQL(sql, new String[]{CK1.trim(),rkNum.trim(),"0","0",bar.trim()}) ;
		}else{
			 cu.moveToNext();
		     sql="update KC set KCNUM=? where CK =? and BAR =?";
		     double d=Double.parseDouble(rkNum)+Double.parseDouble(cu.getString(cu.getColumnIndex("KCNUM")));
		     dbw.execSQL(sql, new String[]{d+"",CK1.trim(),bar.trim()});
		}
		cu.close();
	    cu=null;
	     sql="select* from KC where BAR=? AND CK=?";
	    cu=db.rawQuery(sql, new String[]{bar.trim(),CK2.trim()});
		if(cu.getCount()==0){
			  sql="insert into KC(CK,KCNUM,RKNUM,CKNUM,BAR) values (?,?,?,?,?)";
			  dbw.execSQL(sql, new String[]{CK2.trim(),rkNum.trim(),"0","0",bar.trim()}) ;
		}else{
			 cu.moveToNext();
			 double d=Double.parseDouble(rkNum)+Double.parseDouble(cu.getString(cu.getColumnIndex("KCNUM")));
		     sql="update KC set KCNUM=? where CK =? and BAR =?";
		     dbw.execSQL(sql, new String[]{d+"",CK2.trim(),bar.trim()}) ;
		}  
		String rkString ="YK-"+DH+"-"+CK1+"-"+CK2+"|"+bar.trim()+","+rkNum.trim()+","+date+","+YGDL.YG;
		    SQLiteDatabase d = LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
			ContentValues cv= new ContentValues();
			cv.put("BAR", rkString);
			cv.put("ISsent","1");  //表示未传
			d.insert("SENT", null, cv);
			/*barcode.setText("");
			barcode.requestFocus();*/
	}
	private void seaCK(String barcodeString,String ck){
		SQLiteDatabase db = LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
		Cursor cursor=null;
		 cursor = db.query("KC", new String[] { "BAR","CK", "KCNUM" }, "BAR=? and CK=?",new String[] { barcodeString.trim(),
						ck.trim() }, null,null, null);
		try{
			  if (cursor.getCount() != 0)
				{
					while (cursor.moveToNext())
					{
						KCsl.setText(cursor.getString(cursor.getColumnIndex("KCNUM")));
				   }
				}else{
					KCsl.setText("0");
				}
		}catch(Exception e){
			  cursor.close();
			}finally{
			    if(cursor != null){
			        cursor.close();
			    }
			}
	}
	/*搜索本地资料并显示出来*/
	private  void  SearchINF(String barcodes){
		SQLiteDatabase db = LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
		Cursor cursor=null;
		try{
		 cursor = db.query("JBZL", new String[] { "INF1",
				"INF2", "INF3", "INF4", "INF5", "INF6", "INF7",
				"INF8", "INF9", "INF10", "INF11" }, "INF1=?",
				new String[] { barcodes.trim() },
				null, null, null);
		String[] arr = new String[10];
		if (cursor.getCount() != 0)
		{
			cursor.moveToNext();
			arr[0] = cursor.getString(cursor.getColumnIndex("INF2")).toString();
			arr[1] = cursor.getString(cursor.getColumnIndex("INF3")).toString();
			arr[2] = cursor.getString(cursor.getColumnIndex("INF4")).toString();
			arr[3] = cursor.getString(cursor.getColumnIndex("INF5")).toString();
			arr[4] = cursor.getString(cursor.getColumnIndex("INF6")).toString();
			arr[5] = cursor.getString(cursor.getColumnIndex("INF7")).toString();
			arr[6] = cursor.getString(cursor.getColumnIndex("INF8")).toString();
			arr[7] = cursor.getString(cursor.getColumnIndex("INF9")).toString();
			arr[8] = cursor.getString(cursor.getColumnIndex("INF10")).toString();
			arr[9] = cursor.getString(cursor.getColumnIndex("INF11")).toString();
			dis.setText("");
			sView.scrollTo(0,dis.getMeasuredHeight());
			for(int i = 0; i < arr.length; i++){
	            SYS.disPlay(sView, dis, arr[i]+"\n");
			}
		} else{
				vibrator.vibrate(500);
				dis.setText("");
				sView.scrollTo(0,dis.getMeasuredHeight());
	            SYS.disPlay(sView, dis,getString(R.string.NOinft)+"\n");
		}
	  }catch(Exception e){
			}finally{
			    if(cursor != null){
			        cursor.close();
			    }
			}
	   } 
	   private void Searchys(String bar,String DH,String CK1,String CK2)
	   {
		   Cursor cursor=null;
		 //  LoginOpenHelper helper=new LoginOpenHelper(mContext, "AndroidPosJxc.db", null, 1);
		    SQLiteDatabase db=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
		    try{
				     cursor=db.query("DB", new String[]{"BAR","NUM","原仓库","目的仓库","DH"}, 
							    "BAR=? and DH=?",new String[]{bar,DH},null, null, null);
						  if(cursor.getCount()!=0){
							  double CKnum = 0;
								while (cursor.moveToNext())
								{
									CKnum += Double.parseDouble(cursor.getString(cursor.getColumnIndex("NUM")));
								}
								YSSL=CKnum+"";	 
						  }else{
							  YSSL="0";
				}
						  CKys.setText(YSSL); 
		    }catch (Exception e){
					e.printStackTrace();
					cursor.close();
					// TODO: handle exception
				}finally{
				    if(cursor != null){
				        cursor.close();
				    }
				}

	   }  
	   
	   /*关于仓库和单号的操作*/
		private int initDH(){
		   	SQLiteDatabase db =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
			Cursor cursor=null;
			String sql="SELECT * FROM DHCX where Flag=?";
			cursor=db.rawQuery(sql, new String[]{"DB"});
			dhdata=new  ArrayList<String>(); 
			if(cursor.getCount()==0){	
				dhdata.add("DH001");
				DH.setText("DH001");
				cursor.close();
				db.close();
				return 0;
			}else{
				 while(cursor.moveToNext()){
					 dhdata.add(cursor.getString(cursor.getColumnIndex("DH")));
				 }
				 String name=dhdata.get(dhdata.size()-1);
				 DH.setText(name);
				DH.setSelection(dhdata.size());
				cursor.close();	
				return 1;
			}
		}
		/*将所有的登录名都放到ListView*/
		private void initDHPopup() {  
			    initDH();
				mAdapter = new ArrayAdapter<String>(this,  
				android.R.layout.simple_dropdown_item_1line,dhdata);  
				mListView =new ListView(mContext); 
				mListView.setAdapter(mAdapter);  
				
				int height = ViewGroup.LayoutParams.WRAP_CONTENT;  
				int width =  DH.getWidth();  
				popView = new PopupWindow(mListView, width, height, true);  
				popView.setOutsideTouchable(true);  
				popView.setBackgroundDrawable(getResources().getDrawable(R.color.white));
				popView.setOnDismissListener(oClickListener); 
				mListView.setOnItemClickListener(onItem); 	
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
	   
	private int initCK(){
	   	SQLiteDatabase db =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
		Cursor cursor=null;
		try{
			cursor=db.query("KC", new String[]{"CK"}, null,null,null, null, null);
			System.out.println(cursor.getCount());
			if(cursor.getCount()==0){
				ck1data=new  ArrayList<String>(); 
				ck1data.add("CK001");
				return 0;
			}else{
				int i=0;
				boolean eq=false;
				ck1data=new  ArrayList<String>(); 
				while (cursor.moveToNext())
				{
					eq=false;
					for(i=0;i<ck1data.size();i++){
						if(ck1data.get(i).equals(cursor.getString(cursor.getColumnIndex("CK")))){
							eq=true;
							break;
						}	
					}
					if(!eq){
						ck1data.add(cursor.getString(cursor.getColumnIndex("CK")));
					}  
				}
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

	private void initCKPopup() {  
		
		initCK();
			mAdapter = new ArrayAdapter<String>(this,  
			android.R.layout.simple_dropdown_item_1line,ck1data);  
			mListView =new ListView(mContext); 
			mListView.setAdapter(mAdapter);  
			
			int height = ViewGroup.LayoutParams.WRAP_CONTENT;  
			int width = DCCK.getWidth();  
			//System.out.println(width);  
			popView = new PopupWindow(mListView, width, height, true);  
			popView.setOutsideTouchable(true);  
			popView.setBackgroundDrawable(getResources().getDrawable(R.color.white));
			popView.setOnDismissListener(oClickListener); 	
		}  

	private OnItemClickListener onckItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			DCCK.setText(ck1data.get(arg2));  
			 popView.dismiss(); 
		}
	};
	private OnItemClickListener onDRckItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			DRCK.setText(ck1data.get(arg2));  
			 popView.dismiss(); 
		}
	};
/*这里是点击那个向下*/		
private OnClickListener ckin =new OnClickListener() {
	@Override
	public void onClick(View v){
		// TODO 自动生成的方法存根
		System.out.println("我按下了");
		initCKPopup();
		mListView.setOnItemClickListener(onckItem); 
			if (popView!= null){  
			if (!mShowing){  
				popView.showAsDropDown(DCCK,0,-5);  
			mShowing = true;  
			}else{  
				popView.dismiss();  
			}  
		 }  
	}
};

private OnClickListener drckin =new OnClickListener() {
		@Override
		public void onClick(View v){
			// TODO 自动生成的方法存根
			System.out.println("我按下了");
			initCKPopup();
			mListView.setOnItemClickListener(onDRckItem); 
				if (popView!= null){  
				if (!mShowing){  
					popView.showAsDropDown(DCCK,0,-5);  
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
			bundle.putString("dh","DB");
			resultIntent.putExtras(bundle);
			resultIntent.setClass(DBScan.this,SearchDH.class);
			startActivity(resultIntent); 
	        return true;  
	    }  
	    return false;  
	}
	
}
