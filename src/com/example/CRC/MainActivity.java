package com.example.CRC;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.Menu;

public class MainActivity extends Activity {
	File sdCard;
	File dir;
	File file;
	MediaPlayer mp =new MediaPlayer();
	public static final  int REQUEST_CONNECT_DEVICE = 2;    //�궨���ѯ�豸���
	private static final int REQUEST_ENABLE_BT = 3;
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 13;
	public static final int MESSAGE_WRITE =54;
	public static final int MESSAGE_DEVICE_NAME =4;
	public static final int MESSAGE_TOAST = 5;
	public static final int SCAN = 55;    //�ֻ�������ǹ����ɨ���ָ��
	
	 private static final boolean D = true;
	
	 public  static final String DEVICE_NAME = "device_name";
	 public  static final String TOAST = "toast";
	boolean Isbt=false;	
	private MyGridView gridview;
	private ViewPager mTabPager;	
	private ImageView mTabImg;// ����ͼƬ
	private ImageView mTab1,mTab2,mTab3,mTab4;
	 int zero = 0;// ����ͼƬƫ����
	 int currIndex = 0;// ��ǰҳ�����
	 int one;//����ˮƽ����λ��
	 int two;
	 int three;
	 boolean menu_display = false;
	 private static Vibrator vibrator=null;	//�ֻ���
	private Context mContext;
	String Text_of_output="";
	//static String androidID="";   //��ȡ�ֻ������кţ�����ע������������ϴ�
	String Interface="";  //��ȡ����һ������  
	
	byte  zcFlag=0;
	int PJflag=0;
    int transferpovit=0;  //�Ѵ�����
    int transferall=0;    //������    
    public  BluetoothDevice _device = null;     //�����豸
    public BluetoothSocket _socket = null;      //����ͨ��socket 
    boolean _discoveryFinished = false;    
    boolean bRun = true;
    boolean bThread = false;	
    boolean btsent=false;
    
	/*�ļ���д�ı���*/
	public  OutputStream os;
	public static final String bm = "GBK";
	public InputStream is; 	
	int  CF=0;
 	boolean isEque=false;
	/**/
    static BufferedReader mBufferedReadersever = null;
 	static PrintWriter mPrintWritersever = null;
 	private static final String TAG = "jxc";
   
	int  SocketFlag=0;

	SimpleDateFormat   sDateFormat;   //ʱ��
	String   date="" ;
	String pJString="";
	String QString="";
	int  infbunber=0;
    boolean isSend=false;   //
	boolean zc=false;  //�Ƿ�ע����
	public static int sentall=0;
    public static   int sentpovit=0;
	String inf []=null;  //�����Ʒ���ϵ�
	private ProgressDialog proDia;
	public static MainActivity instance = null;
	public static BluetoothAdapter _bluetooth=null;
	public static String BTPOWER="100";
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		instance=this;
		Interface="MA";
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.main);
		//setContentView(R.layout.natch_file);
		//ע��ı�־λ	
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);	//���ܺ���
	
   	     mContext = this;
   	     Interface="";
   	
   	SQLiteDatabase db =LoginOpenHelper.getInstance(this,log.DB_NAME).getReadableDatabase();
   	Cursor cursor=db.query("SENT",null, null, null, null,null, null);
   	sentall=cursor.getCount();
	Cursor c=db.query("SENT",new String[]{"ISsent"},"ISsent=?", new String[]{"0"}, null,null, null);
	sentpovit=c.getCount();
	c.close();
	cursor.close();
   	vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE); 
	initView();
	
	
//	initBeepSound();
	/*�����Ļ��״̬������������Ļ��͸�����ǹ�������ߵ�ָ��*/
	
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

/****************************************************************************
 * 
 * 
 * 
 * 
 *            ���������
 * 
 * 
 * 
 * 
 *****************************************************************************/

