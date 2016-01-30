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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JBZL extends Activity{
	private  EditText barcode;   //条码
 
    private Button sendButton;   //确定按钮
   	private TextView btzt;  //蓝牙状态
   	private TextView wc;   //未传数量
    private TextView yc;   //已传数量
    private TextView BTpower; //蓝牙电量
   	private Button  ConnectButton;
   	private Button  sjsm;
    /*资料更新的空键变量*/
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;
    private Button b7;
    private Button b8;
    private Button b9;
    private Button b10;
	
    private EditText inf1;
    private EditText inf2;
    private EditText inf3;
    private EditText inf4;
    private EditText inf5;
    private EditText inf6;
    private EditText inf7;
    private EditText inf8;
    private EditText inf9;
    private EditText inf10;
    private TextView printTextView;
    String nameString="";
	String hhString="";
	String ggString="";
	String xhString="";
	private String BH="";
	private String SentData="";
	float JJ=0;
	float SJ=0;
	String DW="";
	String BZ1="";
	String BZ2="";
	String BZ3="";
   
	private String priString="";
    private String  Text_of_output="";
   	private String recvMessageClient = "";
	private String recvMessageserver ="";
   	private Context mContext=null;
    private String iPString="";
    private String portString="0";
	private String barString="";
	private String fwq="";
	float  RKnum=0; 
	private String BTNAME="RS-";
	private static final String bm = "GBK";
	//private BluetoothChatService mChatService = null;
	//private BluetoothChatService printService = null;
	private mySocket mysocket=null;
	 Vibrator vibrator=null;
	 
	private final static int SCANNIN_GREQUEST_CODE = 1;
	public static final  int REQUEST_CONNECT_DEVICE = 2;    //宏定义查询设备句柄
	private static final int REQUEST_ENABLE_BT = 3;
	private  BluetoothDevice _device = null;     //蓝牙设备
	//boolean Isbt=false;
	boolean isconnect=false;
	boolean btSent=false;   //是蓝牙
	boolean isSearchinf=false;
	boolean isSearchOK=false;
	//boolean isPrint=false;
	private int Running=21;
	private boolean isSent=false;
	private boolean isTimeout=false;//试用到期  40条
	SimpleDateFormat   sDateFormat;   //时间
	String   date="" ;
	private String androidID="";
	private String sx1="";
	private String sx2="";
	private String sx11="";
	private String sx12="";
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		mContext=this;
		 TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	     androidID=telephonyManager.getDeviceId();
		 String[] str=null;
		/*读取文件获取员工编号和IP，端口号*/
		 setContentView(R.layout.jbzl);
		 btzt=(TextView) findViewById(R.id.zlbt);
			yc=(TextView)findViewById(R.id.zlrk);
			wc=(TextView)findViewById(R.id.zlwc);
			Running=21;
			b1=(Button)findViewById(R.id.B2);
			b2=(Button)findViewById(R.id.B3);
			b3=(Button)findViewById(R.id.B4);
			b4=(Button)findViewById(R.id.B5);
			b5=(Button)findViewById(R.id.B6);
			b6=(Button)findViewById(R.id.B7);
			b7=(Button)findViewById(R.id.B8);
			b8=(Button)findViewById(R.id.B9);
			b9=(Button)findViewById(R.id.B10);
			b10=(Button)findViewById(R.id.B1);
			
			inf1=(EditText)findViewById(R.id.E1);
			inf2=(EditText)findViewById(R.id.E2);
			inf3=(EditText)findViewById(R.id.E3);
			inf4=(EditText)findViewById(R.id.E4);
			inf5=(EditText)findViewById(R.id.E5);
			inf6=(EditText)findViewById(R.id.E6);
			inf7=(EditText)findViewById(R.id.E7);
			inf8=(EditText)findViewById(R.id.E8);
			inf9=(EditText)findViewById(R.id.E9);
			inf10=(EditText)findViewById(R.id.E10);
		    printTextView=(TextView) findViewById(R.id.jbprint);
		    String[] bufx=null;
			priString=WriteReadSD.read("isPrint",log.fdir);	
		   File	file=new File(log.pdir,"商品资料.TXT");
		    if(file.exists()){
		    	String[] bufStrings=null;
		    	Text_of_output=null;
		    	Text_of_output=WriteReadSD.readline("商品资料.TXT",log.pdir);
				if(Text_of_output.trim().length()>0){
					bufStrings=SYS.splitString(Text_of_output.trim(),"\n");
					System.out.println("抬头是:"+bufStrings[0].trim());
					
					bufx=SYS.splitString(bufStrings[0].trim(),"\t");
					for(int i=0;i<11;i++){
						System.out.println(bufx[i].trim());
					}
				  	 b1.setText(bufx[1].trim());
					 b2.setText(bufx[2].trim());
					 b3.setText(bufx[3].trim());
					 b4.setText(bufx[4].trim());
				     b5.setText(bufx[5].trim());
				     b6.setText(bufx[6].trim());
				     b7.setText(bufx[7].trim());
				     b8.setText(bufx[8].trim());
				     b9.setText(bufx[9].trim());
				     b10.setText(bufx[10].trim());
					
				}else{
					 bufx=new String[]{"条码","名称","货号","规格","型号","进价","售价","单位","备注一","备注二","备注三"};
				}		
		    }else{
		        bufx=new String[]{"条码","名称","货号","规格","型号","进价","售价","单位","备注一","备注二","备注三"};
		    }	
		    /*打印属性*/
		    sx1=WriteReadSD.read("Print1",log.fdir);
		    if(sx1.trim().equals("-1")) sx11="";
		    else{
		    	sx11=bufx[Integer.parseInt(sx1)+1];
		    }
		  	sx2=WriteReadSD.read("Print2",log.fdir);
		  	if(sx2.trim().equals("-1")) sx12="";
		    else{
		    	sx12=bufx[Integer.parseInt(sx2)+1];
		    }
		yc.setText(R.string.Transferall);
		wc.setText(R.string.Transferpovit);
		yc.append(MainActivity.sentpovit+""); 
		wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
		BTpower=(TextView)findViewById(R.id.zlDL);
		BTpower.setText( R.string.power); 	
		sjsm=(Button)findViewById(R.id.zlSJ); 
		sendButton=(Button)findViewById(R.id.zlbc); 
		ConnectButton=(Button)findViewById(R.id.zllj);
		printTextView.setOnClickListener(printClickListener);
		vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
		 btzt.setOnClickListener(Btset);
	  	 barcode=(EditText)findViewById(R.id.zl_sendmessage);
	  	Text_of_output="";
		 Text_of_output=WriteReadSD.read("IP", log.fdir);
	  	 if(Text_of_output.length()>0){
	  		str=SYS.splitString(Text_of_output.trim(),":");
	  		iPString=str[0].trim();
	  		portString=str[1].trim();
	  	 }else{
	  		//  sys.disPlay(sView, dis, getString(R.string.Noset)+"\n");   //显示数据 
	  	 }
	  	Text_of_output="";
	  	Text_of_output=WriteReadSD.read("FWQM",log.fdir);
	 	 if(Text_of_output.length()>0){
	 		fwq=Text_of_output.trim();
	 	 }else{
	 		fwq="RS";
	 	}
	 	mysocket=new mySocket(mContext, mHandler,iPString,Integer.parseInt(portString));
	 	if(iPString.length()>0 && mysocket.getState()==mySocket.STATE_NONE){
	  		mysocket.connect();
	  	}else{
	  		ConnectButton.setText(R.string.SKconnected);
	  	}
	  	if(iPString.length()>0){
	  		ConnectButton.setOnClickListener(socketconnect);
	  	}
	
	  	if(!RKScan.Isbt){
	    	btzt.setText(R.string.title_not_connected);
	    }else{
	    	 btzt.setText(R.string.title_connected_to);
	    }
	  	if(!RKScan.isPrint){
	  		printTextView.setText(R.string.printer_not_connected);
	  	}else{
	  		printTextView.setText(R.string.printer_connected_to);
	  	}
	  	if(RKScan.setInf){
		 String	barcodeString=getIntent().getStringExtra("BARCODE");
		  	if(barcodeString.trim().length()>0){
		  		 barcode.setText(barcodeString);
		  		SearchINF(barcodeString.trim());
		  	}
		  }	
	 	mHandler.postDelayed(socketRunnable,50);
	    mHandler.postDelayed(btRunnable,5000);
	    mHandler.postDelayed(printRunnable,5000);
	    mHandler.postDelayed(socketConnect,1000*60);
		btzt.setOnClickListener(Btset);
		Button search=(Button)findViewById(R.id.zlSear);
		search.setOnClickListener(searchon);
		sjsm.setOnClickListener(scanClickListener);
		sendButton.setOnClickListener(sent);
	}
	
