package com.example.da08.threadclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.drawable.GradientDrawable.LINE;

public class MainActivity extends AppCompatActivity {

    int deviceHeight;
    int deviceWidth;

    int center_x, center_y;  // 중심 좌표

    int Line = 0;

    double angle = 0;
    double end_x, end_y;


    CustomView stage;

    // while문에 플래그를 줘야 앱을 종료했을때 Thread가 종료 됨 그러지 않으면 계속 실행되어 배터리 die....
    boolean runFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        // 화면 세로길이
        deviceHeight = metrics.heightPixels;
        // 화면 가로길이
        deviceWidth = metrics.widthPixels;
        // 중심점 가로
        center_x = deviceWidth /2;
        // 중심점 세로
        center_y = deviceHeight /2;
        // 선의 길이
        Line = center_x -50;


        stage = new CustomView(this);
        setContentView(stage);

        // 화면을 그려주는 thread를 동작시킨다
        new DrawStage().start();

        Sec sec1 = new Sec(5, center_x, center_y, LINE, 1000/360);
        stage.addsec(sec1);

    }

    class Sec extends Thread{  // 초침
        Paint paint = new Paint();

        float sx;
        float sy;

        double angle;
        double line;

        double ex;
        double ey;

        int interval = 0;


        public  Sec(int stroke, int x, int y, int length, int interval){
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(stroke);

            sx = x;
            sy = y;
            line = length;
            angle = 0;
            this.interval = interval; // 시간 간격
        }

        @Override
        public void run() {
            while(true){
                angle = angle +1;
                // 화면의 중앙부터 12시 방향으로 직선을 그음
                double angle_temp = angle - 90;
                end_x = Math.cos(angle_temp * Math.PI / 180) * Line + center_x; // x좌표 , Math.PI/180은 원주율
                end_y = Math.sin(angle_temp * Math.PI / 180) * Line + center_y;  // y좌표

                if(interval > 0){
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    // View률 1초에 한번씩 갱신
    class DrawStage extends Thread{
        @Override
        public void run() {
//            super.run();
            while (true){
                stage.postInvalidate();
            }
        }
    }

    class CustomView extends View{
        List<Sec> secc = new ArrayList<>();

        public CustomView(Context context) {
            super(context);
        }

        public void addsec(Sec sec){
            secc.add(sec);
            sec.start();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if(secc.size()>0){
                for(Sec sec : secc){
                    canvas.drawLine(sec.sx, sec.sy,(float)sec.ex, (float)sec.ey,sec.paint);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runFlag = false; // thread 종료
    }
}