private void initView() {
	gridview=(MyGridView) findViewById(R.id.gridview);
	gridview.setAdapter(new MyGridAdapter(this));
	gridview.setOnItemClickListener(onItemClickListener);

	/* Display currDisplay = getWindowManager().getDefaultDisplay();//��ȡ��Ļ��ǰ�ֱ���
     int displayWidth = currDisplay.getWidth();
     int displayHeight = currDisplay.getHeight();
     one = displayWidth/4; //����ˮƽ����ƽ�ƴ�С
     two = one*2;
     three = one*3;

    
	mTabPager =(ViewPager) findViewById(R.id.tabpager);
	mTab1=(ImageView) findViewById(R.id.img_weixin);
	mTab2=(ImageView) findViewById(R.id.img_address);
	mTab3=(ImageView) findViewById(R.id.img_friends);
	mTab4=(ImageView) findViewById(R.id.img_settings);
	
	
	
	LayoutInflater layoutInflater=LayoutInflater.from(mContext);
	View view1=layoutInflater.inflate(R.layout.main_tab_address, null);
	View view2=layoutInflater.inflate(R.layout.scan_setting, null);
	View view3=layoutInflater.inflate(R.layout.setting, null);
	View view4=layoutInflater.inflate(R.layout.zhgl,null);

	
	final ArrayList<View > list=new ArrayList<View>();
	list.add(view1);
	list.add(view2);
	list.add(view3);
	list.add(view4);
	
	PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(list.get(position));
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(list.get(position));
				return list.get(position);
			}
		};
		mTabPager.setAdapter(mPagerAdapter);
		
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener() );
		
		 mTab1.setOnClickListener(new MyOnClickListener(0));
	     mTab2.setOnClickListener(new MyOnClickListener(1));
	     mTab3.setOnClickListener(new MyOnClickListener(2));
	     mTab4.setOnClickListener(new MyOnClickListener(3));
	 */    
}

    /**
	 * ͷ��������
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};
  
	 /* ҳ���л�����(ԭ����:D.Winter)*/
/*	 
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				} else if (currIndex == 2){
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
			
				break;
			case 1:
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 2){
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
			
				break;
			case 2:
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
			 
				break;
			case 3:
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
				if (currIndex == 0){
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1){
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				}
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	*/
	
