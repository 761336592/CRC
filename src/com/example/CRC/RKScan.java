package com.example.CRC;

import java.io.File;
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

public class RKScan extends Activity{

	private TextView dis;       //接收数据显示句柄
    private ScrollView sView;
    private  EditText barcode;   //条码
    private  EditText KCsl;      //库存
    private Button sendButton;   //确定按钮
   	private TextView btzt;  //蓝牙状态
   	private TextView wc;   //未传数量
    private TextView yc;   //已传数量
    private TextView BTpower; //蓝牙电量
    private TextView printTextView;
   	private Button  printButton;
   	private Button  ConnectButton;
   	private EditText  NUM;
   	private Button  sjsm;
    private  EditText CK;      //库存
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
	private String rkNum="";
	private static final String bm = "GBK";
	 private String BTNAME="RS-"; //存放蓝牙设备名称，用于区分枪和打印机
	 public static BluetoothChatService mChatService = null;
	 public static BluetoothChatService printService = null;
	 private mySocket mysocket=null;
	 Vibrator vibrator=null;
	 
	private final static int SCANNIN_GREQUEST_CODE = 1;
	public static final  int REQUEST_CONNECT_DEVICE = 2;    //宏定义查询设备句柄
	private static final int REQUEST_ENABLE_BT = 3;
	private  BluetoothDevice _device = null;     //蓝牙设备
	public static boolean Isbt=false;
	public static boolean isPrint=false;  //蓝牙打印机
	boolean isconnect=false;
	boolean btSent=false;   //是蓝牙传输过来的，或者是手机扫描的
	boolean isSearchinf=false;
	boolean isSearchOK=false;
	
	public static  boolean setInf=false; //跳转到资料更新的界面
	private int Running=21;
	private boolean isSent=false;
	
	private String isReScan="";    //是否允许重复扫描；
	private String isNUM="";      //是否需要数量
	private SimpleDateFormat   sDateFormat;   //时间
	private String   date="" ;
	private PopupWindow popView;
	private ArrayAdapter<String> mAdapter;  
	private ListView mListView; 
	private ArrayList<String> dhdata=null;
	private	ArrayList<String> ckdata=null;
	private boolean mShowing;
	private String androidID="";
    protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.ydcklayout);
	}
	
	
/*条码扫描返回的结果处理*/
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    String bufString="";
    switch (requestCode) {
	case SCANNIN_GREQUEST_CODE:
		if(resultCode == RESULT_OK){
			
		}
		break;
	case REQUEST_CONNECT_DEVICE:
		if(resultCode==RESULT_OK){
			 String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
			  WriteReadSD.writex(address, "BTMAC",log.fdir);
			 _device= MainActivity._bluetooth.getRemoteDevice(address);
					// 试图连接到装置
					mChatService.connect(_device);
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
			  printService.connect(_device);
		}
		break;   
   }
}
public void connect(){

		Text_of_output="";
		//Text_of_output=sys.My_ReadFile(is, "BTMAC", mContext);
		Text_of_output=WriteReadSD.read("BTMAC",log.fdir);
		_device =null;
		/*有就直接连接*/
		if(Text_of_output.length()>0){
			_device = MainActivity._bluetooth.getRemoteDevice(Text_of_output.trim());
		   mChatService.connect(_device);
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
		   printService.connect(_device);
		}
	return;
} 
public void onStart() {
    super.onStart();
   
  
}
private void setupChat() {
	
      
}
public void onDestroy() {
    super.onDestroy();
    Log.i("rksocket", "onDestroy");
    // 停止蓝牙
   // if (mChatService != null) mChatService.stop();
    if(mysocket!=null) mysocket.stop();
 //   if ( printService != null) printService.stop();
}
public synchronized void onResume() {
		super.onResume();
		 Log.i("rksocket", "onResume");
		
	}
public void onStop() {
    super.onStop();
   
   
 }
public synchronized void onPause() {
    super.onPause();
    Log.e("rksocket", "- ON PAUSE -");
}
public boolean onKeyDown(int keyCode, KeyEvent event)  {  
	
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
    	
    	RKScan.this.finish();
    }else if(keyCode == KeyEvent.KEYCODE_MENU){
       return false;
    }
    return true; 
} 



/*****************************************************************************
 * 
 *  消息处理
 * 
 * 
 * ***************************************************************************/
private Runnable printRunnable=new Runnable() {
	
	@Override
	public void run() {
		System.out.println("我进来连接打印机了");
		// TODO 自动生成的方法存根
		 if(!isPrint && printService.getState()!=BluetoothChatService.STATE_CONNECTING){
			 connectprint();
		 }else {
			 System.out.println("打印机已经连接了");
		 }
		 mHandler.postDelayed(this,5000); 
	}
};
/*定时检测蓝牙*/	
private Runnable btRunnable=new Runnable() {
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		System.out.println("我进来了");
			 if(!Isbt){
			      if(mChatService.getState()!=BluetoothChatService.STATE_CONNECTING)
			      connect();
			    }else{
			    	System.out.println("蓝牙枪已经连接了");
			    } 
			  mHandler.postDelayed(this, 5000);
		 }
	
};
/*定时检测数据库*/
private Runnable socketRunnable=new Runnable() {
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
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
		System.out.println("我进来连接服务器");
		if(mysocket.getState()==mySocket.STATE_NONE||mysocket.getState()==mySocket.STATE_LISTEN || !isconnect){
			mysocket.connect();
		}else{
			System.out.println("服务器已经连接了");
			//return ;
		}
		mHandler.postDelayed(this,500000);
	}
};
Handler mHandler = new Handler(){
	 public void handleMessage(Message msg){
		
			super.handleMessage(msg);
			switch(msg.what)
			{
		    
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

private  void RK(String bar,String DH,String CK,String rkNum){
	

		
}


private void seaCK(String barcodeString,String ck){
	
}
/*搜索本地资料并显示出来*/
private  void  SearchINF(String barcodes){
	
	
} 
private void Searchys(String bar,String DH,String CK)
{

}

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
		bundle.putString("dh","RK");
		resultIntent.putExtras(bundle);
		resultIntent.setClass(RKScan.this,SearchDH.class);
		startActivity(resultIntent); 
        return true;  
     
    }  
    return false;  
}

}
