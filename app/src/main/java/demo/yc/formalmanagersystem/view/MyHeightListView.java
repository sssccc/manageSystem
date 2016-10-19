package demo.yc.formalmanagersystem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/7/28 0028.
 */
public class MyHeightListView extends ListView {
    public MyHeightListView(Context context) {
        super(context);
    }

    public MyHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHeightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
