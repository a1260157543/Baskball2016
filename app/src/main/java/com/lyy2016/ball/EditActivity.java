package com.lyy2016.ball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy2016.ball.bean.CityBean;
import com.lyy2016.ball.dao.CityDao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends Activity implements View.OnClickListener {

    private CityDao mCityDao;

    private EditText et_main_id;
    private EditText et_main_province;
    private EditText et_main_city;
    private EditText et_main_district;
    private EditText et_main_date;
    private EditText et_main_qd;
    private EditText et_main_gm;
    private EditText et_main_tl;
    private EditText et_main_sf;
    private EditText et_main_fq;
    private EditText et_main_sw;
    private EditText et_main_fg;
    TextView btn_main_update;
    String id;
    String province;
    String city;
    String district;
    String date;
    String qd;
    String gm;
    String tl;
    String sf;
    String fq;
    String sw;
    String fg;
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
    Date curDate = new Date(System.currentTimeMillis());
    String curTime = format.format(curDate);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mCityDao = new CityDao(this);
        initView();
    }

    private void initView() {
        et_main_id = (EditText) findViewById(R.id.et_main_id);
        et_main_province = (EditText) findViewById(R.id.et_main_province);
        et_main_city = (EditText) findViewById(R.id.et_main_city);
        et_main_district = (EditText) findViewById(R.id.et_main_district);
        et_main_date = (EditText) findViewById(R.id.et_main_date);
        et_main_qd = (EditText) findViewById(R.id.et_main_qd);
        et_main_gm = (EditText) findViewById(R.id.et_main_gm);
        et_main_tl = (EditText) findViewById(R.id.et_main_tl);
        et_main_sf = (EditText) findViewById(R.id.et_main_sf);
        et_main_fq = (EditText) findViewById(R.id.et_main_fq);
        et_main_sw = (EditText) findViewById(R.id.et_main_sw);
        et_main_fg = (EditText) findViewById(R.id.et_main_fg);
        Intent intent = getIntent();
        et_main_id.setText(intent.getStringExtra("id"));
        et_main_province.setText(intent.getStringExtra("province"));
        et_main_city.setText(intent.getStringExtra("city"));
        et_main_district.setText(intent.getStringExtra("district"));
        et_main_date.setText(intent.getStringExtra("date"));
        et_main_qd.setText(intent.getStringExtra("qd"));
        et_main_gm.setText(intent.getStringExtra("gm"));
        et_main_tl.setText(intent.getStringExtra("tl"));
        et_main_sf.setText(intent.getStringExtra("sf"));
        et_main_fq.setText(intent.getStringExtra("fq"));
        et_main_sw.setText(intent.getStringExtra("sw"));
        et_main_fg.setText(intent.getStringExtra("fg"));

        btn_main_update = (TextView) findViewById(R.id.btn_main_update);
        btn_main_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_update:
                    update();
                break;
            default:
                break;
        }
    }


    private void update() {
        id = et_main_id.getText().toString();
        province = et_main_province.getText().toString();
        city = et_main_city.getText().toString();
        district = et_main_district.getText().toString();
        date = et_main_date.getText().toString();
        qd = et_main_qd.getText().toString();
        gm = et_main_gm.getText().toString();
        tl = et_main_tl.getText().toString();
        sf = et_main_sf.getText().toString();
        fq = et_main_fq.getText().toString();
        sw = et_main_sw.getText().toString();
        fg = et_main_fg.getText().toString();

        if(TextUtils.isEmpty(city)){
            city = "0";
        }
        if(TextUtils.isEmpty(district)){
            district = "0";
        }
        if(TextUtils.isEmpty(date)){
            date = "0";
        }
        if(TextUtils.isEmpty(qd)){
            qd = "0";
        }
        if(TextUtils.isEmpty(gm)){
            gm = "0";
        }
        if(TextUtils.isEmpty(tl)){
            tl = "0";
        }
        if(TextUtils.isEmpty(sf)){
            sf = "0";
        }
        if(TextUtils.isEmpty(fq)){
            fq = "0";
        }
        if(TextUtils.isEmpty(sw)){
            sw = "0";
        }
        if(TextUtils.isEmpty(fg)){
            fg = "0";
        }

        if (TextUtils.isEmpty(province)) {
            Toast.makeText(this, "球员信息不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.isEmpty(province) | !TextUtils.isEmpty(city)
                    | !TextUtils.isEmpty(district) | !TextUtils.isEmpty(date) | !TextUtils.isEmpty(qd) | !TextUtils.isEmpty(gm)
                    | !TextUtils.isEmpty(tl)| !TextUtils.isEmpty(sf)| !TextUtils.isEmpty(fq)| !TextUtils.isEmpty(sw)| !TextUtils.isEmpty(fg)) {
                CityBean cityBean = new CityBean();
                cityBean.setId(id);
                cityBean.setProvince(province);
                cityBean.setCity(city);
                cityBean.setDistrict(district);
                cityBean.setDate(date);
                cityBean.setQd(qd);
                cityBean.setGm(gm);
                cityBean.setTl(tl);
                cityBean.setSf(sf);
                cityBean.setFq(fq);
                cityBean.setSw(sw);
                cityBean.setFg(fg);
                cityBean.setTime(curTime);

                if (mCityDao.update(cityBean)) {
                    Toast.makeText(this, "修改球员数据成功", Toast.LENGTH_SHORT).show();
                    this.finish();
                    Intent intent = new Intent(EditActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "修改球员数据失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "请正确填写需要修改的数据", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void fin(View v) {
        this.finish();
    }

}
