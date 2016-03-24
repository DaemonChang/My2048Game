package com.daemon.my2048game.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;

import com.daemon.my2048game.Home;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10319 on 03/22/16.
 */
//-9-
public class GameView extends GridLayout {

    private NumberItem[][] mNumberItemMatrix;

    private int mRowColumn = 4;

    private List<Point> blankList;
  //用于滑动时记录的坐标
    double startX = 0;
    double startY = 0;
    double endX;
    double endY;

    //该矩阵用于记录上一步棋盘
    private int[][] historyMatrix;

    //该数组用于记录滑动之后每行或每列合并后的数值
    List<Integer> calculatorList;
    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    private void init(){
        blankList = new ArrayList<>();
        mNumberItemMatrix = new NumberItem[mRowColumn][mRowColumn];
        calculatorList = new ArrayList<>();

        historyMatrix = new int[mRowColumn][mRowColumn];
        //-11-封装-8-2-6-

        //宽高应该动态获取，然后除以gridlayout的列数

        WindowManager windowManager = (WindowManager)getContext().
                getSystemService(getContext().WINDOW_SERVICE);

        final Display defaultDisplay = windowManager.getDefaultDisplay();

        //新方法
        DisplayMetrics metrics = new DisplayMetrics();
        defaultDisplay.getMetrics(metrics);
        int width = metrics.widthPixels;

        setRowCount(mRowColumn);
        setColumnCount(mRowColumn);

        //初始化中间的内容布局
        for(int i = 0;i<mRowColumn;i++){
            for(int j = 0;j<mRowColumn;j++){

                NumberItem item = new NumberItem(getContext(),0);

                addView(item, width / mRowColumn, width / mRowColumn);

                //-13-
                //矩阵记录每个item的状况
                mNumberItemMatrix[i][j] = item;


                //初始化的时候记录当前空白的位置

                blankList.add(new Point(i,j));

            }

        }
        //继续初始化棋盘view，一开始看到的时候，里面应该有随机出现的两个非0数字
        //利用List<Point>记录棋盘上的空白位置

      //表示随机找两个空白位置，产生一个数字
        addRandomNumber();

        addRandomNumber();


    }
    //随机找一个位置，给其item赋值
    private void addRandomNumber() {

        updateBlankList();

        final int size = blankList.size();
        final int index = (int)Math.floor(Math.random()*size);
        //Log.v("addRandomNumber","blankList.size:"+size +",index:"+index);
        if(size!=0) {
            final Point point = blankList.get(index);
            mNumberItemMatrix[point.x][point.y].setTextNumber(Math.random() > 0.5d ? 2 : 4);
        }
    }
    //-14-
    private void updateBlankList() {
        //清空blankList
        blankList.clear();
        //遍历矩阵里item的number为0的加入到blankList中，用于下次随机生成item不重叠
        for(int i = 0;i<mRowColumn;i++) {
            for (int j = 0; j < mRowColumn; j++) {
                NumberItem numberItems = mNumberItemMatrix[i][j];
                if(numberItems.getNumber() == 0){
                    blankList.add(new Point(i,j));

                }

            }
        }
    }

    //-15-
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                //保存未滑动前的棋盘
                saveHistory();


                Log.v("GameView","DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v("GameView","MOVE");
                break;

            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();
                Log.v("GameView","UP");

                judgeDeriction(startX, startY, endX, endY);

                //-18-判断游戏是否结束（1：继续；2：成功；3：失败）
                handleResult();

                break;

        }


