package com.example.CRC;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.mining.app.socket.mySocket;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class KCCX extends Activity{
 
	/*���ֿؼ��Ķ���*/
	private TextView dis;       //����������ʾ���
    private ScrollView sView;
    private  EditText barcode;   //����
    private  EditText KCsl;      //���
    private TextView btzt;  //����״̬
   	private TextView BTpower; //��������
   	private Button  scanButton;
   	private Button  ConnectButton;
    private  EditText CK;      //���
   	
   	private String  Text_of_output="";
   	private String recvMessageserver ="";
   	private Context mContext=null;
    private String iPString="";
	private String portString="0";
	private String CKString="";
	 private static final String bm = "GBK";
	 private String fwq="";
	 private mySocket mysocket=null;
	//	private BluetoothChatService mChatService = null;
	 Vibrator vibrator=null;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	public static final  int REQUEST_CONNECT_DEVICE = 2;    //�궨���ѯ�豸���
	private static final int REQUEST_ENABLE_BT = 3;
	private  BluetoothDevice _device = null;     //�����豸
	//boolean Isbt=false;
	boolean isconnect=false;
	private String androidID="";
	private ImageButton CKimag;
	
	 private PopupWindow popView;
		private ArrayAdapter<String> mAdapter;  
		private ListView mListView; 
			 ArrayList<String> ckdata=null;
		  private boolean mShowing;
		  protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kccx);
		mContext=this;
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		 TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	     androidID=telephonyManager.getDeviceId();
		 String[] str=null;
		 new SYS();
		/*��ȡ�ļ���ȡԱ����ź�IP���˿ں�*/
		btzt=(TextView) findViewById(R.id.KCbt);
		dis=(TextView)findViewById(R.id.kctextview);   
		sView=(ScrollView)findViewById(R.id.kcSview);
		KCsl=(EditText)findViewById(R.id.kcKC);
		KCsl.setEnabled(false);
		CK=(EditText)findViewById(R.id.kcck);
		 scanButton=(Button)findViewById(R.id.kcsjsm);
		 barcode=(EditText)findViewById(R.id.kc_sendmessage);
		 CKimag=(ImageButton) findViewById(R.id.KCImageButton02);
	  	 barcode.setText("");
		 Text_of_output="";
		 Text_of_output=WriteReadSD.read("IP", log.fdir);
		 System.out.println("IP��:"+Text_of_output.trim());
	  	 if(Text_of_output.length()>0){
	  		str=SYS.splitString(Text_of_output.trim(),":");
	  		iPString=str[0].trim();
	  		portString=str[1].trim();
	  	 }else{
	  		  SYS.disPlay(sView, dis, getString(R.string.Noset)+"\n");   //��ʾ����   
	  	 }
	  	initCK();
	  	CKimag.setOnClickListener(ckin);
	  	Text_of_output="";
	  	Text_of_output=WriteReadSD.read("FWQM", log.fdir);
	 	 if(Text_of_output.length()>0){
	 		fwq=Text_of_output.trim();
	 	 }else{
	 		fwq="RS";
	 	 }
	 	BTpower=(TextView)findViewById(R.id.KCdl);
		BTpower.setText(R.string.power);
	  	ConnectButton=(Button)findViewById(R.id.KClj);
		vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
	  	/*���ӷ�����*/
		//if(mysocket.getState()==mySocket.STATE_NONE || mysocket.getState()==mySocket.STATE_LISTEN ||!isconnect|| mysocket.getState()==mySocket.STATE_FAILE){
		mysocket=null;
		mysocket=new mySocket(this, mHandler,iPString,Integer.parseInt(portString));
	  	if(iPString.length()>0){
	  		System.out.println("��濪ʼ����");
	  		mysocket.connect();
	  	}else{
	  		ConnectButton.setText(R.string.SKconnected);
	  	}
	  	if(iPString.length()>0){
	  		ConnectButton.setOnClickListener(socketconnect);
	  	}
	  	scanButton.setOnClickListener(scanClickListener);
	  	if(!RKScan.Isbt){
	    	btzt.setText(R.string.title_not_connected);
	    	//connect();
	    }else{
	    	 btzt.setText(R.string.title_connected_to);
	    }
		mHandler.postDelayed(btRunnable, 5000);
		mHandler.postDelayed(socketConnect,1000*60);
		btzt.setOnClickListener(Btset);
		Button search=(Button)findViewById(R.id.kcsear);
		search.setOnClickListener(searchon);
		Button Scanbutton=(Button)findViewById(R.id.kcsm);
		Scanbutton.setOnClickListener(scanbuttOnClickListener);
	}
	
