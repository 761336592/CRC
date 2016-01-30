package com.example.CRC;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class Help extends Activity{
	private int face=0;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sysm);
		RelativeLayout bt=(RelativeLayout) findViewById(R.id.help_bt);
     	bt.setOnClickListener(btClickListener);
     	face=0;
     	RelativeLayout inf=(RelativeLayout) findViewById(R.id.help_inf);
     	inf.setOnClickListener(infClickListener);
     	
    	RelativeLayout net=(RelativeLayout) findViewById(R.id.help_net);
     	net.setOnClickListener(netClickListener);
	}
   private OnClickListener btClickListener=new OnClickListener() {
	
	   @Override
	   public void onClick(View v) {
		// TODO 自动生成的方法存根
		   face=1;
		   setContentView(R.layout.connectbt);
	   }
   };
   private OnClickListener infClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			face=2;
			setContentView(R.layout.inputinf);
		}
	};
	 private OnClickListener netClickListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				face=3;
				setContentView(R.layout.net);
			}
		};
public boolean onKeyDown(int keyCode, KeyEvent event)  {  
		
        if (keyCode == 4 ) {   
               if(face==1||face==2||face==3){
            	   this.onCreate(null);
               }else{
            	   this.finish();
               }
              return true;
        	}
        return false; 
    } 
}
