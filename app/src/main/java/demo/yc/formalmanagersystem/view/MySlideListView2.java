package demo.yc.formalmanagersystem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * Created by user on 2016/7/25.
 */
public class MySlideListView2 extends ListView {


    public MySlideListView2(Context context) {
        super(context);
        scroller = new Scroller(context);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public MySlideListView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }

    public MySlideListView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }

    /**禁止侧滑模式*/
    public static int MOD_FORBID = 0;

    public static int MOD_LEFT = 1;

    public static int MOD_RIGHT = 2;

    public static int MOD_BOTH = 3;

    private int mode = MOD_FORBID;

    private int leftLength = 0;

    private int rightLength = 0;

    private int slidePosition;

    private int downY;

    private int downX;

    private View itemView;

    private Scroller scroller;

    private int mTouchSlop;

    private boolean canMove = false;

    private boolean isSlided = false;

    public void initSlideMode(int mode){

        this.mode = mode;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        int lastX = (int) ev.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(this.mode == MOD_FORBID){
                    return super.onTouchEvent(ev);
                }
                if (isSlided) {
                    scrollBack();
                    return false;
                }
                if (!scroller.isFinished()) {
                    return false;}

                downX = (int) ev.getX();

                downY = (int) ev.getY();

                slidePosition = pointToPosition(downX, downY);

                if (slidePosition == AdapterView.INVALID_POSITION) {
                    if(isSlided) {
                        scrollBack();
                        return true;
                    }
                    return super.onTouchEvent(ev);
                }
                itemView = getChildAt(slidePosition - getFirstVisiblePosition());

                if(this.mode == MOD_BOTH){
                    this.leftLength = -itemView.getPaddingLeft();

                    this.rightLength = -itemView.getPaddingRight();

                }else if(this.mode == MOD_LEFT){

                    this.leftLength = -itemView.getPaddingLeft();

                }else if(this.mode == MOD_RIGHT){
                    this.rightLength = -itemView.getPaddingRight();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                //  如果滑动超过一定距离，则取消触摸时间。达到控制滑动的最大距离
                if(canMove && (Math.abs(ev.getX() - downX)>2*Math.abs(this.leftLength)))
                {
                    return false;
                }
                if (!canMove && slidePosition != AdapterView.INVALID_POSITION
                        && (Math.abs(ev.getX() - downX) > mTouchSlop && Math.abs(ev.getY() - downY) < mTouchSlop))
                {
                int offsetX = downX - lastX;
                if(offsetX > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT))
                {
                    canMove = true;
                }else if(offsetX < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT))
                {
                    canMove = true;
                }else{
                    canMove = false;
                }
                MotionEvent cancelEvent = MotionEvent.obtain(ev);
                cancelEvent.setAction(MotionEvent.ACTION_CANCEL | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                onTouchEvent(cancelEvent);
            }

            if (canMove) {

                requestDisallowInterceptTouchEvent(true);
                int deltaX = downX - lastX;

                if (deltaX < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT))
                {
                    itemView.scrollTo(deltaX, 0);
                } else if (deltaX > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT))
                {

                    itemView.scrollTo(deltaX, 0);
                } else {
                    itemView.scrollTo(0, 0);
                }
                return true; // 拖动的时候ListView不滚动
            }

            case MotionEvent.ACTION_UP:
                if (canMove){
                    canMove = false;
                    scrollByDistanceX();
                }
                break;

        }
        return super.onTouchEvent(ev);
    }



    private void scrollByDistanceX() {
        if(this.mode == MOD_FORBID){
            return;
        }

        if(itemView.getScrollX() > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT)){
            if (itemView.getScrollX() >= rightLength / 2) {
                scrollLeft();
            }  else {
                scrollBack();
            }

        }else if(itemView.getScrollX() < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT)){
            if (itemView.getScrollX() <= -leftLength / 2) {
                scrollRight();
            } else {
                scrollBack();
            }
        }else{
            scrollBack();
        }
    }

    private void scrollRight() {

        isSlided = true;

        final int delta = (leftLength + itemView.getScrollX());
        scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));

        postInvalidate(); // 刷新itemView

    }

    private void scrollLeft() {

        isSlided = true;

        final int delta = (rightLength - itemView.getScrollX());

        scroller.startScroll(itemView.getScrollX(), 0, delta, 0,

                Math.abs(delta));

        postInvalidate(); // 刷新itemView

    }

    private void scrollBack() {

        isSlided = false;

        scroller.startScroll(itemView.getScrollX(), 0, -itemView.getScrollX(),
                0, Math.abs(itemView.getScrollX()));
        postInvalidate(); // 刷新itemView

    }
    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {

            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    //外部调用关闭侧滑
    public void slideBack()
    {
        this.scrollBack();
    }
    public boolean isSlided()
    {
        return this.isSlided;
    }
    public void setIsSlided(boolean isSlided)
    {
        this.isSlided = isSlided;
    }

}