private OnClickListener Btset =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
			RKScan.Isbt=false;
			Intent serverIntent = new Intent(KCCX.this, DeviceListActivity.class); //��ת��������
			startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);  //���÷��غ궨��*/
		}
	};
	
private OnClickListener searchon =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
			
			if(CK.getText().toString().trim().length()==0){
				CKString="CK001";
				}else{
					CKString=CK.getText().toString().trim();
				}
			String	bufString="QUERY��"+barcode.getText().toString()+"��"+CKString+"��"+"CX"; 
			//  barcodeString=barcode.getText().toString().trim();
	        /*�����ϵĻ��ͷ��Ͳ�ѯָ��*/
			if(mysocket.getState()==mySocket.STATE_CONNECTED||isconnect){
				 mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
			}else{
				seaCK(barcode.getText().toString(),CKString) ;
				 SearchINF(barcode.getText().toString()); 
			}
			/*SearchINF(barcode.getText().toString());
			seaCK(barcode.getText().toString(),CKString) ;*/
		}
	};
private OnClickListener scanbuttOnClickListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
			for(int i=0;i<6;i++)
				sendbtMessage("S\n");
		}
	};		
/*���������Ӱ�ť*/
private OnClickListener socketconnect=new OnClickListener() {
	
	@Override
	public void onClick(View v){
		// TODO �Զ����ɵķ������
		if(mysocket.getState()!=mySocket.STATE_CONNECTING){
		  if(mysocket.getState()==mySocket.STATE_CONNECTED||isconnect){
			mysocket.stop();
			isconnect=false;
		  }else if(mysocket.getState()==mySocket.STATE_NONE||mysocket.getState()==mySocket.STATE_LISTEN || !isconnect){
			mysocket.connect();
		  }
		}	
	}
}; 
/*����ɨ�谴��*/
private OnClickListener scanClickListener=new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		Intent intent1 = new Intent();
		intent1.setClass(KCCX.this, MipcaActivityCapture.class);
		intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent1, SCANNIN_GREQUEST_CODE);
	}
};
/*����ɨ�践�صĽ������*/
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    String bufString="";
    switch (requestCode) {
	case SCANNIN_GREQUEST_CODE:
		if(resultCode == RESULT_OK){
			Bundle bundle = data.getExtras();
			 barcode.setText(bundle.getString("result"));
			if(CK.getText().toString().trim().length()==0){
				CKString="CK001";
				}else{
					CKString=CK.getText().toString().trim();
				}
				bufString="QUERY��"+barcode.getText().toString()+"��"+CKString+"��"+"CX"; 
		
	        /*�����ϵĻ��ͷ��Ͳ�ѯָ��*/
			if(mysocket.getState()==mySocket.STATE_CONNECTED||isconnect){
				 mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
			}else{
				seaCK(barcode.getText().toString(),CKString) ;
				 SearchINF(barcode.getText().toString()); 
			}
			/*û�����ӵĻ���ֱ�Ӳ�ѯ�������ݿ�*/	
		}
		break;
	case REQUEST_CONNECT_DEVICE:
		if(resultCode==RESULT_OK){
			 String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
			  WriteReadSD.writex(address, "BTMAC",log.fdir);
			 _device= MainActivity._bluetooth.getRemoteDevice(address);
		}
		break;
	case REQUEST_ENABLE_BT:
         // �ж������Ƿ�����
         if (resultCode == Activity.RESULT_OK) { 
             setupChat();
         } else{
             finish();
         }
         break;
   }
}
/*��Ϊ��Ϣ�����*/	
private Runnable btRunnable=new Runnable() {
	
	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		 synchronized (this){
				  if(!RKScan.Isbt){
				//      btzt.setText("ɨ��ǹδ����");
				      if(RKScan.mChatService.getState()!=BluetoothChatService.STATE_CONNECTING)
				      connect();
				    }else{
				     
				    } 
				  mHandler.postDelayed(this, 5000); 
		 }
	}
};
/*��ʱ��� �������Ƿ����� */
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
			switch(msg.what)
			{	
		     case 0:
				  break;
		     case 1:            //���������״̬��
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
						 break;
					case BluetoothChatService.STATE_NONE:
					     btzt.setText(R.string.title_not_connected);
					     RKScan.Isbt=false;
						 break;
					}
					break;	  
		     case 17: //��ȡ���յ�������
		    	 String name = msg.getData().getString("REC");
		    	 String [] string=null;
		    	 String [] buf=null;
		    	 int len=0;
		         dis.setText("");
		    	 if(SYS.indexofString(name,"talk��"+androidID+"��")){
		    		 buf=SYS.splitString(name.trim(),"��"); 
		    		 try {
						len=SYS.crc_r(buf[0].getBytes(bm),buf[0].getBytes(bm).length);
						len=len % 99;
						if(len==Integer.parseInt(buf[1].trim())){
							string=SYS.splitString(buf[0],",");
							if(string[1].equals("��--")){
							   KCsl.setText(string[3]);
							   SearchINF(barcode.getText().toString()); 
							}else{
								dis.setText("");
								for(int i=2;i<string.length-1;i+=2){
									SYS.disPlay(sView, dis,string[i]+":"+string[i+1]+"\n");
								}
							  KCsl.setText(string[string.length-8]);
							}
							
						}else{
							 //�Ҳ����Ͳ鱾������
							if(CK.getText().toString().trim().length()==0){
								CKString="CK001";
								}else{
									CKString=CK.getText().toString().trim();
								}
								seaCK(barcode.getText().toString(),CKString) ;
								 SearchINF(barcode.getText().toString()); 
							
						}
					} catch (UnsupportedEncodingException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
		    	 }else{
		    		 //û�нӵ���ô�ò��ұ������ݿ���
		    		 if(CK.getText().toString().trim().length()==0){
							CKString="CK001";
							}else{
								CKString=CK.getText().toString().trim();
							}
							seaCK(barcode.getText().toString(),CKString) ;
							 SearchINF(barcode.getText().toString()); 
		    	 }
		    	
		    	 break;
		     case 4:
				btzt.setText(R.string.title_connected_to);
				RKScan.Isbt=true;
					break;
				case 5:
					btzt.setText(R.string.title_not_connected);
				//	vibrator.vibrate(500);
					RKScan.Isbt=false;
					//connect();
					break;
		     case 16:
		    	 switch (msg.arg1) {
				case 0:
					ConnectButton.setText(R.string.Noconnected);
				//	vibrator.vibrate(800);
					dis.setText("");
					SYS.disPlay(sView, dis, getString(R.string.dk)+"\n");
					isconnect=false; //��������ʧ��
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
					//vibrator.vibrate(500);
					SYS.disPlay(sView, dis, getString(R.string.connectting)+"\n");
					ConnectButton.setText(R.string.connectting);
				//	isconnect=true;
					break;	
				case 3:
					dis.setText("");
				//	vibrator.vibrate(500);
					SYS.disPlay(sView, dis, getString(R.string.Sockted)+"\n");
					ConnectButton.setText(R.string.SKconnected);
					isconnect=true;
					break;	
				case 7:
					dis.setText("");
					ConnectButton.setText(R.string.Noconnected);
					//vibrator.vibrate(500);
					SYS.disPlay(sView, dis, getString(R.string.IP)+"\n");
					isconnect=false;
					break;
				/*case 6:	
					if(CK.getText().toString().trim().length()==0){
						CKString="CK001";
						}else{
							CKString=CK.getText().toString().trim();
						}
						seaCK(barcode.getText().toString(),CKString) ;
						 SearchINF(barcode.getText().toString()); 
					 break; */
				default:
					break;
				}
		    	 break;
		     case 13:
		    	 byte[] readBuf = (byte[]) msg.obj;
		    	 try {
					String s= new String(readBuf, 0, msg.arg1,bm);
					recvMessageserver+=s ;
					if(recvMessageserver.substring(recvMessageserver.length()-1,recvMessageserver.length()).equals("\n")){
						String bar="";
						String bufString="";
						bar=SYS.BTistrue(recvMessageserver);
						String recvMessageClient = recvMessageserver;
						recvMessageserver="";
						if(bar.length()>0){
						  MainActivity.playScanOK(mContext);
		                    dis.setText("");
			   				 sView.scrollTo(0,dis.getMeasuredHeight());
		                    barcode.setText(bar);
		                    if(CK.getText().toString().trim().length()==0){
		        				CKString="CK001";
		        			  }else{
		        				CKString=CK.getText().toString().trim();
		        			}
		        			bufString="QUERY��"+barcode.getText().toString()+"��"+CKString+"��"+"CX"; 
		        	        /*�����ϵĻ��ͷ��Ͳ�ѯָ��*/
		        			if(isconnect){
		        				 mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
		        			}else{
		        				seaCK(barcode.getText().toString(),CKString);
		        				 SearchINF(barcode.getText().toString());
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
								return ;
							}						
						}
					}		
				} catch (UnsupportedEncodingException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
		    	 break;
			   default:
				break;
			}
	 }
	
};	