private OnClickListener Btset =new OnClickListener() {
		
		@Override
		public void onClick(View v){
			// TODO 自动生成的方法存根
			RKScan.Isbt=false;
			Intent serverIntent = new Intent(JBZL.this, DeviceListActivity.class); //跳转程序设置
			startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);  //设置返回宏定义*/
		}
	};

private OnClickListener sent=new OnClickListener() {
	
	Cursor cursor=null;
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		String jj="0";
		String sj="0";
		if(barcode.getText().toString().length()==0){
		    Toast.makeText(JBZL.this, R.string.BAR, Toast.LENGTH_LONG).show(); 
			return ;
		}
		if(inf5.getText().toString().trim().length()==0){
			jj="0";
		}else{
		  try{
			  jj=Double.parseDouble(inf5.getText().toString().trim())+"";
			}catch(Exception e){
				new AlertDialog.Builder(JBZL.this).setTitle(R.string.SYS)//设置对话框标题  
			     .setPositiveButton(R.string.BXSZ,new DialogInterface.OnClickListener(){//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
			             return ;   
			         }  
			     }).show();  //在按键响应事件中显示此对话框  
				return ;
			} 
		} 
		if(inf6.getText().toString().trim().length()==0){
			sj="0";
		}else{
			  try{
					sj=Double.parseDouble(inf6.getText().toString().trim())+"";
				}catch(Exception e){
					new AlertDialog.Builder(JBZL.this).setTitle(R.string.SYS)//设置对话框标题  
				     .setPositiveButton(R.string.BXSZ,new DialogInterface.OnClickListener(){//添加确定按钮  
				         @Override  
				         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
				             return ;   
				         }  
				     }).show();  //在按键响应事件中显示此对话框  
					return ;
				} 
			 
		} 
		if(log.isRes==false){
			SQLiteDatabase db=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
			 cursor = db.query("ZC", new String[]{"flag"}, null, null, null, null, null);
			 if(cursor.getCount()==0){
				 isTimeout=true;
			 }else{
				 int number=0;
				 cursor.moveToNext();
				 number=Integer.parseInt( cursor.getString(cursor.getColumnIndex("flag")).toString());
				 if(number>=50){
					 isTimeout=true; 
				 }else{
					 isTimeout=false; 
					 number+=1;  
					 SQLiteDatabase zcdw =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
					 ContentValues values=new ContentValues(); 
					 values.put("flag",number+"");
					 String where = "zcm = ? " ;
					 String[] whereValue = {androidID};
					 zcdw.update("ZC", values, where, whereValue);
				 }
				 cursor.close();
				 cursor=null;
			 }
			if(isTimeout){
				 new AlertDialog.Builder(JBZL.this).setTitle(R.string.SYS)//设置对话框标题  
			     .setPositiveButton(R.string.INFre,new DialogInterface.OnClickListener() {//添加确定按钮 
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
			             // TODO Auto-generated method stub  
			        	  return ;
			         }  
			     }).show();
				 return ;
			}
		}
	      SQLiteDatabase db=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
	    try{
	    	 cursor = db.query("JBZL", new String[]{"INF1","INF2","INF3","INF4","INF5","INF6","INF7","INF8","INF9","INF10","INF11"}, "INF1=?", new String[]{barcode.getText().toString().trim()}, null, null, null);
		  if(cursor.getCount()!=0){
				 SQLiteDatabase dw =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
				 ContentValues values=new ContentValues(); 
				 values.put("INF2",inf1.getText().toString().trim());
				 values.put("INF3",inf2.getText().toString().trim());
				 values.put("INF4",inf3.getText().toString().trim());
				 values.put("INF5",inf4.getText().toString().trim());
				 values.put("INF6",jj);
				 values.put("INF7",sj);
				 values.put("INF8",inf7.getText().toString().trim());
				 values.put("INF9",inf8.getText().toString().trim());
				 values.put("INF10",inf9.getText().toString().trim());
				 values.put("INF11",inf10.getText().toString().trim());	 
				 String where = "INF1 = ? " ;
				 String[] whereValue={barcode.getText().toString().trim()};
				 dw.update("JBZL", values, where, whereValue);	 
		  }else{
			  SQLiteDatabase dbw =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
			  ContentValues va=new ContentValues();
			  va.put("INF1",barcode.getText().toString().trim());
			  va.put("INF2",inf1.getText().toString().trim());
			  va.put("INF3",inf2.getText().toString().trim());
			  va.put("INF4",inf3.getText().toString().trim());
			  va.put("INF5",inf4.getText().toString().trim());
			  va.put("INF6",jj);
			  va.put("INF7",sj);
			  va.put("INF8",inf7.getText().toString().trim());
			  va.put("INF9",inf8.getText().toString().trim());
			  va.put("INF10",inf9.getText().toString().trim());
			  va.put("INF11",inf10.getText().toString().trim());
		      dbw.insert("JBZL", null, va);
		  }
	    }catch(Exception e){
	    	cursor.close();
			   e.printStackTrace();
			}finally{
			    if(cursor != null){
			        cursor.close();
			    }
			}	
	    db.close();
	String rkString ="UD|"+barcode.getText().toString().trim()
		    	+","+inf1.getText().toString().trim()
		    	+","+inf2.getText().toString().trim()
		    	+","+inf3.getText().toString().trim()
		    	+","+inf4.getText().toString().trim()
		    	+","+jj.trim()
		    	+","+sj.trim()
		    	+","+inf7.getText().toString().trim()
		    	+","+inf8.getText().toString().trim()
		    	+","+inf9.getText().toString().trim()
		    	+","+inf10.getText().toString().trim();
	  ArrayList<String> dataArrayList=new ArrayList<String>();
	  dataArrayList.add(barcode.getText().toString().trim());
	  dataArrayList.add(inf1.getText().toString().trim());
	  dataArrayList.add(inf2.getText().toString().trim());
	  dataArrayList.add(inf3.getText().toString().trim());
	  dataArrayList.add(inf4.getText().toString().trim());
	  dataArrayList.add(jj.trim());
	  dataArrayList.add(sj.trim());
	  dataArrayList.add(inf7.getText().toString().trim());
	  dataArrayList.add(inf8.getText().toString().trim());
	  dataArrayList.add(inf9.getText().toString().trim());
	  dataArrayList.add(inf10.getText().toString().trim());
	  String string1="";
	  String string2="";
	  if(sx1.trim().equals("-1")) string1="";
	    else{
	    	string1=dataArrayList.get(Integer.parseInt(sx1)+1);
	    }
	  
	  	if(sx2.trim().equals("-1")) string2="";
	    else{
	    	string2=dataArrayList.get(Integer.parseInt(sx2)+1);
	    }
	    printBarcode(barcode.getText().toString().trim(),string1,string2);
	    SQLiteDatabase d = LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
		ContentValues cv= new ContentValues();
		cv.put("BAR", rkString);
		cv.put("ISsent","1");  //表示未传
		d.insert("SENT", null, cv);
		d.close();
		MainActivity.sentall++;
		
		yc.setText(R.string.Transferall);
	 	wc.setText(R.string.Transferpovit);
	 	yc.append(MainActivity.sentpovit+""); 
	 	wc.append((MainActivity.sentall-MainActivity.sentpovit)+"");
	 	if(RKScan.setInf){
			 mHandler.removeCallbacks(btRunnable); 
	    	 mHandler.removeCallbacks(printRunnable);
	    	 if(mysocket!=null) mysocket.stop();
	    	 JBZL.this.finish();
		}	
	}
};
private void String2Dec(String str)				//?????
{
	
	byte[] b=new byte[4];
	int len=str.trim().length();
	b[0]=(byte)29;
	b[1]=(byte)107;
	b[2]=(byte)73;
	b[3]=(byte)len;
	RKScan.printService.write(b);
	sendbtMessage(str);
}
private void TwoCode(String str,int SizeTwo,int Correction)	
{

	int len=str.length();
	int CodeH=len/256,CodeL=len%256;
	byte[] b= new byte[]{0x1D,0x6B,0x61};
	RKScan.printService.write(b);
	b=new byte[4];
	b[0]=(byte)SizeTwo;
	b[1]=(byte)Correction;
	b[2]=(byte)CodeL;
	b[3]=(byte)CodeH;
	RKScan.printService.write(b);
	sendbtMessage(str);
}
private void printBarcode(String barcode,String name,String DJ){
	 String string="";
	if(priString.trim().equals("1")){
		byte[] b={0x1D,0x77,0x02};
		RKScan.printService.write(b);
		 b=new byte[]{0x1D, 0x48,0x32};
		 RKScan.printService.write(b);
		 b=new byte[]{0x1D, 0x68,0x41};
		 RKScan.printService.write(b);
		 String2Dec(barcode);
		 if(sx11.trim().length()==0){
			 string="     ";
		 }else{
			 string=new String().format("%s:%s",sx11,name) ;
		 }
		 sendbtMessage(string+"\r\n");
		 if(sx12.trim().length()==0){
			 string="     ";
		 }else{
			 string=new String().format("%s:%s",sx12,DJ) ;
		 }
		 sendbtMessage(string+"\r\n");
		 
		 sendbtMessage("\n");
		 sendbtMessage("\n");
		 sendbtMessage("\n");
	}else{
		 byte[] b={0x1B,0x33,0x21};
		 RKScan.printService.write(b);
		 TwoCode(barcode,8,2);	
		 sendbtMessage("\n");
		 sendbtMessage("条码:"+barcode+"\n");
		 if(sx11.trim().length()==0){
			 string="     ";
		 }else{
			 string=new String().format("%s:%s",sx11,name) ;
		 }
		 sendbtMessage(string+"\r\n");
		// sendbtMessage("名称:"+name+"\n");
	}
}
/*蓝牙发送函数*/
private void sendbtMessage(String message) {
     if (RKScan.printService.getState() != BluetoothChatService.STATE_CONNECTED) {
          return;
      }
      if (message.length() > 0) {
		try{
			RKScan.printService.write(message.getBytes(bm));
		}catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} 
      }
  }

