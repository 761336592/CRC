package com.example.Player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;

public class MyPlayer {
	private  Context context;
	private  MediaPlayer mediaPlayer=null;
	private  final long VIBRATE_DURATION = 100L;
	private  Vibrator vibrator=null;	
	/*
	 * 
	 *   构造函数
	 *   con:  上下文
 	 * 
	 *   v:    系统的震动对象
	 * 
	 * */
	 public MyPlayer(Context con, Vibrator v){
		    context=con;	
		    vibrator=v;
	 }

	private  void playBeepSoundAndVibrate(long inte,int raw) {
		  
			playerStop();
			mediaPlayer=MediaPlayer.create(context,raw);
			mediaPlayer.setOnCompletionListener(beepListener);
			mediaPlayer.start();
			vibrator.vibrate(inte);
		}
/*停止播放*/	
	 private  void playerStop(){
			if(mediaPlayer!=null){
				mediaPlayer.release();
				mediaPlayer=null;
			}
	}
	 /*
	  * 
	  *  播放声音
	  *  raw : 在 raw文件夹中的声音资源
	  * 
	  */
	 public void playScanOK(int raw){
			playBeepSoundAndVibrate(VIBRATE_DURATION,raw);
	 } 
	 
	 
	private  OnCompletionListener beepListener = new OnCompletionListener() {
			public void onCompletion(MediaPlayer mediaPlayer) {
				playerStop();
			}
		};
		
}
