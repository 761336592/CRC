package com.example.CRC;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import myview.MyListview;
import myview.MyListview.ILoadListener;

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
import android.database.sqlite.SQLiteStatement;
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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class CKScan extends Activity implements ILoadListener{
    
	/*各种控件的定义*/
	private TextView dis;       //接收数据显示句柄
    private ScrollView sView;
    private  EditText barcode;   //条码
    private Button sendButton;   //确定按钮
   	private TextView btzt;  //蓝牙状态
   	private TextView wc;   //未传数量
    private TextView yc;   //已传数量
    private TextView BTpower; //蓝牙电量
    private Button  ConnectButton;
	private EditText  NUM;
   	private Button  searchView;
  
    private  EditText DH;        //单号
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

	private MyListview listview = null;
	private HashMap<String, Object>  map=null;
	private ArrayList<HashMap<String, Object>> listItem=null;
	private SimpleAdapter  mSimple;
	
	private int pageCount=0;
	 private int limit=20;//显示10行数据
	 private int pageNo=1;//页码
	 /*有可能是蓝牙的名称是空的所以进不去 */
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Running=21;
		mContext=this;
		 TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	     androidID=telephonyManager.getDeviceId();
	     
	 	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//解决了软件软键盘的问题
		 String[] str=null;
		/*读取文件获取员工编号和IP，端口号*/
		 setContentView(R.layout.ydcklayout);
		 btzt=(TextView) findViewById(R.id.ckbt);
		 listItem=new ArrayList<HashMap<String,Object>>();
		 yc=(TextView)findViewById(R.id.ckyc);
		 wc=(TextView)findViewById(R.id.ckwc);
		 searchView=(Button) findViewById(R.id.Button07);
		 NUM=(EditText)findViewById(R.id.CKNUM);
		
		 DH=(EditText)findViewById(R.id.CKDH);
			
		 vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE); 
		 player=new MyPlayer(mContext,vibrator);

		 BTpower=(TextView)findViewById(R.id.ckdl);
		 BTpower.setText(R.string.power);
		 barcode=(EditText)findViewById(R.id.et_sendmessage);
		 
	
		 ConnectButton=(Button)findViewById(R.id.pjlj);
		 sendButton=(Button)findViewById(R.id.btn_send);
		 ConnectButton=(Button)findViewById(R.id.pjlj);
		
		 
		 sendButton=(Button)findViewById(R.id.btn_send);
		
		/* 
		 Button searchOkButton=(Button) findViewById(R.id.pjsm);
		 searchOkButton.setOnClickListener(DHWC);
		*/ 
		 isReScan=WriteReadSD.read("isReScan",log.fdir);
		 isNUM=WriteReadSD.read("isNUM",log.fdir);
		 if(isNUM.trim().equals("1")){
			 NUM.setText("1");
		 } 
		NUM.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		
		NUM.setSelectAllOnFocus(true);
		DHimag=(ImageButton) findViewById(R.id.ckImageButton01);
		DHimag.setOnClickListener(dhin);
		//initDH();
		DH.requestFocus();
		DH.setSelectAllOnFocus(true);	
		DH.addTextChangedListener(textWatcher2);
		barcode.addTextChangedListener(textWatcher);
		barcode.setSelectAllOnFocus(true);
		
		yc.setText(R.string.Transferall);
		wc.setText(R.string.Transferpovit);
		yc.append(MainActivity.sentpovit+""); 
		wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
		vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
	//  	scanButton.setOnClickListener(scanClickListener);
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
	  		// SYS.disPlay(sView, dis, getString(R.string.Noset)+"\n");   //显示数据 
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
	
		searchView.setOnClickListener(searchon);
		
		NUM.setOnKeyListener(entOnKeyListener);
}
/**/
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
		for(int i=0;i<s.toString().length();i++){
			System.out.println("单号:"+s.toString().getBytes()[i]);
		}
		 if(SYS.indexofString(s.toString(),"\n") && s.toString().length()>0){
			 StringBuffer stringBuffer=new StringBuffer();
			 stringBuffer.append(s.toString().replace("\n"," "));
			 DH.setText(stringBuffer.toString().trim());
			// player.playScanOK(R.raw.beep);
			 int i=0;
					i=SearchDH(DH.getText().toString().trim());
					
					if(i==0 ){
						/*订单下载*/
						mHandler.removeCallbacks(btRunnable); 	
				    	mHandler.removeCallbacks(socketRunnable);
				    	mHandler.removeCallbacks(socketConnect);
				    	if(mysocket!=null)mysocket.stop();
						Intent intentPk = new Intent(); 
						intentPk.putExtra("CXDH",DH.getText().toString());
						intentPk.setClass(CKScan.this,SearchOk.class);
						startActivityForResult(intentPk, 5);
						
					}else if(i==2){
							player.playScanOK(R.raw.fail);
							//dis.setText(barcodeString);
							barcode.setText("");
							new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
						     .setPositiveButton(R.string.SMWC,new DialogInterface.OnClickListener() {//添加确定按钮  
						         @Override  
						         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件   
						             return ;   
						         }  
						     }).show();	
							DH.setText("");
							DH.requestFocus();
							return ;
					}	
		
			   new Handler().postDelayed(new  Runnable() {
						public void run() {	
							barcode.requestFocus();
						}
			   },100);
		 }else{
			 
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
	
		if(s.toString().length()>0 && SYS.indexofString(s.toString(),"\n")){
			  System.err.println("接收到的数据是："+s.toString());
			  StringBuffer stringBuffer=new StringBuffer();
				 stringBuffer.append(s.toString().replace("\n"," "));
				 barcode.setText(stringBuffer.toString().trim());
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
				      cursor2=db1.rawQuery("select * from YD where BAR=? AND DH=? ", new String[]{barcodeString,dhString});
				      if(cursor2.getCount()!=0){
					    	barcode.setText("");  	
					    	player.playScanOK(R.raw.fail);
					    	cursor2.close();
					    	db1.close();
					    	return ;
				      }
                }else{
                	if(isNUM.trim().equals("1")&&barcodeString.length()>0){	  	  
      				    if(RK(barcodeString,dhString,"1")==0){ 
      				    	barcode.setText("");
      				    	barcode.requestFocus();
      						return ;
      				    } 
  						MainActivity.sentall++;
  						yc.setText(R.string.Transferall);
  					 	wc.setText(R.string.Transferpovit);
  					 	yc.append(MainActivity.sentpovit+""); 
  					 	wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
  					 	barcode.setText("");
  				    	barcode.requestFocus();
  				    	SearchDH(dhString);
  				    	setDH(dhString);
  				    	
  					}else if(isNUM.trim().equals("0")){
  						new Handler().postDelayed(new  Runnable() {
  							public void run() {	
  								NUM.setText("");
  		  						NUM.setFocusable(true);
  		  	                    NUM.setFocusableInTouchMode(true);
  		  	                    NUM.requestFocus();
  		  	                    InputMethodManager inputManager =
  		  	                       (InputMethodManager)NUM.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
  		  	                    inputManager.showSoftInput(NUM, 0);
  							}
  					},100);
  					/*	NUM.setText("");
  						NUM.setFocusable(true);
  	                    NUM.setFocusableInTouchMode(true);
  	                    NUM.requestFocus();
  	                    InputMethodManager inputManager =
  	                       (InputMethodManager)NUM.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
  	                    inputManager.showSoftInput(NUM, 0);*/
  					}
                	
                	
                	
                } 
             

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
			int i=0;
		if(DH.getText().toString().trim().length()>0){	
			i=SearchDH(DH.getText().toString().trim());
			System.err.println("i的值是:"+i);
			if(i==0 && DH.getText().toString().length()>0 ){
				/*订单下载*/
				mHandler.removeCallbacks(btRunnable); 
		    	
		    	mHandler.removeCallbacks(socketRunnable);
		    	mHandler.removeCallbacks(socketConnect);
		    	if(mysocket!=null)mysocket.stop();
				Intent intentPk = new Intent(); 
				intentPk.putExtra("CXDH",DH.getText().toString());
				intentPk.setClass(CKScan.this,SearchOk.class);
				startActivityForResult(intentPk, 5);
				
			}else if(i==2){
					player.playScanOK(R.raw.fail);
					//dis.setText(barcodeString);
					barcode.setText("");
					new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
				     .setPositiveButton(R.string.SMWC,new DialogInterface.OnClickListener() {//添加确定按钮  
				         @Override  
				         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件   
				             return ;   
				         }  
				     }).show();	
				    DH.setText(" ");
					DH.requestFocus();
					return ;
			}	
		  }else return ;
		}
};
	
