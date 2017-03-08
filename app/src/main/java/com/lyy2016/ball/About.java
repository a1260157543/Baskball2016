package com.lyy2016.ball;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.lyy2016.ball.appupdate.SplashActivity;
import com.lyy2016.ball.dao.CityDao;

public class About extends Activity implements View.OnClickListener {

    LinearLayout update;
    LinearLayout btn_main_reset;
    LinearLayout tv_gs;
    private CityDao mCityDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        mCityDao = new CityDao(this);
        update = (LinearLayout) findViewById(R.id.update);
        btn_main_reset = (LinearLayout) findViewById(R.id.btn_main_reset);
        tv_gs = (LinearLayout) findViewById(R.id.tv_gs);
        tv_gs.setOnClickListener(this);
        update.setOnClickListener(this);
        btn_main_reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update:
                Intent intent = new Intent(About.this,
                        SplashActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_main_reset:
                reset();
                break;
            case R.id.tv_gs:
                Intent gs = new Intent(About.this, MainImageActivity .class);
                startActivity(gs);
                break;
            default:
                break;
        }
    }

    private void reset() {

        AlertDialog.Builder builder = new AlertDialog.Builder(About.this);
        builder.setMessage("确定要重置球员信息吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",

                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!mCityDao.isEmpty()) {
                            mCityDao.clearTable();
                            mCityDao.initTable();
                            Intent reset = new Intent(About.this,
                                    MainActivity.class);
                            startActivity(reset);
                            Toast.makeText(About.this, "重置球员信息成功", Toast.LENGTH_SHORT).show();
                        }

                    }

                });

        builder.setNegativeButton("取消",

                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }

                });

        builder.create().show();

    }

    public void fin(View v) {
        this.finish();
    }

}
