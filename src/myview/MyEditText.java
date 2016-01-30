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
		// TODO 自动生成的构造函数存根
	}
	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}
	
	public MyEditText(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO 自动生成的方法存根
		 Paint paint =  new Paint();
		    paint.setTextSize(18);
		    paint.setColor(Color.GRAY);
		    //编写提示文字。
		    canvas.drawText("提示文字",2,getHeight()/2+5,paint);
		    super.onDraw(canvas);
		super.onDraw(canvas);
	}
}	
