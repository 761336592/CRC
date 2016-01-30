package com.mining.app.socket;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;

import com.example.CRC.SYS;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
public class mySocket {
	File sdCard;
	File dir;
	File file;
	 private final Handler mHandler;
	 private int mState;
	 public static final int STATE_NONE = 0;       //服务器没有连接
	    public static final int STATE_LISTEN = 1;  //连接断开了
	    public static final int STATE_FAILE = 7; //连接失败了
	    public static final int STATE_CONNECTING = 2; // 正在连接服务器
	    public static final int STATE_CONNECTED = 3;  //已连接上远程设备
	    public static final int STATE_sent = 4;    //发送
	    public static final int nosent = 5;    
	    public static final int TIMEOUT = 6;    
	  //手机向蓝牙枪发送扫描的指令
	
	 private static final boolean D = true;
	
		public static final String DEVICE_NAME = "device_name";
		public static final String TOAST = "toast";
	boolean Isbt=false;	
		String Text_of_output="";
	String androidID="";   //获取手机的序列号，用于注册软件或者是上传
	String Interface="";  //获取在哪一个界面  
	String fwq="";
	byte  zcFlag=0;
	int PJflag=0;
    int transferpovit=0;  //已传数量
    int transferall=0;    //总数量    
    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket 
    boolean _discoveryFinished = false;    
    boolean bRun = true;
    boolean bThread = false;	
    boolean btsent=false;

	/*文件读写的变量*/
	public  OutputStream os;
	public static final String bm = "GBK";
	int  CF=0;
 	boolean isEque=false;
	/**/

     private BufferedReader mBufferedReadersever = null;
     private PrintWriter mPrintWritersever = null;
 	private static final String TAG = "socket";
    
   
	private String iPString="";
	 private int portString=0;
	 private Socket mSocketClient=null;   
	int  SocketFlag=0;
	
    Button search;              //查询
    
    private Thread mThreadClient = null;
    boolean isConnecting=false;
	SimpleDateFormat   sDateFormat;   //时间
	String   date="" ;
	String pJString="";
	String QString="";
	int  infbunber=0;
    boolean isSend=false;   //
	boolean zc=false;  //是否注册了
	 private String recvMessageClient = "";
public mySocket(Context mContext,Handler handler,String IP, int Port){
	mHandler = handler;
	mState = STATE_NONE;
	iPString=IP;		  
	portString=Port;
	
}
private synchronized void setState(int state) {
  
    mState = state;
    // Give the new state to the Handler so the UI Activity can update
    mHandler.obtainMessage(16, state, -1).sendToTarget();
}

/**
 * Return the current connection state. 
 */
public synchronized int getState() {
    return mState;
}
/*连接按键监听*/
public void connect() {

			// TODO 自动生成的方法存根
		
			if(mSocketClient != null){
				try {
					mSocketClient.close();
					mSocketClient = null;
					os=null;
					//mPrintWritersever.close();
					mPrintWritersever = null;
			//		mBufferedReadersever.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		if(mSocketClient==null){
		 
		  setState(STATE_CONNECTING ); //正在连接服务器
	    	mThreadClient = new Thread(mRunnable); // 线程启动
	     	mThreadClient.start();
		}	
		
}
public void stop(){
	if(isConnecting || getState()==STATE_CONNECTED||getState()==STATE_LISTEN){
		isConnecting =false;
		if(mSocketClient != null){
			try {
				mSocketClient.close();
			  
				  mSocketClient = null;
				mPrintWritersever = null;
				os = null;
			//	mBufferedReadersever.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			//mBufferedReadersever=null;
			mThreadClient.interrupt();
		}
	}
	setState(STATE_LISTEN);   //服务器停止连接
	
}

public void connetting(){
	try{
	
		mSocketClient = new Socket();  
		
		mSocketClient.connect(new InetSocketAddress(iPString, portString), 10000);
		mSocketClient.setSoTimeout(5000); //接收数据超时时间
		// 但是对于实时交互性高的程序，建议其改为 true，即关闭 Nagle 算法，客户端每发送一次数据，无论数据包大小都会将这些数据发送出去  
		mSocketClient.setTcpNoDelay(true);  
		  // 设置输出流的发送缓冲区大小，默认是4KB，即4096字节  
		mSocketClient.setSendBufferSize(4096*900);  
		  // 设置输入流的接收缓冲区大小，默认是4KB，即4096字节  
		mSocketClient.setReceiveBufferSize(4096*900);  
		  // 作用：每隔一段时间检查服务器是否处于活动状态，如果服务器端长时间没响应，自动关闭客户端socket  
		  // 防止服务器端无效时，客户端长时间处于连接状态  
	  	mSocketClient.setKeepAlive(true);  
		// 取得输入、输出流

	
		 isConnecting=true;
		setState(STATE_CONNECTED);
		
	}catch (SocketTimeoutException aa) {  
        //发送消息 修改UI线程中的组件  
		System.out.println("连接服务器超时");
		setState(STATE_FAILE);
		mSocketClient=null;
		return;
    }catch (Exception e){
    	System.out.println("IPy异常");
		setState(STATE_FAILE);
		mSocketClient=null;
		return;
	}
	
	
}
/*****************************************************************************
 * 
 * 
 * 
 *                       消息和线程处理
 * 
 * 
 * 
 * 
 ******************************************************************************/

/*线程*/
private Runnable mRunnable = new Runnable() {
	
	
	public void run() {		
		 connetting();
		while (isConnecting){
			
			int count =0;
			char[] buffer=new char[4096*900];
				try {
					if(mSocketClient!=null){
						mBufferedReadersever = new BufferedReader(
								new InputStreamReader(mSocketClient.getInputStream(),bm));
				
						StringBuilder sb=new StringBuilder("");
					if ((count =mBufferedReadersever.read(buffer)) > 0 )// 读出接收到的数据
					{
						 sb.append(SYS.getInfoBuff(buffer, count)) ;/*SYS.getInfoBuff(buffer, count) ;*/// 消息换行
						  buffer=new char[4096*900];
						 
						   Message msg = mHandler.obtainMessage(17);
					        Bundle bundle = new Bundle();
					        bundle.putString("REC", sb.toString().trim());
					        msg.setData(bundle);
					        mHandler.sendMessage(msg);	
					}else if(count<0){
						    System.out.println("没有数据");
						    stop();
							return;
					}
				  }else{
					  System.out.println("连接断开");
					  stop();
						return; 
				  }	
				}catch (IOException e){
					setState(TIMEOUT);
				}
		}	
	} 

};

/*发送数据*/
public void writeData(String str) {
	  try{
		  if(mSocketClient!=null){
				mPrintWritersever = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(mSocketClient.getOutputStream(),
								bm)));
	         mPrintWritersever.print(str);
	         mPrintWritersever.flush();
	         setState(STATE_sent);
	         System.err.println("发送的是："+str);
		  }else
		    System.out.println("SOCKET 发送");
	  }catch (Exception e) {
		  System.out.println("发送失败:");
		  e.printStackTrace();
	   }
}	
}