public OnItemClickListener onItemClickListener=new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO �Զ����ɵķ������
	    switch (arg2) {
		case 0:  //�������ɨ��
			Intent intentrk = new Intent(); 
			intentrk.setClass(MainActivity.this,CKScan.class);
			startActivity(intentrk); 
			break;
		case 1:
			Intent intentCk = new Intent(); 
			intentCk.setClass(MainActivity.this,ScanSetting.class);
			startActivity(intentCk);  
			break;
		case 2:
			Intent intentPk = new Intent(); 
			intentPk.setClass(MainActivity.this,zhgl.class);
			startActivity(intentPk);  
			break;
		case 3:
			Intent DB = new Intent(); 
			DB.setClass(MainActivity.this,Setting.class);
			startActivity(DB);  
			break;
			
	/*	case 4:
			Intent intentzl = new Intent(); 
			intentzl.setClass(MainActivity.this,JBZL.class);
			startActivity(intentzl);
			break;
		case 5:
			Intent intent3 = new Intent(); 
			intent3.setClass(MainActivity.this,KCCX.class);
			startActivity(intent3); 
			break;
		case 6:
			Intent intent = new Intent(); 
			intent.setClass(MainActivity.this,LookOver.class);
			startActivity(intent); 
			break;
		case 7:
			Intent llintent = new Intent(); 
			llintent.setClass(MainActivity.this,BrowseCK.class);
			startActivity(llintent); 
			break;	
		case 8:
			Intent intentrt = new Intent(); 
			intentrt.setClass(MainActivity.this,ScanSetting.class);
			startActivity(intentrt); 
			break;
		case 9:
			Intent intent1 = new Intent(); 
			intent1.setClass(MainActivity.this,zhgl.class);
			startActivity(intent1);  
			break;
		case 10:
			Intent intent2 = new Intent(); 
			intent2.setClass(MainActivity.this,Setting.class);
			startActivity(intent2);
			break;
		case 11:
			Intent intentprint = new Intent(); 
			intentprint.setClass(MainActivity.this,PrintSet.class);
			startActivity(intentprint); 
			break;
		case 12:
		
			int k=zldr();
			if(k==0){
				new AlertDialog.Builder(MainActivity.this).setTitle(R.string.SYS)//���öԻ������  
			     .setPositiveButton(R.string.setinf,new DialogInterface.OnClickListener() {//���ȷ����ť  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//ȷ����ť����Ӧ�¼�  
			             return ;   
			         }  
			     }).show();//�ڰ�����Ӧ�¼�����ʾ�˶Ի���  
				return ;
			}
				proDia=new ProgressDialog(MainActivity.this);
				proDia.setTitle(getString(R.string.ZZDR));
				proDia.setMessage(getString(R.string.DDYX));
				proDia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL );
				proDia.show();
				proDia.incrementProgressBy(-proDia.getProgress()); //����������Ϊ0
				proDia.setMax(inf.length-1);
				  new Thread() {  
	                  public void run() { 
	                	  String sql = "insert into JBZL(INF1,INF2,INF3,INF4,INF5,INF6,INF7,INF8,INF9,INF10,INF11) values(?,?,?,?,?,?,?,?,?,?,?)";
	          			SQLiteDatabase dbwW =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
	          			SQLiteStatement stat=dbwW.compileStatement(sql);
	          			dbwW.beginTransaction();
	                	  List<String> dataStrings=new ArrayList<String>();
	                    for(int i=1;i<inf.length;i++) {  
	                    	 dataStrings=SYS.NewsplitString1(inf[i]+"\t","\t");
	                    		if(dataStrings.size()<11){
	                        		for(int j=0;j<12;j++){
	                        			dataStrings.add("null");
	                        		}
	                        	}
	          					SQLiteDatabase dbw = LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
	          					String where = "INF1= ?";
	          					String[] whereValue = {dataStrings.get(0).trim()};
	          					dbw .delete("JBZL", where,whereValue);
	          					String jj="0";
	          					String sj="0";
	          					try{
	          						jj=Double.parseDouble(dataStrings.get(5).trim())+"";	
	          					}catch(Exception e){
	          						jj="0";
	          					}
	          					try{
	          						sj=Double.parseDouble(dataStrings.get(6).trim())+"";	
	          					}catch(Exception e){
	          						sj="0";
	          				}
	          				stat.bindString(1,dataStrings.get(0).trim()) ;
	          				stat.bindString(2,dataStrings.get(1).trim()) ;
	          				stat.bindString(3,dataStrings.get(2).trim()) ;
	          				stat.bindString(4,dataStrings.get(3).trim()) ;
	          				stat.bindString(5,dataStrings.get(4).trim()) ;
	          				stat.bindString(6,jj) ;
	          				stat.bindString(7,sj) ;
	          				stat.bindString(8,dataStrings.get(7).trim()) ;
	          				stat.bindString(9,dataStrings.get(8).trim()) ;
	          				stat.bindString(10,dataStrings.get(9).trim());
	          				stat.bindString(11,dataStrings.get(10).trim());
	          				stat.executeInsert();
	                        mHandler.sendEmptyMessage(0);  
	                    }  
	                    dbwW.setTransactionSuccessful();
	          			dbwW.endTransaction();
	          			dbwW.close();
	          			proDia.dismiss();
	          			inf=null;
	                  }  
	              }.start();	          
			 break;*/
		case 4:
			 new AlertDialog.Builder(MainActivity.this).setTitle(R.string.SYS)//���öԻ������  
		     .setMessage(R.string.IsClear)//������ʾ������  
		     .setPositiveButton(R.string.S,new DialogInterface.OnClickListener() {//���ȷ����ť 
		         @Override  
		         public void onClick(DialogInterface dialog, int which) {//ȷ����ť����Ӧ�¼�  
		             // TODO Auto-generated method stub  
		        	 clearData(); 
		         }  
		     }).setNegativeButton(R.string.N,new DialogInterface.OnClickListener() {//��ӷ��ذ�ť  
		         @Override  
		            public void onClick(DialogInterface dialog, int which) {//��Ӧ�¼�  
		         }  
		     }).show();//�ڰ�����Ӧ�¼�����ʾ�˶Ի���  
			break;
		case 5:
			Intent help = new Intent(); 
			help.setClass(MainActivity.this,Help.class);
			startActivity(help); 
			break;
		case 6:
			Intent intent4 = new Intent(); 
			intent4.setClass(MainActivity.this,Aboutjxc.class);
			startActivity(intent4); 
			break;
		case 7:
			 new AlertDialog.Builder(MainActivity.this).setTitle(R.string.SYS)//���öԻ������  
		     .setMessage(R.string.exit)//������ʾ������  
		     .setPositiveButton(R.string.S,new DialogInterface.OnClickListener() {//���ȷ����ť 
		         @Override  
		         public void onClick(DialogInterface dialog, int which) {//ȷ����ť����Ӧ�¼�  
		             // TODO Auto-generated method stub  
		        	 if (RKScan.mChatService != null) RKScan.mChatService.stop(); 
		        	 SysApplication.getInstance().exit();
		         }  
		      }).setNegativeButton(R.string.N,new DialogInterface.OnClickListener() {//��ӷ��ذ�ť  
		             @Override  
		             public void onClick(DialogInterface dialog, int which){//��Ӧ�¼�  
		         }  
		     }).show();//�ڰ�����Ӧ�¼�����ʾ�˶Ի���  
			break;
		    default:
			break;
		}
	}
};

