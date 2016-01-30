package com.example.CRC;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setting extends Activity{
	public  OutputStream os;
	public static final String bm = "GBK";
	public InputStream is; 	
	int  CF=0;
 	boolean isEque=false;
	 private	EditText IP;
	 private	EditText porText;
	 private    EditText FWQ;
	 private	Button blackButton;
	 static BufferedReader mBufferedReaderClient = null;
	 static PrintWriter mPrintWriterClient = null;
	 boolean isTbConnecting = false;
	 private Context mContext;
	 String Text_of_output="";
	 SYS sys=new SYS();
	 Vibrator vibrator;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//解决了软件软键盘的问题
		setContentView(R.layout.setting);
		 
	 getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	
         mContext=this;
		 String[] str=null;
		  IP=(EditText)findViewById(R.id.IP);
	  	 porText=(EditText)findViewById(R.id.PORT);
	  
	     blackButton=(Button)findViewById(R.id.IP_btn);
	  	 FWQ=(EditText)findViewById(R.id.fwq);
	  	Button sImageView=(Button) (findViewById(R.id.SA));
	  //	RelativeLayout  tb=(RelativeLayout )findViewById(R.id.i);
	  	
	    vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
	  
	     Text_of_output="";
	  	Text_of_output=WriteReadSD.read("IP",log.fdir);
	   	
			 if(Text_of_output.trim().length()>0){
		  		str=SYS.splitString(Text_of_output.trim(),":") ;
		  		IP.setText(str[0].trim());
		  		porText.setText(str[1].trim());
		  	 }
		  	Text_of_output=""; 
		  	Text_of_output=WriteReadSD.read("FWQM",log.fdir); 
		  	if(Text_of_output.trim().length()>0){
	  			FWQ.setText(Text_of_output.trim());
	  		 }else{
	  			FWQ.setText("RS");
	  		 }
		  	 Text_of_output=""; 
		  
		  	 sImageView.setOnClickListener(saveClickListener);
		  	 isTbConnecting = true;
		  	blackButton.setOnClickListener(blackClickListener);
		
	}
	private OnClickListener saveClickListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String string="";
			// TODO 自动生成的方法存根
			//sys.My_WriteFile("YGBH", yGBHText.getText().toString().trim(),mContext,os);
			if(IP.length()>0 && porText.length()>0){
				string=IP.getText().toString()+":"+porText.getText().toString();
				WriteReadSD.writex(string, "IP", log.fdir);
				//sys.My_WriteFile("IP", string,mContext,os);
			}
			WriteReadSD.writex(FWQ.getText().toString().trim(), "FWQM", log.fdir);
		//	sys.My_WriteFile("FWQM", FWQ.getText().toString().trim(),mContext,os);
			Toast.makeText(mContext,  "保存成功", Toast.LENGTH_SHORT).show();
		
		}
	};	
    private OnClickListener blackClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			  Setting.this.finish();
			  //System.exit(1);
		}
	};
	/*ip同步按钮*/
	/*private OnClickListener tbClickListener =new OnClickListener() {
		
		int count=0;
		char[] buffer=new char[256];
		@Override
		public void onClick(View v) {
			isTbConnecting=true;
			// TODO 自动生成的方法存根
			try {
				mSocketServer = new Socket();  
				
				// mSocketClient.setSoTimeout(5000);
				mSocketServer.connect(new InetSocketAddress("210.209.84.163",50000), 50000);
				//mSocketServer = new Socket("210.209.84.163",50000);
				
				mBufferedReaderClient = new BufferedReader(
						new InputStreamReader(mSocketServer.getInputStream(),bm));

				mPrintWriterClient = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(mSocketServer.getOutputStream(),
								bm)));
			
				recvMessageserver = "已经连接server!\n";// 消息换行
	          Toast.makeText(mContext, recvMessageserver, Toast.LENGTH_SHORT).show();
	           SentIp();
	          
	           
			}catch(UnknownHostException e){
				// TODO 自动生成的 catch 块
				Toast.makeText(mContext, "链接失败", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				mSocketServer=null;
				vibrator.vibrate(600);
			}catch (IOException e) {
				// TODO 自动生成的 catch 块
				Toast.makeText(mContext, "链接失败", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				mSocketServer=null;
				vibrator.vibrate(600);
			}
			try{
				mSocketServer.setSoTimeout(5000);
				if ((count = mBufferedReaderClient.read(buffer)) > 0)// 读出接收到的数据
					{
					  recvMessageserver=sys.getInfoBuff(buffer, count) ;// 消息换行
						Message msg = new Message();
						msg.what = 1;
						
						mHandler.sendMessage(msg);
					}
			}catch (Exception e) {
				recvMessageserver = "接收异常:" + e.getMessage() + "\n"; // 消息换行
				mSocketServer=null;
				Toast.makeText(mContext,  recvMessageserver, Toast.LENGTH_SHORT).show();
				vibrator.vibrate(600);
			}
		}
	};

	/*发送IP同步数据*/
	/*private void SentIp(){
		String  meString="";
		if(isTbConnecting &&  mSocketServer!= null){
			try {

				 meString="talkandroid" + "□" + "search" + "□" +iptb.getText().toString()
							+ "□" +tbmm.getText().toString() +"\n"+"□";
				try {
				int len = sys.crc_r(meString.getBytes(bm),meString.getBytes(bm).length);
				     len = len % 99;
				     String s = "";
				  // s=len+"";
				 s=String.format("%02d",len);
				     meString=meString+ "■" + s + "■" + "\n";
				
				     mPrintWriterClient.print(meString);// 发送给服务器
				     mPrintWriterClient.flush();
				  //   Toast.makeText(mContext,meString,Toast.LENGTH_SHORT).show();
				     Toast.makeText(mContext, "正在同步请稍等",Toast.LENGTH_SHORT).show();
				    // tbView.setText("正在同步请稍等..........."+"\n");
				} catch (Exception e) {
					Toast.makeText(mContext, "发送异常：" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					// TODO: handle exception
				}

			} catch (Exception e) {
				
			}
		}else {
			Toast.makeText(mContext, "没有连接", Toast.LENGTH_SHORT).show();
		}
	}

	/*处理接收到的同步数据*/
	/*private void reciveTb(String str){
		 int len=0;
		 /*将数据拆分*/
	/*	 String string="";
		 String[] str1=null;
		 String[] bufx=null;
		 str1=sys.splitString(str, "■");
		 try {
			len=sys.crc_r(str1[0].getBytes(bm),str1[0].getBytes(bm).length);
			len=len%99;
			if(len==Integer.parseInt(str1[1].trim())){
				 bufx=sys.splitString(str1[0].trim(),"□");
				 if(bufx[0].equals("未注册")||bufx[0].equals("密码错误") ){
					//tbView.setText("同步成功");
					 Toast.makeText(mContext,"同步失败"+bufx[0], Toast.LENGTH_SHORT).show();
				 }else{
					 vibrator.vibrate(500);
					Toast.makeText(mContext,"同步成功", Toast.LENGTH_SHORT).show();
					 String WRITE=bufx[0].trim()+":"+porText.getText().toString();
					sys.My_WriteFile("IP", WRITE,mContext,os);
					 string=iptb.getText().toString()+":"+tbmm.getText().toString();
					 sys.My_WriteFile("IPTB", string,mContext,os);
					 IP.setText(bufx[0].trim());
					// IP.setEnabled(false);
					 isTbConnecting=false;
					 mPrintWriterClient.close();
					 mBufferedReaderClient.close();
					 mSocketServer.close();
				 }
			}
			
		}catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e){
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}*/
	/*Handler mHandler = new Handler() {
		int len;
     
		// 实例化Handler，这里需要覆盖handleMessage方法，处理收到的消息
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
			//	reciveTb(recvMessageserver);   //接受IP同步数据
				 break;
			 
			 }
		}
	};*/
	public boolean onKeyDown(int keyCode, KeyEvent event)  {  
		
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
        	 Setting.this.finish();
             // SysApplication.getInstance().exit();
        }else if(keyCode == KeyEvent.KEYCODE_MENU) {
           return false;
        }
        return true; 
    } 
	

}
