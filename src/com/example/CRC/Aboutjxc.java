package com.example.CRC;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class Aboutjxc extends Activity{

	 protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.about_jxc);
			RelativeLayout lay=(RelativeLayout)findViewById(R.id.about);
			lay.setOnClickListener(fhClickListener);
	 }
	 private OnClickListener fhClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			 Aboutjxc.this.finish();
		}
	};
		

}