/*条码扫描返回的结果处理*/
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
	case SCANNIN_GREQUEST_CODE:
		if(resultCode == RESULT_OK){
			Bundle bundle = data.getExtras();
			 barcode.setText(bundle.getString("result"));
			 btSent=false;
			
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
			if(string.equals("无--")){
				player.playScanOK(R.raw.fail);
				if(mysocket.getState()==mySocket.STATE_NONE){
			  		mysocket.connect();
			  	}
				barcode.setText("");
				new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
			     .setPositiveButton(R.string.DHBZ,new DialogInterface.OnClickListener() {//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件   
			             return ;   
			         }  
			    }).show();	
				listItem.clear();
				DH.setText(" ");
				DH.requestFocus();
			    showListView(listItem);
			 
			}else if(string.equals("OVER")){
				player.playScanOK(R.raw.fail);
				if(mysocket.getState()==mySocket.STATE_NONE){
			  		mysocket.connect();
			  	}
				barcode.setText("");
				new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
			     .setPositiveButton(R.string.SMWC,new DialogInterface.OnClickListener(){//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件   
			             return ;   
			         }  
			     }).show();	
				listItem.clear();
				DH.setText(" ");
				DH.requestFocus();
				showListView(listItem);
			}else if(string.equals("DOING")){
				
				player.playScanOK(R.raw.fail);
				if(mysocket.getState()==mySocket.STATE_NONE){
			  		mysocket.connect();
			  	}
				barcode.setText("");
				new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
			     .setPositiveButton(R.string.DDLQ,new DialogInterface.OnClickListener() {//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which){       //确定按钮的响应事件   
			             return ;       
			         }  
			     }).show();	
				listItem.clear();
				DH.setText(" ");
				DH.requestFocus();
				showListView(listItem);
			}
			if(string.equals("fail")){
				listItem.clear();
				DH.setText(" ");
				DH.requestFocus();
			    showListView(listItem);
				Toast.makeText(mContext,"查询失败",Toast.LENGTH_SHORT).show();
			}else if(string.equals("DownOver")){
				 SearchDH(DH.getText().toString());
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
/*数字框监听  ，当软键盘上的确定键按下的时候能够直接 出库*/
OnKeyListener entOnKeyListener =new OnKeyListener() {
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if(keyCode==KeyEvent.KEYCODE_ENTER){
			if(barcode.getText().toString().length()==0){
				new AlertDialog.Builder(CKScan.this).setTitle(R.string.SYS).setPositiveButton(R.string.nosent,new DialogInterface.OnClickListener() {//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
			              return ;   
			         }  
			     }).show();//在按键响应事件中显示此对话框  
				return false;
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
						return false;
					}
					CKString="CK001";
					if(DH.getText().toString().trim().length()>0){
						dhString=DH.getText().toString().trim();
					}else{
						dhString="DH001";
					}
					String barcodeString=barcode.getText().toString().replace("\n"," ").trim();	
					if(RK(barcodeString,dhString,rkNum)==0){
				    	barcode.setText("");
				    	barcode.requestFocus();
				    	return false;
					}	
					setDH(dhString);
					MainActivity.sentall++;
					yc.setText(R.string.Transferall);
					wc.setText(R.string.Transferpovit);
					yc.append(MainActivity.sentpovit+""); 
					wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
					
					SearchDH(dhString);
					setDH(dhString);
					new Handler().postDelayed(new  Runnable() {
							public void run() {	
								barcode.setText("");
								barcode.requestFocus();
								NUM.setText("");
								
							}
					},100);
					
		}
		return false;
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
				if(RK(barcodeString,dhString,rkNum)==0){
			    	barcode.setText("");
			    	barcode.requestFocus();
			    	return ;
				}	
				setDH(dhString);
				MainActivity.sentall++;
				yc.setText(R.string.Transferall);
				wc.setText(R.string.Transferpovit);
				yc.append(MainActivity.sentpovit+""); 
				wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
				barcode.setText("");
				barcode.requestFocus();
				NUM.setText("");
				SearchDH(dhString);
				setDH(dhString);
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
/*显示*/
private void showListView(ArrayList<HashMap<String, Object>> apk_list){
	if (mSimple == null){
		listview = (MyListview) findViewById(R.id.ydlistView01);
		listview.setInterface(this);
		mSimple=new SimpleAdapter(this,apk_list,R.layout.detailsitem
				,new String[]{"1","2","3","4","5","6","7","8","9","10"}
                ,new int[]{R.id.detail1,R.id.detail2,R.id.detail3,R.id.detail4,R.id.detail5,R.id.detail6,R.id.detail7,R.id.detail8,R.id.detail9,R.id.detail10});
		listview.setAdapter(mSimple);
	}else{
		mSimple.notifyDataSetChanged();
	}
	//listview.setSelection(0);
}
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
				
					isconnect=false; //网络连接失败
					break;
				case 1:
				
					ConnectButton.setText(R.string.Noconnected);
					
					isconnect=false;
					break;
				case 2:
					
					ConnectButton.setText(R.string.connectting);
					break;	
				case 3:
				
					ConnectButton.setText(R.string.SKconnected);
					isconnect=true;
					break;	
				case 7:
					ConnectButton.setText(R.string.Noconnected);
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
				
					 break;
				case BluetoothChatService.STATE_CONNECTING:
					if(SYS.indexofString(btname,BTNAME)||SYS.indexofString(btname,"HC"))
					 btzt.setText(R.string.title_connecting);
					
					 break;
				case BluetoothChatService.STATE_LISTEN:
					if(SYS.indexofString(btname,BTNAME)||SYS.indexofString(btname,"HC")){
						RKScan.Isbt=false;
					    btzt.setText(R.string.title_not_connected);
					} 
					
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
		       /*接收订单数据*/
		    	 String barcodeString=DH.getText().toString().trim();
		    	 String name = msg.getData().getString("REC");
		    	 String num="";
		    	 int len=0;
				 String sqlString=String.format("talk%s%s%s%d%s%s,","□",androidID,"□",pageNo,"□",barcodeString);
		    	 String [] string=null;
		    	 String [] buf=null;
		    	//System.out.println("收到的消息:"+name);
		          if(isSearchinf){
		        	  isSearchinf=false;
		        	  System.out.println("资料:"+name);
		        	  if(SYS.indexofString(name,sqlString)){
		        		  buf=SYS.splitString(name.trim(),"■");
		        		  System.err.println("长度是:"+buf.length);
		        		  if(!SYS.indexofString(buf[0].trim(),sqlString) && buf.length!=2)//保证接收到的不是流水号+1
		        			  return ;
		        		  try {
								len=SYS.crc_r(buf[0].getBytes(bm),buf[0].getBytes(bm).length);
								len=len % 99;
								if(len==Integer.parseInt(buf[1].trim())){
									string=SYS.splitString(buf[0],",");
									if(string[1].equals("无--")){
										player.playScanOK(R.raw.fail);
										barcode.setText("");
										new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
									     .setPositiveButton(R.string.DHBZ,new DialogInterface.OnClickListener() {//添加确定按钮  
									         @Override  
									         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件   
									             return ;   
									         }  
									    }).show();	
										DH.setText("");
										DH.requestFocus();
										return ;
									 
									}else if(string[1].equals("OVER")){
										player.playScanOK(R.raw.fail);
										
										barcode.setText("");
										new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
									     .setPositiveButton(R.string.SMWC,new DialogInterface.OnClickListener() {//添加确定按钮  
									         @Override  
									         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件   
									             return ;   
									         }  
									     }).show();	
										DH.setText("");
										DH.requestFocus();
										return ;
									}else if(string[1].equals("DOING")){
										player.playScanOK(R.raw.fail);
										barcode.setText("");
										new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
									     .setPositiveButton(R.string.DDLQ,new DialogInterface.OnClickListener() {//添加确定按钮  
									         @Override  
									         public void onClick(DialogInterface dialog, int which){    //确定按钮的响应事件   
									             return ;   
									         }  
									     }).show();	
										DH.setText("");
										DH.requestFocus();
										return ;
									}else if(string[1].equals("DownOver")){
									}
									else{
										
										/*将订单数据写入到数据库里面*/
										String bufString[]=null;
										String bufx[]=null;
										bufx=SYS.splitString(string[1].trim(), "#");
										SQLiteDatabase dbwW =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
										sqlString="insert into YD(DH,BAR,BZ1,BZ2,BZ3,BZ4,BZ5,BZ6,BZ7,DQ,NUM1,NUM2) values (?,?,?,?,?,?,?,?,?,?,?,?)";
					          			SQLiteStatement stat=dbwW.compileStatement(sqlString);
					          			dbwW.beginTransaction();
										 for(int i=0;i<bufx.length-1;i++){
									        	bufString=SYS.splitString(bufx[i].trim(), "@");
									        	stat.bindString(1,barcodeString.trim()) ;
						          				stat.bindString(2,bufString[0].trim()) ;
						          				stat.bindString(3,bufString[1].trim()) ;
						          				stat.bindString(4,bufString[2].trim()) ;
						          				stat.bindString(5,bufString[3].trim()) ;
						          				stat.bindString(6,bufString[4].trim()) ;
						          				stat.bindString(7,bufString[5].trim()) ;
						          				stat.bindString(8,bufString[6].trim()) ;
						          				stat.bindString(9,bufString[7].trim()) ;
						          				stat.bindString(10,bufString[8].trim());
						          				stat.bindString(11,bufString[9].trim());
						          				stat.bindString(12,bufString[10].trim());
						          				stat.executeInsert();
									     }
										 dbwW.setTransactionSuccessful();
						          		 dbwW.endTransaction();
						          		/*同时更新状态*/ 
						          		cursor=dbwW.rawQuery("select * from DHCX WHERE DH=? ", new String []{barcodeString.trim()});
						          		if(cursor.getCount()==0){
						          		   sqlString="insert into DHCX(DH,BZ1) values (?,?)";
						          		   dbwW.execSQL(sqlString,new String[]{barcodeString.trim(),"0"});
						          		}	
						          	    dbwW.close();
						          		
						          		
						          		/*显示*/
						          //		pageCount=bufx.length-1;
						          		
						          	//	pageNo=1;
						          		query(pageNo,barcodeString.trim());
						          		showListView(listItem);
									}
									
								   isSearchOK=true;
								}else{
									 /*查找失败*/
								
									isSearchOK=false;
									DH.setText("");
									DH.requestFocus();
									return ;
								}
							}catch (UnsupportedEncodingException e) {
								// TODO 自动生成的 catch 块
								
								DH.setText("");
								DH.requestFocus();
								return ;
							}
		        	  }else{
		        		  
						  isSearchOK=false;
						  DH.setText("");
						  DH.requestFocus();
							return ;
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
		                  if(isNUM.trim().equals("1")){	  	  
		      				    if(RK(bar,dhString,"1")==0){ 
		      				    	barcode.setText("");
		      				    	barcode.requestFocus();
		      						return ;
		      				    } 
		  						MainActivity.sentall++;
		  						yc.setText(R.string.Transferall);
		  					 	wc.setText(R.string.Transferpovit);
		  					 	yc.append(MainActivity.sentpovit+""); 
		  					 	wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
		  					 	barcode.setText("");
		  				    	barcode.requestFocus();
		  				    	SearchDH(dhString);
		  				    	setDH(dhString);
		  				    	
		  					}else if(isNUM.trim().equals("0")){
		  						new Handler().postDelayed(new  Runnable() {
		  							public void run() {	
		  								NUM.setText("");
		  		  						NUM.setFocusable(true);
		  		  	                    NUM.setFocusableInTouchMode(true);
		  		  	                    NUM.requestFocus();
		  		  	                    InputMethodManager inputManager =
		  		  	                       (InputMethodManager)NUM.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		  		  	                    inputManager.showSoftInput(NUM, 0);
		  							}
		  					},100);
		  					/*	NUM.setText("");
		  						NUM.setFocusable(true);
		  	                    NUM.setFocusableInTouchMode(true);
		  	                    NUM.requestFocus();
		  	                    InputMethodManager inputManager =
		  	                       (InputMethodManager)NUM.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		  	                    inputManager.showSoftInput(NUM, 0);*/
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
   
        if (RKScan.mChatService == null) setupChat();
        else{
        	RKScan.mChatService.setHandler(mHandler);
        }
    
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
	
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){   
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
/* 0 表示单号不存在     1   正在验单       2  已经完成   */
private  int  SearchDH(String DH){
	 SQLiteDatabase db =LoginOpenHelper.getInstance(CKScan.this,log.DB_NAME).getReadableDatabase();
	 Cursor cursor=null; 
	 int i=0;
	try {
		cursor=db.rawQuery("select * from DHCX WHERE DH=? ", new String []{DH.trim()});
		 if(cursor.getCount()==0){
			  i=0;
		 }else{
			 cursor.moveToNext();
			 if(cursor.getString(cursor.getColumnIndex("BZ1")).equals("0")){
				
				Cursor cursor1=db.rawQuery("select * from  YD WHERE DH= ? AND NUM1 != NUM2 ",new String[]{DH} );
				pageCount=cursor1.getCount();
				cursor1.close();
				pageNo=1;
				barcode.requestFocus();
				listItem.clear();
				query(pageNo,DH);
				showListView(listItem);	 
				i=1;
			 }else if(cursor.getString(cursor.getColumnIndex("BZ1")).equals("1")){
				 i=2;
			 }	
		 }
	} catch (Exception e) {
		// TODO: handle exception
		i=3;  //查询失败
	} 
	cursor.close();
    db.close();
	
	return i;
	
}
/*要查询一下看看单据是不是完成了，如果完成了，可以了要写入标志位*/
private  void setDH(String DH)
{
	SQLiteDatabase db =LoginOpenHelper.getInstance(CKScan.this,log.DB_NAME).getReadableDatabase();
    Cursor cursor=null;
    int num=0;
    String sqlString="select * from YD where DH=? ";
    cursor=db.rawQuery(sqlString, new String[]{DH});
    if(cursor.getCount()==0){ 
    	
    	
    } 	
    else{
    	
      while(cursor.moveToNext()){
    	    if(Integer.parseInt(cursor.getString(cursor.getColumnIndex("NUM1")))==
    	        Integer.parseInt(cursor.getString(cursor.getColumnIndex("NUM2")))){
    	    	 num++;
    	    }  
      }
      if(num==cursor.getCount()){
    	  /*表示这个订单已经完成了*/
    	  sqlString="update DHCX set BZ1 =? where DH=?";
    	  db.execSQL(sqlString, new String[]{"1",DH.trim()});
      }	
    }
    cursor.close();
	db.close();
}
/*查询数据库  并将数据显示出来*/
private void query(int pageNo,String str){
    
    SQLiteDatabase db =LoginOpenHelper.getInstance(CKScan.this,log.DB_NAME).getReadableDatabase();
    Cursor cursor=null;    
    String string= String.format("SELECT * from YD  Where DH =? AND NUM1!=NUM2 order by BZ7")+" limit ?,? ";
    cursor=db.rawQuery(string,new String[] {str.trim(),String.valueOf(limit*(pageNo-1)),String.valueOf(limit)});
    System.err.println("查询到的结果是"+cursor.getCount());
    if(cursor.getCount()>0){
	    while(cursor.moveToNext()){
	   	  map=new  HashMap<String, Object>();
	   	    map.put("1",cursor.getString(cursor.getColumnIndex("BZ1")).toString());
	   	    map.put("2",cursor.getString(cursor.getColumnIndex("BAR")).toString());
	   	    map.put("3",cursor.getString(cursor.getColumnIndex("BZ7")).toString());
	   	    map.put("4",cursor.getString(cursor.getColumnIndex("BZ5")).toString());
	     	map.put("5",cursor.getString(cursor.getColumnIndex("BZ2")).toString());
	     	map.put("6",cursor.getString(cursor.getColumnIndex("BZ6")).toString());
	     	map.put("7",cursor.getString(cursor.getColumnIndex("BZ3")).toString());
	     	map.put("8",cursor.getString(cursor.getColumnIndex("BZ4")).toString());
	     	map.put("9",cursor.getString(cursor.getColumnIndex("NUM1")).toString());
	     	map.put("10",cursor.getString(cursor.getColumnIndex("NUM2")).toString());
	   	    listItem.add(map);  
	     } 
    }
     cursor.close();
     db.close();
 }


/*入库*/



private  int RK(String bar,String DH,String rkNum){

	/*保存的数据       条码      单号      应扫数量           已扫数量*/
	sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd ");
	date=sDateFormat.format(new   java.util.Date()); 
	String sqlString="";
	Cursor cursor=null;
	SQLiteDatabase db=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
	sqlString="select * from YD WHERE BAR =? AND DH=?";
	cursor=db.rawQuery(sqlString, new String[]{bar.trim(),DH.trim()});
	if(cursor.getCount()==0){
		sqlString=String.format("条码:%s不在订单里面",bar);
       player.playScanOK(R.raw.fail);	
		new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
	     .setPositiveButton(sqlString,new DialogInterface.OnClickListener() {//添加确定按钮  
	         @Override  
	         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件   
	             return;   
	         }  
	     }).show();	
		cursor.close();
		db.close();
		return 0; 
	}else {
	     cursor.moveToNext();
	  	 if(Integer.parseInt(rkNum)+Integer.parseInt(cursor.getString(cursor.getColumnIndex("NUM2")))
	  		 >Integer.parseInt(cursor.getString(cursor.getColumnIndex("NUM1")))){
		  		player.playScanOK(R.raw.fail);	
		  		sqlString=String.format("条码:%s出库数大于应扫数量",bar);
				new AlertDialog.Builder(mContext).setTitle(R.string.SYS)//设置对话框标题  
			     .setPositiveButton(sqlString,new DialogInterface.OnClickListener() {//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件   
			             return;   
			         }  
			     }).show();	
				cursor.close();
				db.close();
				return 0;      
	  	 }
	  	 /* 更新数据*/
	  	 int num=Integer.parseInt(rkNum)+Integer.parseInt(cursor.getString(cursor.getColumnIndex("NUM2")));
	 	 sqlString="update YD set NUM2=? ,TIME=? ,YGBH=?  where DH =? and BAR =?";
	 	 db.execSQL(sqlString, new String[]{String.valueOf(num),date,YGDL.YG,DH,bar});   

	 	 String  rkString=""; 
		  rkString ="YD-"+DH+"|"+bar.trim()+","+rkNum.trim()+","+date+","+YGDL.YG; 
		  ContentValues cv= new ContentValues();
		  cv.put("BAR", rkString);
		  cv.put("ISsent","1");  //表示未传
		  db.insert("SENT", null, cv);
		  
		  cursor.close();
		  db.close();
		  return 1;
	}
 
}
   /*关于仓库和单号的操作*/
	private int initDH(){
	   	SQLiteDatabase db =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
		Cursor cursor=null;
		String sqlString="select * from  DHCX ";
		cursor=db.rawQuery(sqlString,null);
			if(cursor.getCount()==0){
				cursor.close();
				db.close();
				return 0;
			}else{
				dhdata=new  ArrayList<String>(); 
				while (cursor.moveToNext()){
					if(cursor.getString(cursor.getColumnIndex("BZ1")).equals("0")){
						dhdata.add(cursor.getString(cursor.getColumnIndex("DH")));
					}
				}
				db.close();
				// DH.setText(name);
				cursor.close();
				return 1;
			}
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
			SQLiteDatabase db =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
			Cursor	cursor=db.rawQuery("select * from  YD WHERE DH= ? AND NUM1 != NUM2 ",new String[]{dhdata.get(arg2)} );
			pageCount=cursor.getCount();
			cursor.close();
			db.close();
			pageNo=1;
			barcode.requestFocus();
			listItem.clear();
			query(pageNo, dhdata.get(arg2));
			showListView(listItem);	
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
/*	MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);*/
	return true;
}
public boolean onOptionsItemSelected(MenuItem item) {  
    switch (item.getItemId()) {  
    case R.id.DHLL:  
    /*	Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("dh","CK");
		resultIntent.putExtras(bundle);
		resultIntent.setClass(CKScan.this,SearchDH.class);
		startActivity(resultIntent); */
        return true;  
     
    }  
    return false;  
}

		@Override
		public void onLoad() {
			// TODO 自动生成的方法存根
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
				//	getLoadData();
					if(pageCount>pageNo) {
						pageNo++;
						query(pageNo,DH.getText().toString());
					}
					showListView(listItem);  //显示
			
					listview.isOver();  //加载结束
				}
			}, 500);
		}
}
