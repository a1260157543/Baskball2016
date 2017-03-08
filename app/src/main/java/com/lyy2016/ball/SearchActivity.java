package com.lyy2016.ball;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy2016.ball.bean.CityBean;
import com.lyy2016.ball.dao.CityDBHelper;
import com.lyy2016.ball.dao.CityDao;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity implements View.OnClickListener {

    private CityDao mCityDao;
    private List<CityBean> list = new ArrayList<>();
    CityAdapter mCityAdapter;

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
    private TextView tv_sx;
    TextView btn_main_select;
    ListView lv_main;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mCityDao = new CityDao(this);

        if (mCityDao.isEmpty()) {
            mCityDao.initTable();
        }

        initView();
        refreshList();
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

        tv_sx = (TextView) findViewById(R.id.tv_sx);
        btn_main_select = (TextView) findViewById(R.id.btn_main_select);
        lv_main = (ListView) findViewById(R.id.lv_main);

        mCityAdapter = new CityAdapter();
        lv_main.setAdapter(mCityAdapter);
        btn_main_select.setOnClickListener(this);
        tv_sx.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_select:
                select();
                break;
            case R.id.tv_sx:
                refreshList();
                break;
            default:
                break;
        }
    }

    private void select() {
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

        CityBean cityBean = null;
        String msg = "";

        if (!TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setId(id);

            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where id = " + id;
        } else if (TextUtils.isEmpty(id) && !TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setProvince(province);

            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where province = " + province;
        } else if (TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && !TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setCity(city);

            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where city = " + city;
        } else if (TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setDistrict(district);

            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where district = " + district;
        } else if (TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& !TextUtils.isEmpty(date)&& TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setDate(date);

            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where date = " + date;
        } else if (TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& !TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setQd(qd);

            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where qd = " + qd;
        }else if (TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& !TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setQd(qd);

            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where qd = " + qd;
        } else if (TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && !TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setTl(tl);
            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where tl = " + tl;
        } else if (TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& !TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setFq(fq);
            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where fq = " + fq;
        }  else if (TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && !TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setSf(sf);
            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where sf = " + sf;
        }  else if (TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& !TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setSw(sw);
            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where sw = " + sw;
        }  else if (TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& !TextUtils.isEmpty(fg)) {
            cityBean = new CityBean();
            cityBean.setFg(fg);
            msg = "查询数据\n" +
                    "select * from " + CityDBHelper.TABLE_NAME + " where fg = " + fg;
        }   else if (TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district)&& TextUtils.isEmpty(date)&& TextUtils.isEmpty(qd)&& TextUtils.isEmpty(gm)
                && TextUtils.isEmpty(tl) && TextUtils.isEmpty(sf)&& TextUtils.isEmpty(fq)&& TextUtils.isEmpty(sw)&& TextUtils.isEmpty(fg)) {
            Toast.makeText(this, "请填写一个查询条件", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "只能填写一个查询条件", Toast.LENGTH_SHORT).show();
        }

        Message message = new Message();
        message.what = 0x123;
        if (cityBean != null) {
            List<CityBean> selectList = mCityDao.select(cityBean);
            if (selectList != null) {
                list.clear();
                list.addAll(selectList);
                mCityAdapter.notifyDataSetChanged();
                Toast.makeText(this, "查找球员数据成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "请正确填写查询条件", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void refreshList() {
        list.clear();
        list.addAll(mCityDao.selectAll());
        mCityAdapter.notifyDataSetChanged();
    }

    class CityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_lv_main, null);

                holder = new ViewHolder();
                holder.btn =
                        (Button) convertView.findViewById(R.id.btn);
                holder.tv_item_lv_main_id =
                        (TextView) convertView.findViewById(R.id.tv_item_lv_main_id);
                holder.tv_item_lv_main_province =
                        (TextView) convertView.findViewById(R.id.tv_item_lv_main_province);
                holder.tv_item_lv_main_city =
                        (TextView) convertView.findViewById(R.id.tv_item_lv_main_city);
                holder.tv_item_lv_main_district =
                        (TextView) convertView.findViewById(R.id.tv_item_lv_main_district);
                holder.tv_item_lv_main_date =
                        (TextView) convertView.findViewById(R.id.tv_item_lv_main_date);
//                holder.tv_item_lv_main_qd =
//                        (TextView) convertView.findViewById(R.id.tv_item_lv_main_qd);
//                holder.tv_item_lv_main_gm =
//                        (TextView) convertView.findViewById(R.id.tv_item_lv_main_gm);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CityBean cityBean = list.get(position);
            if (cityBean == null) {
                return null;
            }

            holder.tv_item_lv_main_id.setText(cityBean.getId());
            holder.tv_item_lv_main_province.setText(cityBean.getProvince());
            holder.tv_item_lv_main_city.setText(cityBean.getCity());
            holder.tv_item_lv_main_district.setText(cityBean.getDistrict());
            holder.tv_item_lv_main_date.setText(cityBean.getDate());
            //holder.tv_item_lv_main_qd.setText(cityBean.getQd());
//            holder.tv_item_lv_main_gm.setText(cityBean.getGm());

            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show(position);
                }
            });

            return convertView;
        }

        private void show(final int position) {
            ImageView img = new ImageView(SearchActivity.this);
            img.setImageResource(R.mipmap.ic_launcher);
            CityBean cityBean = list.get(position);
            new AlertDialog.Builder(SearchActivity.this)
                    .setTitle("球员基本信息")
                    .setMessage("球号："+cityBean.getId()+"\n球员："+cityBean.getProvince()
                            +"\n得分："+cityBean.getCity()+"\n篮板："+cityBean.getDistrict()
                            +"\n助攻："+cityBean.getDate()+"\n抢断："+cityBean.getQd()
                            +"\n盖帽："+cityBean.getGm()+"\n投篮："+cityBean.getTl()
                            +"\n三分："+cityBean.getSf() +"\n罚球："+cityBean.getFq()
                            +"\n失误："+cityBean.getSw() +"\n犯规："+cityBean.getFg()
                            +"\n更新时间："+cityBean.getTime()).show();
        }

        class ViewHolder {
            TextView tv_item_lv_main_id;
            TextView tv_item_lv_main_province;
            TextView tv_item_lv_main_city;
            TextView tv_item_lv_main_district;
            TextView tv_item_lv_main_date;
            Button btn;
            //TextView tv_item_lv_main_qd;
//          TextView tv_item_lv_main_gm;
        }

    }

    public void fin(View v) {
        this.finish();
    }

}
