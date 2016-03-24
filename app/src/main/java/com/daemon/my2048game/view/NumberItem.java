package com.daemon.my2048game.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by 10319 on 03/21/16.
 */
//-5-
public class NumberItem extends FrameLayout{
    private TextView mTv;
    private int number;

    public NumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(0);
    }

    public NumberItem(Context context) {
        super(context);
        initView(0);
    }
    public NumberItem(Context context,int number) {
        super(context);
        initView(number);

    }

    public int getNumber() {
        return number;
    }

    private void initView(int number){
        setBackgroundColor(0xFFF5F5F5);
        mTv = new MyTextView(getContext());

        //-7-
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );

        params.setMargins(5, 5, 5, 5);//默认5px
      /*  mTv.setText(number + "");
        mTv.setBackgroundColor(0xFFEDE6DE);
        */
        //数字和颜色封装到setTextNumber()
        setTextNumber(number);
        mTv.setTextSize(25);
        mTv.setGravity(Gravity.CENTER);

        addView(mTv);
        this.number = number;
    }

    //-12-
    public void setTextNumber(int num){
        //更改控件的显示数字的同时，应该把里面保存数值的值更改
        number = num;
        //显示数字
        if(num == 0){
           mTv.setText("");
        }else{
            mTv.setText(num+"");
        }

        //背景颜色
        switch(num){
            case 0:
                mTv.setBackgroundColor(0xFFF5F5F5);//ARGB
                break;
            case 2:
                mTv.setBackgroundColor(0xFFE3F2FD);
                break;
            case 4:
                mTv.setBackgroundColor(0xFFBBDEFB);
                break;
            case 8:
                mTv.setBackgroundColor(0xFF90CAF9);
                break;
            case 16:
                mTv.setBackgroundColor(0xFF64B5F6);
                break;
            case 32:
                mTv.setBackgroundColor(0xFF42A5F5);
                break;
            case 64:
                mTv.setBackgroundColor(0xFF2196F3);
                break;
            case 128:
                mTv.setBackgroundColor(0xFF1E88E5);
                break;
            case 256:
                mTv.setBackgroundColor(0xFF1976D2);
                break;
            case 512:
                mTv.setBackgroundColor(0xFF1565C0);
                break;
            case 1024:
                mTv.setBackgroundColor(0xFF0D47A1);
                break;
            case 2048:
                mTv.setBackgroundColor(0xFF1A237E);
                break;
            case 4096:
                mTv.setBackgroundColor(0xFF263238);
                break;
        }


    }



}
