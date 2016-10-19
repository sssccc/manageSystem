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
public class MySlideListView extends ListView {


    public MySlideListView(Context context) {
        super(context);
        scroller = new Scroller(context);
        initSlideMode(3);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public MySlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        initSlideMode(3);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }

    public MySlideListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
        initSlideMode(3);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }


    /**禁止侧滑模式*/
    public static int MOD_FORBID = 0;  //    表示禁止滑动
    public static int MOD_LEFT = 1;     //   表示允许左滑
    public static int MOD_RIGHT = 2;    //   表示允许右滑
    public static int MOD_BOTH = 3;     //   表示允许左右滑动
    private int mode = MOD_FORBID;      //    记录滑动模式

    private int leftLength = 0;         //    左滑弹出的长度  该项目中是 80  根据实际的布局决定
    private int rightLength = 0;        //    右滑弹出的长度  该项目中是 80

    private int slidePosition;          //      手指按下时，对应listView 的第几个item
    private int downY;                  //      手指按下的x
    private int downX;                  //      手指按下的y

    private View itemView;              //      滑动选择 listView 中的item视图

    private Scroller scroller;          //      滑动块，帮助listView item 具有滑动功能

    private int mTouchSlop;             //      判断是否 达到滑动条件   就是滑动距离超过一定长度

    private boolean canMove = false;       //   判断是否可以滑动

    private boolean isSlided = false;     //   表示侧滑是否已经弹出来了


    //给外部调用的，用来决定滑动模式
    public void initSlideMode(int mode)
    {

        this.mode = mode;

    }


    //  继承触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {


        final int action = ev.getAction();

        //记录当前的x位置 ，这个值在手指滑动的过程中，不断改变，
        // 然后与downX 进行计算比较
        int lastX = (int) ev.getX();


        switch (action)
        {
            //对手指按下时候的处理
            case MotionEvent.ACTION_DOWN:
                //不允许滑动，则对触摸事件不做任何处理。返回上一级
                if(this.mode == MOD_FORBID)
                {
                    return super.onTouchEvent(ev);
                }

                //如果滑动已经弹出来了，则关闭滑动，返回上一级
                if (isSlided)
                {
                    scrollBack();
                    return false;
                }

                //如果滑动块正在滑动，则不做任何处理，返回上一级
                if (!scroller.isFinished())
                {
                    return false;
                }
                downX = (int) ev.getX(); //手指按下的x
                downY = (int) ev.getY(); //手指按下的y
                slidePosition = pointToPosition(downX, downY); // 记录选中adapter中第几个item

                //如果点击的地方，不属于listView 的任何一个地方，则不做任何处理，返回给上一级
                if (slidePosition == AdapterView.INVALID_POSITION)
                {
                    return super.onTouchEvent(ev);
                }

                //通过计算获得当前点击的listView 的item视图
                //slidePosition（当前的第几个） - getFirstVisiblePosition(屏幕上中的第一个ite在adapter上的位置)；
                //这点还是有一点不理解。。。。具体的含义不清楚
                //
                itemView = getChildAt(slidePosition - getFirstVisiblePosition());


                //根据滑动模式，计算出对应的侧滑弹出的长度。
                if(this.mode == MOD_BOTH)
                {
                    this.leftLength = -itemView.getPaddingLeft();
                    this.rightLength = -itemView.getPaddingRight();
                }else if(this.mode == MOD_LEFT)
                {

                    this.leftLength = -itemView.getPaddingLeft();
                }else if(this.mode == MOD_RIGHT)
                {
                    this.rightLength = -itemView.getPaddingRight();
                }
                break;


            // 对手指滑动，做对应的处理
            case MotionEvent.ACTION_MOVE:

                // 如果可以滑动，并且当滑动的距离超过150 ，取消滑动事件，
                //达到限制滑动长度的作用。
                if(canMove && (Math.abs(ev.getX() - downX)>2*Math.abs(this.leftLength)))
                {
                    return false;
                }

                //如果canMove 不能滑动，但是手指选中的listView item有效，而且达到了可以滑动的条件。则对canMove进行设置
                if (!canMove && slidePosition != AdapterView.INVALID_POSITION
                        && (Math.abs(ev.getX() - downX) > mTouchSlop && Math.abs(ev.getY() - downY) < mTouchSlop))
                {
                    //计算手指滑动距离
                    int offsetX = downX - lastX;
                    //如果滑动距离大于零（表示手指向左动）  并且滑动模式允许右滑，则将canMove 设置为true
                    if(offsetX > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT))
                    {
                        canMove = true;
                        //如果滑动距离小于零（表示手指向右滑动）  并且滑动模式允许左滑，则将canMove 设置为true
                    }else if(offsetX < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT))
                    {
                        canMove = true;
                        //否则设置为不能移动
                    }else
                    {
                        canMove = false;
                    }

                    //这一段不能理解
                    /*此段代码是为了避免我们在侧向滑动时同时出发ListView的OnItemClickListener时间*/
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    onTouchEvent(cancelEvent);
                }


                //如果可以滑动
                if (canMove)
                {
                    requestDisallowInterceptTouchEvent(true);
                    //计算手指滑动距离
                    int deltaX = downX - lastX;
                    //小于0 ，表示弹出左边的滑动界面
                    if (deltaX < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT))
                    {
                        itemView.scrollTo(deltaX, 0);

                        //大于0 ，表示弹出右边的滑动界面
                    } else if (deltaX > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT))
                    {

                        itemView.scrollTo(deltaX, 0);
                    } else
                    {
                        itemView.scrollTo(0, 0);
                    }
                    return true; // 拖动的时候ListView不滚动
                }

                //对手指抬上来，进行处理
            case MotionEvent.ACTION_UP:
                //如果可以移动，则表示移动结束，将canMove设置为false；
                if (canMove)
                {
                    canMove = false;

                    //并且计算移动结束后的是否要弹出侧滑
                    scrollByDistanceX();
                }
                break;

        }
        return super.onTouchEvent(ev);
    }



    //根据手指滑动的距离，来判断是否要弹出侧滑界面
    private void scrollByDistanceX()
    {
        //如果模式为禁止滑动，则不做任何处理
        if(this.mode == MOD_FORBID)
        {
            return;
        }

        // 如果滑动距离大于0，表示手指向右滑动，并且允许弹出左边侧滑
        if(itemView.getScrollX() > 0 && (this.mode == MOD_BOTH || this.mode == MOD_RIGHT))
        {
            //如果滑动距离大于右边侧滑界面长度的一半，则将左边侧滑完全弹出
            if (itemView.getScrollX() >= rightLength / 2)
            {
                scrollLeft();
            }else  //否则收缩回去
            {
                scrollBack();
            }

        }
        // 同理判断是否弹出右边的侧滑界面
        else if(itemView.getScrollX() < 0 && (this.mode == MOD_BOTH || this.mode == MOD_LEFT))
        {
            if (itemView.getScrollX() <= -leftLength / 2)
            {
                scrollRight();
            } else
            {
                scrollBack();
            }
        }else
        {
            scrollBack();
        }
    }

    //弹出右边的侧滑界面
    private void scrollRight()
    {

        isSlided = true;

        final int delta = (leftLength + itemView.getScrollX());
        scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));

        postInvalidate(); // 刷新itemView

    }

    private void scrollLeft()
    {

        isSlided = true;

        final int delta = (rightLength - itemView.getScrollX());

        scroller.startScroll(itemView.getScrollX(), 0, delta, 0, Math.abs(delta));

        postInvalidate(); // 刷新itemView

    }



    // 在左右侧滑弹出来之后，点击屏幕，把侧滑弹回去。
    private void scrollBack()
    {

        isSlided = false;

        scroller.startScroll(itemView.getScrollX(), 0, -itemView.getScrollX(),0, Math.abs(itemView.getScrollX()));
        postInvalidate(); // 刷新itemView

    }


    //时时刻刻计算当前view的滑动位置
    //自己重写内容，将item 滑动到指定位置。使滑动平缓，不卡顿
    //结合scroller 使用
    @Override
    public void computeScroll()
    {
        if (scroller.computeScrollOffset())
        {
            //将item 滑动到 scriller 当前的位置
            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate(); //更新界面
        }
    }


    //这个是嵌套scrollView 来用关掉listView 滑动的
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


    //外部调用关闭侧滑
    public void slideBack()
    {
        this.scrollBack();
    }


}
