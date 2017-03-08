package com.lyy2016.ball;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lyy2016.ball.countdowntimer.*;
import java.util.Timer;
import java.util.TimerTask;

public class Count extends Activity implements View.OnClickListener {
    int scoreTeamA = 0;
    int scoreTeamB = 0;
    private TextView tvTime;
    private EditText second;
    private EditText mm;
    private EditText qi;
    private TextView a;
    private TextView b;
    private EditText mi;
    String ccc,mmm;
    LinearLayout ll1,ll2;
    private Button btnStart;
    private Button btnStop;
    private long timer_unit = 1000,c,m;
    private long distination_total = timer_unit*2880;
    private long timer_couting;
    private Vibrator vibrator;
    private int timerStatus = CountDownTimerUtil.PREPARE;

    private Timer timer;
    private TimerTask timerTask;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(timer_couting==distination_total){
                        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        vibrator.vibrate(new long[]{1000,1000,1000,1000},0);
                        dialog();
                        btnStart.setText("开始");
                    }
                    tvTime.setText(formateTimer(timer_couting));
                    break;
                case 2:
                    if(timer_couting==distination_total){
                        Toast.makeText(Count.this, "重置成功！", Toast.LENGTH_SHORT).show();
                        btnStart.setText("开始");
                        tvTime.setVisibility(View.GONE);
                    }
                    tvTime.setText(formateTimer(timer_couting));
                    break;
            }
        }
    };

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Count.this);
        builder.setMessage("比赛结束");
        builder.setTitle("提示");
        builder.setCancelable(false);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onStop();
                    }

                });
        builder.create().show();
    }

    protected void onStop(){
        if(null!=vibrator){
            vibrator.cancel();
        }
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        second = (EditText) findViewById(R.id.s);
        mm = (EditText) findViewById(R.id.m);
        mi = (EditText) findViewById(R.id.mi);
        qi = (EditText) findViewById(R.id.qi);
        a = (TextView) findViewById(R.id.a);
        b = (TextView) findViewById(R.id.b);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        tvTime = (TextView) findViewById(R.id.tv_time);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        tvTime.setText(formateTimer(timer_couting));
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        initTimerStatus();
        tvTime.setText(formateTimer(timer_couting));
        second.setText("48");
        mm.setText("0");
        tvTime.setVisibility(View.GONE);
    }

    private void initTimerStatus(){
        timerStatus = CountDownTimerUtil.PREPARE;
        timer_couting = distination_total;
    }

    public void submit(View v) {
        if (!TextUtils.isEmpty(mi.getText())&& !TextUtils.isEmpty(qi.getText())) {
            ll2.setVisibility(View.GONE);
            a.setText(mi.getText());
            b.setText(qi.getText());
        }else {
            Toast.makeText(this, "队名不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer(){
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask, 0, timer_unit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                ll2.setVisibility(View.GONE);
                ll1.setVisibility(View.GONE);
                switch (timerStatus){
                    case CountDownTimerUtil.PREPARE:
                        ccc = second.getText().toString();
                        mmm = mm.getText().toString();
                        if(TextUtils.isEmpty(ccc)){
                            c = 0;
                        }else {
                            c = Integer.parseInt(ccc);
                        }
                        if(TextUtils.isEmpty(mmm)){
                            m = 0;
                        }else {
                            m = Integer.parseInt(mmm);
                        }
                        distination_total = (c*60+m)*timer_unit;
                        timer_couting = distination_total;
                        tvTime.setVisibility(View.VISIBLE);
                        startTimer();
                        timerStatus = CountDownTimerUtil.START;
                        btnStart.setText("暂停");
                        break;
                    case CountDownTimerUtil.START:
                        timer.cancel();
                        timerStatus = CountDownTimerUtil.PASUSE;
                        btnStart.setText("继续");
                        break;
                    case CountDownTimerUtil.PASUSE:
                        startTimer();
                        timerStatus = CountDownTimerUtil.START;
                        btnStart.setText("暂停");
                        break;
                }
                break;
            case R.id.btn_stop:
                if(timer!=null){
                    timer.cancel();
                    initTimerStatus();
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.VISIBLE);
                    tvTime.setVisibility(View.VISIBLE);
                    scoreTeamA = 0;
                    scoreTeamB = 0;
                    second.setText("48");
                    mm.setText("0");
                    a.setText("信电院队");
                    b.setText("其他院队");
                    mi.setText("信电院队");
                    qi.setText("其他院队");
                    displayForTeamA(0);
                    displayForTeamB(0);
                    mHandler.sendEmptyMessage(2);
                }
                break;
        }
    }

    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            timer_couting -=timer_unit;
            if(timer_couting==0){
                cancel();
                initTimerStatus();
            }
            mHandler.sendEmptyMessage(1);
        }
    }

    public void displayForTeamA(int score) {
        scoreTeamA += score;
        TextView scoreView = (TextView) findViewById(R.id.text_view_team_a);
        scoreView.setText(String.valueOf(scoreTeamA));
    }

    public void addThreeForTeamA(View v) {
        displayForTeamA(3);
    }

    public void addTwoForTeamA(View v) {
        displayForTeamA(2);
    }

    public void addOneForTeamA(View v) {
        displayForTeamA(1);
    }

//    Team B
public void displayForTeamB(int score) {
    scoreTeamB += score;
    TextView scoreView = (TextView) findViewById(R.id.text_view_team_b);
    scoreView.setText(String.valueOf(scoreTeamB));
}
    public void addThreeForTeamB(View v) {
        displayForTeamB(3);
    }

    public void addTwoForTeamB(View v) {
        displayForTeamB(2);
    }

    public void addOneForTeamB(View v) {
        displayForTeamB(1);
    }

    public void fin(View v) {
        finish();
    }

    private String formateTimer(long time){
        String str = "00:00";
//        int hour = 0;
//        if(time>=1000*3600){
//            hour = (int)(time/(1000*3600));
//            time -= hour*1000*3600;
//        }
        int minute = 0;
        if(time>=1000*60){
            minute = (int)(time/(1000*60));
            time -= minute*1000*60;
        }
        int sec = (int)(time/1000);
        str = "倒计时："+formateNumber(minute)+" 分钟 "+formateNumber(sec)+" 秒 ";
        return str;
    }

    private String formateNumber(int time){
        return String.format("%02d", time);
    }


}
