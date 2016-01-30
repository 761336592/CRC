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
	 public static final int STATE_NONE = 0;       //������û������
	    public static final int STATE_LISTEN = 1;  //���ӶϿ���
	    public static final int STATE_FAILE = 7; //����ʧ����
	    public static final int STATE_CONNECTING = 2; // �������ӷ�����
	    public static final int STATE_CONNECTED = 3;  //��������Զ���豸
	    public static final int STATE_sent = 4;    //����
	    public static final int nosent = 5;    
	    public static final int TIMEOUT = 6;    
	  //�ֻ�������ǹ����ɨ���ָ��
	
	 private static final boolean D = true;
	
		public static final String DEVICE_NAME = "device_name";
		public static final String TOAST = "toast";
	boolean Isbt=false;	
		String Text_of_output="";
	String androidID="";   //��ȡ�ֻ������кţ�����ע������������ϴ�
	String Interface="";  //��ȡ����һ������  
	String fwq="";
	byte  zcFlag=0;
	int PJflag=0;
    int transferpovit=0;  //�Ѵ�����
    int transferall=0;    //������    
    BluetoothDevice _device = null;     //�����豸
    BluetoothSocket _socket = null;      //����ͨ��socket 
    boolean _discoveryFinished = false;    
    boolean bRun = true;
    boolean bThread = false;	
    boolean btsent=false;

	/*�ļ���д�ı���*/
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
	
    Button search;              //��ѯ
    
    private Thread mThreadClient = null;
    boolean isConnecting=false;
	SimpleDateFormat   sDateFormat;   //ʱ��
	String   date="" ;
	String pJString="";
	String QString="";
	int  infbunber=0;
    boolean isSend=false;   //
	boolean zc=false;  //�Ƿ�ע����
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
/*���Ӱ�������*/
public void connect() {

			// TODO �Զ����ɵķ������
		
			if(mSocketClient != null){
				try {
					mSocketClient.close();
					mSocketClient = null;
					os=null;
					//mPrintWritersever.close();
					mPrintWritersever = null;
			//		mBufferedReadersever.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		if(mSocketClient==null){
		 
		  setState(STATE_CONNECTING ); //�������ӷ�����
	    	mThreadClient = new Thread(mRunnable); // �߳�����
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
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			//mBufferedReadersever=null;
			mThreadClient.interrupt();
		}
	}
	setState(STATE_LISTEN);   //������ֹͣ����
	
}

public void connetting(){
	try{
	
		mSocketClient = new Socket();  
		
		mSocketClient.connect(new InetSocketAddress(iPString, portString), 10000);
		mSocketClient.setSoTimeout(5000); //�������ݳ�ʱʱ��
		// ���Ƕ���ʵʱ�����Ըߵĳ��򣬽������Ϊ true�����ر� Nagle �㷨���ͻ���ÿ����һ�����ݣ��������ݰ���С���Ὣ��Щ���ݷ��ͳ�ȥ  
		mSocketClient.setTcpNoDelay(true);  
		  // ����������ķ��ͻ�������С��Ĭ����4KB����4096�ֽ�  
		mSocketClient.setSendBufferSize(4096*900);  
		  // �����������Ľ��ջ�������С��Ĭ����4KB����4096�ֽ�  
		mSocketClient.setReceiveBufferSize(4096*900);  
		  // ���ã�ÿ��һ��ʱ����������Ƿ��ڻ״̬������������˳�ʱ��û��Ӧ���Զ��رտͻ���socket  
		  // ��ֹ����������Чʱ���ͻ��˳�ʱ�䴦������״̬  
	  	mSocketClient.setKeepAlive(true);  
		// ȡ�����롢�����

	
		 isConnecting=true;
		setState(STATE_CONNECTED);
		
	}catch (SocketTimeoutException aa) {  
        //������Ϣ �޸�UI�߳��е����  
		System.out.println("���ӷ�������ʱ");
		setState(STATE_FAILE);
		mSocketClient=null;
		return;
    }catch (Exception e){
    	System.out.println("IPy�쳣");
		setState(STATE_FAILE);
		mSocketClient=null;
		return;
	}
	
	
}
/*****************************************************************************
 * 
 * 
 * 
 *                       ��Ϣ���̴߳���
 * 
 * 
 * 
 * 
 ******************************************************************************/

/*�߳�*/
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
					if ((count =mBufferedReadersever.read(buffer)) > 0 )// �������յ�������
					{
						 sb.append(SYS.getInfoBuff(buffer, count)) ;/*SYS.getInfoBuff(buffer, count) ;*/// ��Ϣ����
						  buffer=new char[4096*900];
						 
						   Message msg = mHandler.obtainMessage(17);
					        Bundle bundle = new Bundle();
					        bundle.putString("REC", sb.toString().trim());
					        msg.setData(bundle);
					        mHandler.sendMessage(msg);	
					}else if(count<0){
						    System.out.println("û������");
						    stop();
							return;
					}
				  }else{
					  System.out.println("���ӶϿ�");
					  stop();
						return; 
				  }	
				}catch (IOException e){
					setState(TIMEOUT);
				}
		}	
	} 

};

/*��������*/
public void writeData(String str) {
	  try{
		  if(mSocketClient!=null){
				mPrintWritersever = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(mSocketClient.getOutputStream(),
								bm)));
	         mPrintWritersever.print(str);
	         mPrintWritersever.flush();
	         setState(STATE_sent);
	         System.err.println("���͵��ǣ�"+str);
		  }else
		    System.out.println("SOCKET ����");
	  }catch (Exception e) {
		  System.out.println("����ʧ��:");
		  e.printStackTrace();
	   }
}	
}