/*�������ͺ���*/
private void sendbtMessage(String message) {
     if (RKScan.mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
    	 SYS.disPlay(sView, dis, getString( R.string.not_connected)+"\n");
       //   Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
         // vibrator.vibrate(500);
          return;
      }
      if (message.length() > 0) {
          byte[] send = message.getBytes();
          RKScan.mChatService.write(send);
      }
  }
public void connect(){

		Text_of_output="";
		Text_of_output=WriteReadSD.read("BTMAC",log.fdir);
		/*�о�ֱ������*/
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
	
       // ��ʼ��BluetoothChatService������������
	RKScan.mChatService = new BluetoothChatService(this, mHandler);
	RKScan.mChatService.setHandler(mHandler);
       connect();
       new StringBuffer("");
   }
public void onDestroy() {
    super.onDestroy();
    Log.i("KC", "onDestroy");
    // ֹͣ����
  //  if (mChatService != null) mChatService.stop();
    if(mysocket!=null) mysocket.stop();
}
public synchronized void onResume() {
		super.onResume();
		 Log.i("KC", "onResume");
		 if(mysocket!=null){
				if(mysocket.getState()==mySocket.STATE_NONE || mysocket.getState()==mySocket.STATE_LISTEN || mysocket.getState()==mySocket.STATE_FAILE){
					mysocket.connect();
				}
			}
			if (RKScan.mChatService != null){
				if (RKScan.mChatService.getState()==BluetoothChatService.STATE_NONE){
					// ���������������
					RKScan.mChatService.start(); 
				}
			}
	}
