package com.daemon.my2048game.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 10319 on 03/21/16.
 */
//-3-
//自定义textview，加上边框
public class MyTextView extends TextView{
    //一般在代码中new出时调用
    public MyTextView(Context context) {
        super(context);
    }

    //一般给系统调用，通过该方法实例化控件时，会把xml里定义的属性一并传进来
    //供控件使用
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //-4-
    @Override
    protected void onDraw(Canvas canvas) {
        //先在画布上画上一个自定义的外框
        Paint p1 = new Paint();
        p1.setColor(0xFFF5F5F5);
        p1.setStyle(Paint.Style.STROKE);
        //必须是已经调用onMeasure方法后才可以得到正确的值
        canvas.drawRect(1,1,getMeasuredWidth(),
                getMeasuredHeight(),p1);

        //然后让其画出textview的内容和背景
        super.onDraw(canvas);
    }
}
