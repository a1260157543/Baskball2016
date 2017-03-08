package com.lyy2016.ball;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllActivity extends Activity implements View.OnClickListener {

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
    private TextView tv_js;
    private TextView tv_sx;
    private TextView about;
    private TextView time;
    private TextView tv_gs;
    TextView btn_main_add;
    Button btn_main_delete;
    Button btn_main_update;
    TextView btn_main_select;
    private TextView tv_main_msg;
    ListView lv_main;
    String id;
    String province;
    String city;
    String district;
    String date;
    String qd;
    String gm;
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
    Date curDate = new Date(System.currentTimeMillis());
    String curTime = format.format(curDate);

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:
                    Button btn = (Button) msg.obj;
                    btn.setEnabled(true);
                    break;

                default:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        mCityDao = new CityDao(this);

        if (mCityDao.isEmpty()) {
            mCityDao.initTable();
        }

        initView();
        //CityBean cityBean = new CityBean();
        //Toast.makeText(MainActivity.this, "欢迎  到来！", Toast.LENGTH_SHORT).show();
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
        tv_js = (TextView) findViewById(R.id.tv_js);
        tv_sx = (TextView) findViewById(R.id.tv_sx);
        about = (TextView) findViewById(R.id.about);
        time = (TextView) findViewById(R.id.time);
        tv_gs = (TextView) findViewById(R.id.tv_gs);
        time.setText(curTime);
        btn_main_add = (TextView) findViewById(R.id.btn_main_add);
        btn_main_delete = (Button) findViewById(R.id.btn_main_delete);
        btn_main_update = (Button) findViewById(R.id.btn_main_update);
        btn_main_select = (TextView) findViewById(R.id.btn_main_select);
        tv_main_msg = (TextView) findViewById(R.id.tv_main_msg);
        lv_main = (ListView) findViewById(R.id.lv_main);

        mCityAdapter = new CityAdapter();
        lv_main.setAdapter(mCityAdapter);

        btn_main_add.setOnClickListener(this);
        btn_main_delete.setOnClickListener(this);
        btn_main_update.setOnClickListener(this);
        btn_main_select.setOnClickListener(this);
        tv_gs.setOnClickListener(this);
        tv_js.setOnClickListener(this);
        tv_sx.setOnClickListener(this);
        about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_add:
                //insert();
                Intent add = new Intent(AllActivity.this, AddActivity.class);
                startActivity(add);
                break;

            case R.id.btn_main_delete:
                btn_main_delete.setEnabled(false);
                delete();
                break;

            case R.id.btn_main_update:
                break;

            case R.id.btn_main_select:
                Intent search = new Intent(AllActivity.this, SearchActivity.class);
                startActivity(search);
                break;

            case R.id.tv_gs:
                Intent gs = new Intent(AllActivity.this, MainImageActivity .class);
                startActivity(gs);
                break;
            case R.id.tv_sx:
                refreshList();
                break;
            case R.id.tv_js:
                Intent intent2 = new Intent(AllActivity.this,
                        Count.class);
                startActivity(intent2);
                break;
            case R.id.about:
                Intent intent = new Intent(AllActivity.this,
                        About.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }



    private void delete() {
        id = et_main_id.getText().toString();
        Message message = new Message();
        message.what = 0x123;
        message.obj = btn_main_delete;

//        if (!TextUtils.isEmpty(id) && TextUtils.isEmpty(province)
//                && TextUtils.isEmpty(city) && TextUtils.isEmpty(district) && TextUtils.isEmpty(date) && TextUtils.isEmpty(qd)) {
        if (!TextUtils.isEmpty(id)) {
            if (mCityDao.delete(id)) {
                String msg = "删除一条数据\n" +
                        "delete form" + CityDBHelper.TABLE_NAME + "where id = " + id;
                tv_main_msg.setText(msg);

                refreshList();

                mHandler.sendMessageDelayed(message, 3000);
                Toast.makeText(this, "删除球员数据成功", Toast.LENGTH_SHORT).show();
            } else {
                mHandler.sendMessage(message);
                btn_main_delete.setEnabled(true);
                Toast.makeText(this, "删除球员数据失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            btn_main_delete.setEnabled(true);
            Toast.makeText(this, "球号信息不能为空", Toast.LENGTH_SHORT).show();
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
                convertView = LayoutInflater.from(AllActivity.this).inflate(R.layout.item_lv_main, null);

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
            ImageView img = new ImageView(AllActivity.this);
            img.setImageResource(R.mipmap.ic_launcher);
            CityBean cityBean = list.get(position);
            new AlertDialog.Builder(AllActivity.this)
                    .setTitle("球员基本信息")
                    .setMessage("球号："+cityBean.getId()+"\n球员："+cityBean.getProvince()
                            +"\n得分："+cityBean.getCity()+"\n篮板："+cityBean.getDistrict()
                            +"\n助攻："+cityBean.getDate()+"\n抢断："+cityBean.getQd()
                            +"\n盖帽："+cityBean.getGm()+"\n投篮："+cityBean.getTl()
                            +"\n三分："+cityBean.getSf() +"\n罚球："+cityBean.getFq()
                            +"\n失误："+cityBean.getSw() +"\n犯规："+cityBean.getFg()
                            +"\n更新时间："+cityBean.getTime())
                    .setNegativeButton("删除球员信息",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                CityBean cityBean = list.get(position);
                                String id = cityBean.getId().toString();
                                String name = cityBean.getProvince().toString();

                                if(list.size()>1){
                                    if (!TextUtils.isEmpty(id)) {
                                        if (mCityDao.delete(id)) {
                                            String msg = "删除一条数据\n" +
                                                    "delete form" + CityDBHelper.TABLE_NAME + "where id = " + id;
                                            tv_main_msg.setText(msg);
                                            refreshList();
                                            Toast.makeText(AllActivity.this, "删除<-"+name+"->成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(AllActivity.this, "删除<-"+name+"->失败", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(AllActivity.this, "球号信息不能为空", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(AllActivity.this, "请保留我呗，就我一个！", Toast.LENGTH_SHORT).show();
                                }

                        }
                    })
                    .setPositiveButton("修改用户信息",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CityBean cityBean = list.get(position);
                            String id = cityBean.getId().toString();
                            String province = cityBean.getProvince().toString();
                            String city = cityBean.getCity().toString();
                            String district = cityBean.getDistrict().toString();
                            String date = cityBean.getDate().toString();
                            String qd = cityBean.getQd().toString();
                            String gm = cityBean.getGm().toString();
                            String tl = cityBean.getTl().toString();
                            String sf = cityBean.getSf().toString();
                            String fq = cityBean.getFq().toString();
                            String sw = cityBean.getSw().toString();
                            String fg = cityBean.getFg().toString();
                            Intent intent = new Intent(AllActivity.this,
                                    EditActivity.class);
                            intent.putExtra("id",id);
                            intent.putExtra("province",province);
                            intent.putExtra("city",city);
                            intent.putExtra("district",district);
                            intent.putExtra("date",date);
                            intent.putExtra("qd",qd);
                            intent.putExtra("gm",gm);
                            intent.putExtra("tl",tl);
                            intent.putExtra("sf",sf);
                            intent.putExtra("fq",fq);
                            intent.putExtra("sw",sw);
                            intent.putExtra("fg",fg);
                            startActivity(intent);
                        }
                    }).show();

        }

        class ViewHolder {
            TextView tv_item_lv_main_id;
            TextView tv_item_lv_main_province;
            TextView tv_item_lv_main_city;
            TextView tv_item_lv_main_district;
            TextView tv_item_lv_main_date;
            Button btn;
        }

    }

    public void fin(View v) {
        this.finish();
    }

}
