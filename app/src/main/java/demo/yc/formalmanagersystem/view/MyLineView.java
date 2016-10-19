package demo.yc.formalmanagersystem.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 2016/7/25.
 */
public class MyLineView extends View {

    int tabNum,currentNum;

    float screenWidth,tabWidth,offSet;

    final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public MyLineView(Context context) {
        super(context);
    }

    public MyLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTabNum(int num)
    {
        tabNum = num;
    }

    public void setCurrentNum(int num)
    {
        currentNum = num;
        offSet =0;
    }

    public void setOffSet(int pos,float offSet)
    {
        if(offSet == 0)
            return ;
        currentNum = pos;
        this.offSet = offSet;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(tabWidth == 0) {
            screenWidth = getWidth();
            tabWidth = screenWidth/tabNum;
        }

        float left = (currentNum + offSet)*tabWidth +10;
        float right = left +tabWidth -20;
        float top = getPaddingTop();
        float bottom = getHeight() - getPaddingBottom();
        paint.setColor(Color.parseColor("#ff0000"));
        canvas.drawRect(left,top,right,bottom,paint);

    }
}
