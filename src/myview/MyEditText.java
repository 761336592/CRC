package myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText {

	
	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO �Զ����ɵĹ��캯�����
	}
	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO �Զ����ɵĹ��캯�����
	}
	
	public MyEditText(Context context) {
		super(context);
		// TODO �Զ����ɵĹ��캯�����
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO �Զ����ɵķ������
		 Paint paint =  new Paint();
		    paint.setTextSize(18);
		    paint.setColor(Color.GRAY);
		    //��д��ʾ���֡�
		    canvas.drawText("��ʾ����",2,getHeight()/2+5,paint);
		    super.onDraw(canvas);
		super.onDraw(canvas);
	}
}	