private OnClickListener searchon =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			btSent=false;
			String bufString="QUERY□"+barcode.getText().toString()+"□"+"DH001"+"□"+"UD"+"□"+"CK001";
			  barString=barcode.getText().toString().trim();
            if(isconnect){
            	isSearchinf=true;
          		mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
            }else{
				SearchINF(barcode.getText().toString());
            }
		}
	};
		
/*服务器连接按钮*/
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

/*条码扫描按键*/
private OnClickListener scanClickListener=new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent1 = new Intent();
		intent1.setClass(JBZL.this, MipcaActivityCapture.class);
		intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent1, SCANNIN_GREQUEST_CODE);
	}
};
/*条码扫描返回的结果处理*/
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    String bufString="";
    switch (requestCode){
	case SCANNIN_GREQUEST_CODE:
		if(resultCode == RESULT_OK){
			Bundle bundle = data.getExtras();
			 barcode.setText(bundle.getString("result"));
			 btSent=false;	
			 bufString="QUERY□"+barcode.getText().toString()+"□"+"DH001"+"□"+"UD"+"□"+"CK001";
			  barString=barcode.getText().toString().trim();
             /*发送查询指令*/
         	  if(isconnect){
         		  isSearchinf=true;
         		mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
	            }else{
					SearchINF(barcode.getText().toString());
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
   }
}
private OnClickListener printClickListener=new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		RKScan.isPrint=false;
		
		Intent serverIntent = new Intent(JBZL.this, DeviceListActivity.class); //跳转程序设置
		startActivityForResult(serverIntent,4);  //设置返回宏定义
	}
};
/*定时检测数据库*/
private Runnable socketRunnable=new Runnable() {
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		 synchronized (this){
		try {
			socketRunnable.wait(10);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		 if(isconnect && MainActivity.sentall>MainActivity.sentpovit && !RKScan.setInf){
				Message msg1 = new Message();
				msg1.what =38;
				mHandler.sendMessage(msg1);
			}
			mHandler.postDelayed(this, 50); 
		 }
	}
};
private Runnable printRunnable=new Runnable() {
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		 if(!RKScan.isPrint && RKScan.printService.getState()!=BluetoothChatService.STATE_CONNECTING){
			 connectprint();
		 }else {

		 }
		 mHandler.postDelayed(this, 5000); 
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
			case 54:
                byte[] writeBuf = (byte[]) msg.obj;
                String writeMessage = new String(writeBuf);
                System.out.println(writeMessage);
                break;	
			 case 17: //获取接收到的数据
		    	 String name = msg.getData().getString("REC");
		    	 String num="";
		    	 int len=0;
		    	 String sqlString=new String().format("talk%s%s%s%s,","□",androidID,"□条码,",barString);
		    	 String sql=new String().format("talk%s%s%s%s,%s","□",androidID,"□",barString,"无-");
		    	 String [] string=null;
		    	 String [] buf=null;
		          if(isSearchinf){
		        	  isSearchinf=false;
		        	  if(SYS.indexofString(name,sqlString)){
		        		  buf=SYS.splitString(name.trim(),"■");
		        		  try {
								len=SYS.crc_r(buf[0].getBytes(bm),buf[0].getBytes(bm).length);
								len=len % 99;
								if(len==Integer.parseInt(buf[1].trim())){
									string=SYS.splitString(buf[0],",");
									
										 b1.setText(string[2]);
									     b2.setText(string[4]);
									     b3.setText(string[6]);
									     b4.setText(string[8]);
									     b5.setText(string[10]);
									     b6.setText(string[12]);
									     b7.setText(string[14]);
									     b8.setText(string[16]);
									     b9.setText(string[18]);
									     b10.setText(string[20]);
									     inf1.setText(string[3]);
									     inf2.setText(string[5]);
									     inf3.setText(string[7]);
									     inf4.setText(string[9]);
									     inf5.setText(string[11]);
									     inf6.setText(string[13]);
									     inf7.setText(string[15]);
									     inf8.setText(string[17]);
									     inf9.setText(string[19]);
									     inf10.setText(string[21]);
									
								   isSearchOK=true;
								}else{ 
									isSearchOK=false;
								}
							} catch (UnsupportedEncodingException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
								isSearchOK=false;
							}
		        	  }else if(SYS.indexofString(name,sql)){
		        		  isSearchOK=false;
						   SearchINF(barcode.getText().toString());
						isSearchOK=false;
		        	  }else{
		        		  isSearchOK=false;
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
	    	     String btname = msg.getData().getString(MainActivity.DEVICE_NAME);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
				  if(SYS.indexofString(btname,BTNAME)||SYS.indexofString(btname,"HC")){
					 btzt.setText(R.string.title_connected_to);
					 RKScan.Isbt=true;
				  }	 
				  else{
					  RKScan.isPrint=true;
						 printTextView.setText(R.string.printer_connected_to);
					 }
					 break;
				case BluetoothChatService.STATE_CONNECTING:
					if(SYS.indexofString(btname,BTNAME)||SYS.indexofString(btname,"HC"))
					 btzt.setText(R.string.title_connecting);
					else{	
						 printTextView.setText(R.string.printer_connecting);
					 }
					 break;
				case BluetoothChatService.STATE_LISTEN:
					if(SYS.indexofString(btname,BTNAME)||SYS.indexofString(btname,"HC")){
						RKScan.Isbt=false;
					  btzt.setText(R.string.title_not_connected);
					} 
					else{
						RKScan.isPrint=false;
						  printTextView.setText(R.string.printer_not_connected);
							// printerTextView.setText(R.string.printer_connecting);
						 }
					 break;
				case BluetoothChatService.STATE_NONE:
					if(SYS.indexofString(btname,BTNAME)||SYS.indexofString(btname,"HC")){
				       btzt.setText(R.string.title_not_connected);
				       RKScan.Isbt=false;
					}
					else{
						RKScan.isPrint=false;
						printTextView.setText(R.string.printer_not_connected);
					}
					 break;
				}
				break;	
		     case 4:
					// 保存该连接装置的名字
					String name1 = msg.getData().getString(MainActivity.DEVICE_NAME);
					System.out.println("蓝牙名称是:"+name1);
					if(SYS.indexofString(name1,BTNAME)||SYS.indexofString(name1,"HC")){
					btzt.setText(R.string.title_connected_to);
					RKScan.Isbt=true;
					}
					else{
						RKScan.isPrint=true;
						 printTextView.setText(R.string.printer_connected_to);
					 }
					break;
				 case 16:
			    	 switch (msg.arg1) {
					case 0:
						ConnectButton.setText(R.string.Noconnected);
						//vibrator.vibrate(800);
						isconnect=false; //网络连接失败
						break;
					case 1:
						ConnectButton.setText(R.string.Noconnected);
						//vibrator.vibrate(800);
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
					case 6:	
						if(isSearchinf){
				        	  isSearchinf=false;
				        	  SearchINF(barcode.getText().toString());
						}
						if(isSent){
							if(Running>21) Running++;
						}
						 break; 
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
						recvMessageClient=recvMessageserver;
						String bar="";
						String bufString="";
						bar=SYS.BTistrue(recvMessageserver);
						recvMessageserver="";
						if(bar.length()>0){
							btSent=true;
							MainActivity.playScanOK(mContext);
		                    recvMessageClient="";
		                    barcode.setText(bar);
		                    barString=bar;         
		                    inf1.setText("");
		    			 	inf2.setText("");
		    			 	inf3.setText("");
		    			 	inf4.setText("");
		    			 	inf5.setText("");
		    			 	inf6.setText("");
		    			 	inf7.setText("");
		    			 	inf8.setText("");
		    			 	inf9.setText("");
		    			 	inf10.setText("");
		    			 	  bufString="QUERY□"+barcode.getText().toString()+"□"+"DH001"+"□"+"UD"+"□"+"CK001";
	                           /*发送查询指令*/
			               	  if(isconnect){
			               		  isSearchinf=true;
			               		mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
		                    }else{
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
								/*dis.setText("");
					   			sView.scrollTo(0,dis.getMeasuredHeight());
								SYS.disPlay(sView, dis,R.string.SB+"\n");*/
								return ;
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
		     case 38:
		    	 Log.i("基本资料进来",Running+"");
		    	SentData="";
		    	 BH="";
				// LoginOpenHelper tb=new  LoginOpenHelper(JBZL.this,"AndroidPosJxc.db", null, 1);
	             SQLiteDatabase db=LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
				 Cursor cursor = null;
			      try{
						cursor =  db.query("SENT", new String[]{"BAR","ISsent","BH"}, "ISsent= ?", new String[]{"1"}, null, null, null);
					    if(cursor != null){
					    	   if( cursor.getCount()!=0 && cursor.moveToFirst()){
					    		   SentData=cursor.getString(cursor.getColumnIndex("BAR"));
					    		   BH=cursor.getString(cursor.getColumnIndex("BH"));
								  if(MainActivity.sentall>MainActivity.sentpovit &&isconnect&& !isSent ){
										  Log.i("发送的数据:", SentData+":"+BH);
										  mysocket.writeData(SYS.SenData(SentData,fwq,androidID,Running));
										  isSent=true; 
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
			   default:
				break;
			}
	 }
};	


/************************************************************************************
 * 
 * 
 * 
 * 
 *                   相关的连接操作
 * 
 * 
 * 
 * ***********************************************************************************/

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
   System.out.println("JBZL onStart");
    if(!MainActivity._bluetooth.isEnabled()){
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    } else {
        if (RKScan.mChatService == null) setupChat();
        else{
        	RKScan.mChatService.setHandler(mHandler);
        }
        if(RKScan.printService==null)  startPrint();
        else{
        	RKScan.printService.setHandler(mHandler);
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
private void startPrint(){
	RKScan.printService=new BluetoothChatService(this, mHandler); //初始化蓝牙打印机
	RKScan.printService.setHandler(mHandler);
	 connectprint();
}
public void onDestroy() {
    super.onDestroy();
    Log.i("JBZL", "onDestroy");
    // 停止蓝牙
    RKScan.setInf=false;
    if(mysocket!=null) mysocket.stop();
   
}
public synchronized void onResume() {
		super.onResume();
		 Log.i("JBZL", "onResume");
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
		if (RKScan.printService != null){
			if (RKScan.printService.getState()==BluetoothChatService.STATE_NONE){
				// 启动蓝牙聊天服务
				RKScan.printService.start(); 
			}
		}
		mHandler.postDelayed(btRunnable,5000);
		mHandler.postDelayed(socketRunnable,50);
		mHandler.postDelayed(socketConnect,1000*60);
		mHandler.postDelayed(printRunnable,5000); 
	}
public void onStop() {
    super.onStop();
    Log.i("JBZL", "onStop");
    mHandler.removeCallbacks(btRunnable); 
    mHandler.removeCallbacks(socketConnect);
    mHandler.removeCallbacks(printRunnable); 
 }
public synchronized void onPause() {
    super.onPause();
    Log.e("JBZL", "- ON PAUSE -");
}

public boolean onKeyDown(int keyCode, KeyEvent event)  {  
	
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
    	mHandler.removeCallbacks(btRunnable); 
    	mHandler.removeCallbacks(socketRunnable);
    	 mHandler.removeCallbacks(socketConnect);
    	 mHandler.removeCallbacks(printRunnable); 
    	JBZL.this.finish();
         // SysApplication.getInstance().exit();
    }else if(keyCode == KeyEvent.KEYCODE_MENU) {
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


/*搜索本地资料并显示出来*/
private  void  SearchINF(String barcodes){
	
/*	LoginOpenHelper sqlHelper = new LoginOpenHelper(
			JBZL.this, "AndroidPosJxc.db", null, 1);*/
	SQLiteDatabase db = LoginOpenHelper.getInstance(mContext,log.DB_NAME).getReadableDatabase();
	Cursor cursor=null;
	try{
	 cursor = db.query("JBZL", new String[] { "INF1",
			"INF2", "INF3", "INF4", "INF5", "INF6", "INF7",
			"INF8", "INF9", "INF10", "INF11" }, "INF1=?",
			new String[] { barcodes.trim() },
			null, null, null);
	String[] arr = new String[10];
	if (cursor.getCount()!= 0)
	{
		cursor.moveToFirst();
		arr[0] = cursor.getString(
				cursor.getColumnIndex("INF2"))
				.toString();
		arr[1] = cursor.getString(
				cursor.getColumnIndex("INF3"))
				.toString();
		arr[2] = cursor.getString(
				cursor.getColumnIndex("INF4"))
				.toString();
		arr[3] = cursor.getString(
				cursor.getColumnIndex("INF5"))
				.toString();
		arr[4] = cursor.getString(
				cursor.getColumnIndex("INF6"))
				.toString();
		arr[5] = cursor.getString(
				cursor.getColumnIndex("INF7"))
				.toString();
		arr[6] = cursor.getString(
				cursor.getColumnIndex("INF8"))
				.toString();
		arr[7] = cursor.getString(
				cursor.getColumnIndex("INF9"))
				.toString();
		arr[8] = cursor.getString(
				cursor.getColumnIndex("INF10"))
				.toString();
		arr[9] = cursor.getString(
				cursor.getColumnIndex("INF11"))
				.toString();
		
		inf1.setText(arr[0]);
	     inf2.setText(arr[1]);
	     inf3.setText(arr[2]);
	     inf4.setText(arr[3]);
	     inf5.setText(arr[4]);
	     inf6.setText(arr[5]);
	     inf7.setText(arr[6]);
	     inf8.setText(arr[7]);
	     inf9.setText(arr[8]);
	     inf10.setText(arr[9]);	
	
	} else{
		//inf1.setText(R.string.NOinft);
		//getString(R.string.NOinft);
	}
  }catch(Exception e){
		}finally{
		    if(cursor != null){
		        cursor.close();
		    }
		}
	db.close();
   } 
  
}

	