public void onStop() {
    super.onStop();
    Log.i("KC", "onStop");
    mHandler.removeCallbacks(btRunnable); 
    mHandler.removeCallbacks(socketConnect); 
    mHandler.removeCallbacks(socketConnect); 
 }
public synchronized void onPause() {
    super.onPause();
    Log.e("KC", "- ON PAUSE -");
}
public boolean onKeyDown(int keyCode, KeyEvent event)  {  
	
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
    	mHandler.removeCallbacks(btRunnable);
    	mHandler.removeCallbacks(socketConnect);
    	mHandler.removeCallbacks(socketConnect);
    	KCCX.this.finish();
         // SysApplication.getInstance().exit();
    }else if(keyCode == KeyEvent.KEYCODE_MENU){
       return false;
    }
    return true;                                                                   
} 
private void seaCK(String barcodeString,String ck){
	SQLiteDatabase db = LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
	Cursor cursor=null;
	 cursor = db.query("KC", new String[] { "BAR",
			"CK", "KCNUM" }, "BAR=? and CK=?",
			new String[] { barcodeString.trim(),
					ck.trim() }, null,
			null, null);
	try{
		if (cursor.getCount() != 0)
		{
			while (cursor.moveToNext()){
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
/*�����������ϲ���ʾ����*/
private  void  SearchINF(String barcodes){
/*	LoginOpenHelper sqlHelper = new LoginOpenHelper(
			KCCX.this, "AndroidPosJxc.db", null, 1);*/
	SQLiteDatabase db = LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
	Cursor c=null;
	try{
	c = db.query("JBZL", new String[] { "INF1",
			"INF2", "INF3", "INF4", "INF5", "INF6", "INF7",
			"INF8", "INF9", "INF10", "INF11" }, "INF1=?",
			new String[] {barcodes.trim() },
			null, null, null);
		
	String[] arr =new String[10];
	if (c.getCount() != 0)
	{
		c.moveToFirst();
		arr[0] = c.getString(
				c.getColumnIndex("INF2"))
				.toString();
		arr[1] = c.getString(
				c.getColumnIndex("INF3"))
				.toString();
		arr[2] = c.getString(
				c.getColumnIndex("INF4"))
				.toString();
		arr[3] = c.getString(
				c.getColumnIndex("INF5"))
				.toString();
		arr[4] = c.getString(
				c.getColumnIndex("INF6"))
				.toString();
		arr[5] = c.getString(
				c.getColumnIndex("INF7"))
				.toString();
		arr[6] = c.getString(
				c.getColumnIndex("INF8"))
				.toString();
		arr[7] = c.getString(
				c.getColumnIndex("INF9"))
				.toString();
		arr[8] = c.getString(
				c.getColumnIndex("INF10"))
				.toString();
		arr[9] = c.getString(
				c.getColumnIndex("INF11")).toString();
			dis.setText("");
			sView.scrollTo(0,dis.getMeasuredHeight());
		for (int i = 0; i < arr.length; i++){
				
                SYS.disPlay(sView, dis, arr[i]+"\n");
		}
	
	} else{
		System.out.println("-----------");
		//	vibrator.vibrate(500);
			dis.setText("");
			sView.scrollTo(0,dis.getMeasuredHeight());
          SYS.disPlay(sView, dis,getString(R.string.NOinft)+"\n");
	}
  }catch(Exception e){
	     e.printStackTrace();
		}finally{
		    if(c != null){
		        c.close();
		    }
		}
   } 
private int initCK(){
   	SQLiteDatabase db =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
	Cursor cursor=null;
	try{
		cursor=db.query("KC", new String[]{"CK"}, null,null,null, null, null);
		System.out.println(cursor.getCount());
		if(cursor.getCount()==0){
			ckdata=new  ArrayList<String>(); 
			ckdata.add("CK001");
			cursor.close();
			 CK.setText("CK001");
			return 0;
		}else{
			int i=0;
			boolean eq=false;
			ckdata=new  ArrayList<String>(); 
			while (cursor.moveToNext())
			{
				eq=false;
				for(i=0;i<ckdata.size();i++){
					if(ckdata.get(i).equals(cursor.getString(cursor.getColumnIndex("CK")))){
						eq=true;
						break;
					}	
				}
				if(!eq){
					ckdata.add(cursor.getString(cursor.getColumnIndex("CK")));
				}  
			}
			String name=ckdata.get(ckdata.size()-1);
			System.out.println(name);
			 CK.setText(name);
			CK.setSelection(ckdata.size());
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
		android.R.layout.simple_dropdown_item_1line,ckdata);  
		mListView =new ListView(mContext); 
		mListView.setAdapter(mAdapter);  
		
		int height = ViewGroup.LayoutParams.WRAP_CONTENT;  
		int width =  CK.getWidth();  
		//System.out.println(width);  
		popView = new PopupWindow(mListView, width, height, true);  
		popView.setOutsideTouchable(true);  
		popView.setBackgroundDrawable(getResources().getDrawable(R.color.white));
		popView.setOnDismissListener(oClickListener); 
		mListView.setOnItemClickListener(onckItem); 
		
	}  
private OnDismissListener oClickListener=new OnDismissListener() {
	@Override
	public void onDismiss() {
		// TODO �Զ����ɵķ������
		mShowing = false;
	}
};
private OnItemClickListener onckItem=new OnItemClickListener(){
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO �Զ����ɵķ������
		CK.setText(ckdata.get(arg2));  
		 popView.dismiss(); 
	}
};
/*�����ǵ���Ǹ�����*/	
private OnClickListener ckin =new OnClickListener() {

@Override
public void onClick(View v){
	// TODO �Զ����ɵķ������
	System.out.println("�Ұ�����");
	initCKPopup();  

		if (popView!= null){  
		if (!mShowing){  
			popView.showAsDropDown(CK,0,-5);  
		mShowing = true;  
		}else{  
			popView.dismiss();  
		}  
	 }  
}
};	
}
