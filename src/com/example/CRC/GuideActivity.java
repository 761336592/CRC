package com.example.CRC;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideActivity extends Activity {
	private ViewPager viewPager; 
    
    /**װ��ҳ��ʾ��view������*/
    private ArrayList<View> pageViews;     
    private ImageView imageView; 
      
    /**��СԲ���ͼƬ�������ʾ*/
    private ImageView[] imageViews; 
      
    //��������ͼƬ��LinearLayout 
    private ViewGroup viewPics; 
      
    //����СԲ���LinearLayout 
    private ViewGroup viewPoints; 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        LayoutInflater inflater = getLayoutInflater(); 
        pageViews = new ArrayList<View>(); 
        pageViews.add(inflater.inflate(R.layout.connectbt, null)); 
        pageViews.add(inflater.inflate(R.layout.inputinf, null)); 
        pageViews.add(inflater.inflate(R.layout.net, null)); 
      /*  pageViews.add(inflater.inflate(R.layout.main_tab_friends, null)); 
        pageViews.add(inflater.inflate(R.layout.main_tab_settings, null));   */
        //����imageviews���飬��С��Ҫ��ʾ��ͼƬ������ 
        imageViews = new ImageView[pageViews.size()]; 
        //��ָ����XML�ļ�������ͼ 
        viewPics = (ViewGroup) inflater.inflate(R.layout.guide, null);
        viewPoints = (ViewGroup) viewPics.findViewById(R.id.viewGroup); 
        viewPager = (ViewPager) viewPics.findViewById(R.id.guidePages); 
        //���СԲ���ͼƬ 
        for(int i=0;i<pageViews.size();i++){ 
            imageView = new ImageView(GuideActivity.this); 
            //����СԲ��imageview�Ĳ��� 
            imageView.setLayoutParams(new LayoutParams(60,60));//����һ����߾�Ϊ20 �Ĳ��� 
            imageView.setPadding(20, 0, 40, 0); 
            //��СԲ��layout��ӵ������� 
            imageViews[i] = imageView;    
            //Ĭ��ѡ�е��ǵ�һ��ͼƬ����ʱ��һ��СԲ����ѡ��״̬���������� 
            if(i==0){ 
                imageViews[i].setBackgroundResource(R.drawable.ic_preference_single_pressed); 
            }else{ 
                imageViews[i].setBackgroundResource(R.drawable.ic_preference_single_normal); 
            } 
          
            //��imageviews��ӵ�СԲ����ͼ�� 
            viewPoints.addView(imageViews[i]); 
        } 
        setContentView(viewPics); 
        viewPager.setAdapter(new GuidePageAdapter()); 
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());         
    }
    
    private Button.OnClickListener  Button_OnClickListener = new Button.OnClickListener() { 
        public void onClick(View v) { 
            //�����Ѿ����� 
            setGuided(); 
              
            //��ת 
            Intent mIntent = new Intent(); 
            mIntent.setClass(GuideActivity.this, log.class); 
            GuideActivity.this.startActivity(mIntent); 
            GuideActivity.this.finish(); 
        } 
    };  
	/*����Ѿ����������ҳ����*/
	private static final String SHAREDPREFERENCES_NAME = "my_pos"; 
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity"; 
    private void setGuided(){ 
        SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_NAME, 0); 
        SharedPreferences.Editor editor = settings.edit(); 
        editor.putString(KEY_GUIDE_ACTIVITY, "false"); 
        editor.commit(); 
    } 
    class GuidePageAdapter extends PagerAdapter{ 
    	  
        //����positionλ�õĽ��� 
        @Override
        public void destroyItem(View v, int position, Object arg2) { 
            // TODO Auto-generated method stub 
            ((ViewPager)v).removeView(pageViews.get(position)); 
              
        } 
  
        @Override
        public void finishUpdate(View arg0) { 
            // TODO Auto-generated method stub 
              
        } 
          
        //��ȡ��ǰ��������� 
        @Override
        public int getCount() { 
            // TODO Auto-generated method stub 
            return pageViews.size(); 
        } 
  
        //��ʼ��positionλ�õĽ��� 
        @Override
        public Object instantiateItem(View v, int position) { 
            // TODO Auto-generated method stub 
            ((ViewPager) v).addView(pageViews.get(position));   
              
             // ����ҳ��1�ڵİ�ť�¼�   
            if (position == 2) {   
              
            	TextView btn=(TextView)findViewById(R.id.entermu);
            	btn.setOnClickListener(Button_OnClickListener);
            }           
            return pageViews.get(position);   
        } 
  
        // �ж��Ƿ��ɶ������ɽ��� 
        @Override
        public boolean isViewFromObject(View v, Object arg1) { 
            // TODO Auto-generated method stub 
            return v == arg1; 
        } 
        @Override
        public void startUpdate(View arg0) { 
            // TODO Auto-generated method stub 
              
        } 
  
        @Override
        public int getItemPosition(Object object) { 
            // TODO Auto-generated method stub 
            return super.getItemPosition(object); 
        } 
  
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) { 
            // TODO Auto-generated method stub 
              
        } 
  
        @Override
        public Parcelable saveState() { 
            // TODO Auto-generated method stub 
            return null; 
        } 
    } 
    class GuidePageChangeListener implements OnPageChangeListener{ 
    	  
        @Override
        public void onPageScrollStateChanged(int arg0) { 
            // TODO Auto-generated method stub 
              
        } 
  
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) { 
            // TODO Auto-generated method stub 
              
        } 
  
        @Override
        public void onPageSelected(int position) { 
            // TODO Auto-generated method stub 
            for(int i=0;i<imageViews.length;i++){ 
                imageViews[position].setBackgroundResource(R.drawable.ic_preference_single_pressed); 
                //���ǵ�ǰѡ�е�page����СԲ������Ϊδѡ�е�״̬ 
                if(position !=i){ 
                    imageViews[i].setBackgroundResource(R.drawable.ic_preference_single_normal); 
                } 
            }  
        } 
    }
}