        return true;//super.onTouchEvent(event);
        //true表示当前控件来处理这个触摸事件的序列
    }
    ///保存未滑动前的棋盘，用于悔棋操作
    private void saveHistory() {
        for(int i =0;i<mRowColumn;i++){
            for(int j=0;j<mRowColumn;j++){
                historyMatrix[i][j] = mNumberItemMatrix[i][j].getNumber();
            }

        }

    }
    //悔棋操作
    public void revertHistory(){
        if(endX!=0) {
            for (int i = 0; i < mRowColumn; i++) {
                for (int j = 0; j < mRowColumn; j++) {

                    mNumberItemMatrix[i][j].setTextNumber(historyMatrix[i][j]);
                }

            }
        }
    }

    private void handleResult() {
        switch (isOver()){
            case 1://继续玩
                addRandomNumber();
                break;
            case 2://成功了
                 new AlertDialog.Builder(getContext())
                         .setTitle("恭喜")
                         .setMessage("闯关成功！")
                         .setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 restart();

                             }
                         })
                         .setNegativeButton("挑战更难", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                             }
                         }).show();

                break;
            case 3://失败咯
                new AlertDialog.Builder(getContext())
                        .setTitle("哎呀")
                        .setMessage("挑战失败！")
                        .setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                restart();
                            }
                        })
                        .setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Home.getActivity().finish();
                            }
                        }).show();

                break;

        }
    }

    public void restart() {
        removeAllViews();
        init();
    }

    private int isOver() {
        //遍历棋盘数值，判断是否达到目标
        for(int i =0;i<mRowColumn;i++){
            for(int j=0;j<mRowColumn;j++){
                if(mNumberItemMatrix[i][j].getNumber()==1024){
                    return 2;//成功，返回2
                }

            }

        }//for
        //遍历完，没成功
        updateBlankList();
         if(blankList.size() == 0){
            //若还可以合并，返回1
             //按行遍历（水平），查看左右相邻是否有相等的数
             for(int i =0;i<mRowColumn;i++){
                 for(int j=0;j<mRowColumn-1;j++) {
                    int currentN = mNumberItemMatrix[i][j].getNumber();
                    int nextN = mNumberItemMatrix[i][j+1].getNumber();

                     if(currentN == nextN){
                        return 1;
                     }
                 }
             }
             //按列遍历（竖直），查看上下相邻是否有相等的数
             for(int i =0;i<mRowColumn;i++){
                 for(int j=0;j<mRowColumn-1;j++) {
                     int currentN = mNumberItemMatrix[j][i].getNumber();
                     int nextN = mNumberItemMatrix[j+1][i].getNumber();

                     if(currentN == nextN){
                         return 1;
                     }
                 }
             }

             //若不可再合并，返回3
                return 3;

         }//if

        //有空白位置
        return 1;


    }

    private void judgeDeriction(double startX, double startY, double endX, double endY) {


        boolean flag = Math.abs(startX-endX) > Math.abs(startY-endY)?true:false;

        if(flag){//水平移动
            if(endX > startX){//右移
                slideRight();

            }else{//左移
                slideLeft();

            }

        }else{//垂直移动
            if(endY >startY){//下移
                slideDown();
            }else{//上移
                slideUp();
            }


        }

    }
    //-16-
    private void slideUp() {
        int pre_number = -1;
        //列遍历矩阵
        for(int i =0;i<mRowColumn;i++){
           for(int j=0;j<mRowColumn;j++){
              final int number = mNumberItemMatrix[j][i].getNumber();

               if(number!=0 ){
                   if(number!=pre_number&& pre_number!=-1){
                      calculatorList.add(pre_number);

                   }else if(pre_number!=-1){
                       calculatorList.add(number*2);
                      pre_number = -1;
                       continue;
                   }
                   pre_number = number;
               }

           }
            //把最后一个元素加入到集合中
            if(pre_number!=-1) {
                calculatorList.add(pre_number);
            }

           //把通过计算后合并的数字放到矩阵中
            for(int x = 0;x<calculatorList.size();x++){
                mNumberItemMatrix[x][i].setTextNumber(calculatorList.get(x));
            }

            //剩余的用0来补充
            for(int y = calculatorList.size();y<mRowColumn;y++){
                mNumberItemMatrix[y][i].setTextNumber(0);

            }

            //重置calculatorList和pre_number
            calculatorList.clear();
            pre_number=-1;
        }

        Log.v("GameView", "上移");
    }
    //-17-
    private void slideDown() {
        int pre_number = -1;
        //列遍历矩阵
        for(int i =0;i<mRowColumn;i++){
            for(int j=mRowColumn-1;j>=0;j--){
                final int number = mNumberItemMatrix[j][i].getNumber();

                if(number!=0 ){
                    if(number!=pre_number&& pre_number!=-1){
                        calculatorList.add(pre_number);

                    }else if(pre_number!=-1){
                        calculatorList.add(number*2);
                        pre_number = -1;
                        continue;
                    }
                    pre_number = number;
                }

            }
            //把最后一个元素加入到集合中
            if(pre_number!=-1) {
                calculatorList.add(pre_number);
            }

            //把通过计算后合并的数字放到矩阵中
            for(int x = 0;x<calculatorList.size();x++){
                mNumberItemMatrix[mRowColumn-x-1][i].setTextNumber(calculatorList.get(x));
            }

            //剩余的用0来补充
            for(int y = mRowColumn-calculatorList.size()-1;y>=0;y--){
                mNumberItemMatrix[y][i].setTextNumber(0);

            }

            //重置calculatorList和pre_number
            calculatorList.clear();
            pre_number=-1;
        }

        Log.v("GameView", "下移");
    }

    private void slideLeft() {
        int pre_number = -1;
        //列遍历矩阵
        for(int i =0;i<mRowColumn;i++){
            for(int j=0;j<mRowColumn;j++){
                final int number = mNumberItemMatrix[i][j].getNumber();

                if(number!=0 ){
                    if(number!=pre_number&& pre_number!=-1){
                        calculatorList.add(pre_number);

                    }else if(pre_number!=-1){
                        calculatorList.add(number*2);
                        pre_number = -1;
                        continue;
                    }
                    pre_number = number;
                }

            }
            //把最后一个元素加入到集合中
            if(pre_number!=-1) {
                calculatorList.add(pre_number);
            }

            //把通过计算后合并的数字放到矩阵中
            for(int x = 0;x<calculatorList.size();x++){
                mNumberItemMatrix[i][x].setTextNumber(calculatorList.get(x));
            }

            //剩余的用0来补充
            for(int y = calculatorList.size();y<mRowColumn;y++){
                mNumberItemMatrix[i][y].setTextNumber(0);

            }

            //重置calculatorList和pre_number
            calculatorList.clear();
            pre_number=-1;
        }

        Log.v("GameView", "左移");
    }

    private void slideRight() {
        int pre_number = -1;
        //列遍历矩阵
        for(int i =0;i<mRowColumn;i++){
            for(int j=mRowColumn-1;j>=0;j--){
                final int number = mNumberItemMatrix[i][j].getNumber();

                if(number!=0 ){
                    if(number!=pre_number&& pre_number!=-1){
                        calculatorList.add(pre_number);

                    }else if(pre_number!=-1){
                        calculatorList.add(number*2);
                        pre_number = -1;
                        continue;
                    }
                    pre_number = number;
                }

            }
            //把最后一个元素加入到集合中
            if(pre_number!=-1) {
                calculatorList.add(pre_number);
            }

            //把通过计算后合并的数字放到矩阵中
            for(int x = 0;x<calculatorList.size();x++){
                mNumberItemMatrix[i][mRowColumn-x-1].setTextNumber(calculatorList.get(x));
            }

            //剩余的用0来补充
            for(int y = mRowColumn-calculatorList.size()-1;y>=0;y--){
                mNumberItemMatrix[i][y].setTextNumber(0);

            }

            //重置calculatorList和pre_number
            calculatorList.clear();
            pre_number=-1;
        }
        Log.v("GameView", "右移");
    }
}
