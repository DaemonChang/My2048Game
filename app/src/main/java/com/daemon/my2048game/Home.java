package com.daemon.my2048game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.daemon.my2048game.view.GameView;



public class Home extends ActionBarActivity implements View.OnClickListener{

    private GameView mGameView;
    private static Home mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //-10-
        //动态添加gameview。相当于 -8- -2- -6-封装起来到init()
        RelativeLayout rl_home_content = (RelativeLayout) findViewById(R.id.rl_home_content);
        mGameView = new GameView(this);

        rl_home_content.addView(mGameView);


        //-20-初始化button并注册监听
        Button bt_restart = (Button) findViewById(R.id.bt_home_restart);
        Button bt_revert = (Button) findViewById(R.id.bt_home_revert);
        Button bt_option = (Button) findViewById(R.id.bt_home_option);

        bt_restart.setOnClickListener(this);
        bt_revert.setOnClickListener(this);
        bt_option.setOnClickListener(this);





        //-1-
   /*      GridLayout gl_home_content = (GridLayout) findViewById(R.id.gl_home_content);


       此处已封装到-11-
        //-8-

        //宽高应该动态获取，然后除以gridlayout的列数

        WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

        final Display defaultDisplay = windowManager.getDefaultDisplay();
        //旧方法
       // int height = defaultDisplay.getHeight();
        //int width = defaultDisplay.getWidth();
       // Log.v("Home","height:"+height+","+"width:"+width);//height:1184,width:720

        //新方法
        DisplayMetrics metrics = new DisplayMetrics();
        defaultDisplay.getMetrics(metrics);
        int height2 = metrics.heightPixels;
        int width2 = metrics.widthPixels;

        Log.v("Home","height2:"+height2+","+"width2:"+width2);//height2:1184,width2:720

        //-2-
        //初始化中间的内容布局
        for(int i = 0;i<4;i++){
            for(int j = 0;j<4;j++){
              //  TextView tv = new MyTextView(this);
                //tv.setText(i+","+j);

                //-6-
                NumberItem item = new NumberItem(this,0);

               //可以指定增加子控件的宽、高,但动态获取更好
                gl_home_content.addView(item,width2/4,width2/4);

            }

        }
*/


    }


    public static Home getActivity(){
        return mActivity;
    }
    //-19-
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_home_restart:
                restart();
                break;
            case R.id.bt_home_revert:
                revert();
                break;
            case R.id.bt_home_option:
                option();
                break;

        }

    }

    private void option() {

    }

    private void revert() {
        new AlertDialog.Builder(this)
                .setTitle("啊噢")
                .setMessage("悔棋？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGameView.revertHistory();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }

    private void restart() {
        new AlertDialog.Builder(this)
                .setTitle("啊噢")
                .setMessage("重新开始？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGameView.restart();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }
}