/*���ϵ���*/
private int zldr() {
	//
	int num=0;
		file=new File(log.pdir,"��Ʒ����.TXT");
	    if(!file.exists()){
	    	num=0;
	    }else{
	    	Text_of_output=WriteReadSD.read("��Ʒ����.TXT",log.pdir);
	 //Text_of_output=WriteReadSD.readline("INF.TXT",log.pdir,0,150);
		    if(Text_of_output.trim().length()==0){
			   num=0;
		    }else{
			   num=1;
			   inf=new String[]{};
			   inf=SYS.splitString(Text_of_output,"\n");
			 
		     }
	    }	
	return num;
}
/*�������*/
private void clearData() {
	
		//file.delete();
	//	LoginOpenHelper helper=new LoginOpenHelper(mContext,log.DB_NAME, null, 1);
		SQLiteDatabase db=LoginOpenHelper.getInstance(this,log.DB_NAME).getReadableDatabase();
		db.delete("SENT",null,null);	
		db.delete("YD",null,null);
		db.delete("DHCX",null,null);
	    db.delete("sqlite_sequence",null,null);
		sentall=0;
		sentpovit=0;
		Toast.makeText(mContext,R.string.Delet,Toast.LENGTH_SHORT).show();
	
}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
	
	
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

	Handler mHandler = new Handler() {
		// ʵ����Handler��������Ҫ����handleMessage�����������յ�����Ϣ
		public void handleMessage(Message msg) {
		
			super.handleMessage(msg);
		    //if(D) Log.i(TAG, "������: " + msg.arg1);
			switch (msg.what) {
			case 0:
				 proDia.incrementProgressBy(1);  
               
                 break;
			
			default:
				break;
			}
		}

	};
	
/*******************************************************************************
 * 
 * 
 * 
 * 
 *                              ������ص���غ���
 *        
 * 
 * 	
 * @param name
 * @param string
 ********************************************************************************/
  
	 @Override
   public void onStart() {
	        super.onStart();
	        if(D) Log.e(TAG, "++ ON START ++");
	        _bluetooth = BluetoothAdapter.getDefaultAdapter();
	        // �ж������Ƿ��.
	        // setupChat��onActivityResult()��������
	        if (!_bluetooth.isEnabled()) {
	            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	        } 

	    }
	 public synchronized void onPause() {
	        super.onPause();
	        if(D) Log.e(TAG, "- ON PAUSE -");
	    }

	    @Override
	    public void onStop() {
	        super.onStop();
	        if(D) Log.e(TAG, "-- ON STOP --");
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        // ֹͣ����
	      //  if (mChatService != null) mChatService.stop();
	        if(D) Log.e(TAG, "--- ON DESTROY ---");
	    }
	 @Override
	public synchronized void onResume() {
		super.onResume();
		 Log.d(TAG, "onResume");
		
	}


public boolean onKeyDown(int keyCode, KeyEvent event)  {  
		
        if (keyCode == 4 ) {   
             System.out.println("------");
             new AlertDialog.Builder(MainActivity.this).setTitle(R.string.SYS)//���öԻ������  
		     .setMessage(R.string.exit)//������ʾ������  
		     .setPositiveButton(R.string.S,new DialogInterface.OnClickListener() {//���ȷ����ť 
		         @Override  
		         public void onClick(DialogInterface dialog, int which) {//ȷ����ť����Ӧ�¼�  
		             // TODO Auto-generated method stub  
		        	  if (RKScan.mChatService != null) RKScan.mChatService.stop();
		        	  
		        	   if(RKScan.printService!=null) RKScan.printService.stop();
		        	 SysApplication.getInstance().exit();
		         }  
		     }).setNegativeButton(R.string.N,new DialogInterface.OnClickListener() {//��ӷ��ذ�ť  
		         @Override  
		         public void onClick(DialogInterface dialog, int which) {//��Ӧ�¼�  
		         }  
		     }).show();//�ڰ�����Ӧ�¼�����ʾ�˶Ի���  
            // SysApplication.getInstance().exit();
            	 return true;
        	}
        return false; 
    } 
	

	private static MediaPlayer mediaPlayer=null;
	//private static boolean playBeep=true;
	//private static final float BEEP_VOLUME = 0.50f;
	private static final long VIBRATE_DURATION = 100L;

	void initBeepSound() {
		/*if (playBeep && mediaPlayer == null) {
			
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}*/
	}
	public static void playBeepSoundAndVibrate(long inte,Context c) {
		
		playerStop();
		mediaPlayer=MediaPlayer.create(c,R.raw.beep);
		mediaPlayer.setOnCompletionListener(beepListener);
		mediaPlayer.start();
		vibrator.vibrate(inte);
		
	}
	private final static OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			playerStop();
		}
	};
	public static void playScanOK(Context c){
		
		playBeepSoundAndVibrate(VIBRATE_DURATION,c);
	}
   public static void playScanOK(){
		
	//	playBeepSoundAndVibrate(VIBRATE_DURATION,c);
	}
   public static void playerStop(){
		if(mediaPlayer!=null){
			mediaPlayer.release();
			mediaPlayer=null;
		}
	}
}
