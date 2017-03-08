package com.lyy2016.ball.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.lyy2016.ball.bean.CityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 2016/5/17 09:43.
 */
public class CityDao {
    private static final String TAG = "CityDao";

    private Context mContext;
    private CityDBHelper mCityDBHelper;
    private SQLiteDatabase db;

    private final String[] TABLE_COLUMNS = new String[]{"id", "province", "city", "district", "date", "qd", "gm", "tl", "sf", "fq", "sw", "fg", "time"};

    public CityDao(Context context) {
        mContext = context;
        mCityDBHelper = CityDBHelper.getInstance(context);
    }

    /**
     * 初始化表格
     */
    public void initTable() {
        try {
            db = mCityDBHelper.getWritableDatabase();
            db.beginTransaction();

            String[] provinces = new String[]{
                    "下铺"
            };
            String[] citys = new String[]{
                    "8"
            };
            String[] districts = new String[]{
                    "5"
            };

            String[] date = new String[]{
                    "1"
            };
            String[] qd = new String[]{
                    "3"
            };
            String[] gm = new String[]{
                    "0"
            };
            String[] tl = new String[]{
                    "8"
            };
            String[] sf = new String[]{
                    "5"
            };

            String[] fq = new String[]{
                    "1"
            };
            String[] sw = new String[]{
                    "3"
            };
            String[] fg = new String[]{
                    "0"
            };
            String[] time = new String[]{
                    "2016年10月25日19:10:58"
            };

            for (int i = 0; i < 1; i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", String.valueOf(i + 1));
                contentValues.put("province", provinces[i]);
                contentValues.put("city", citys[i]);
                contentValues.put("district", districts[i]);
                contentValues.put("date", date[i]);
                contentValues.put("qd", qd[i]);
                contentValues.put("gm", gm[i]);
                contentValues.put("tl", tl[i]);
                contentValues.put("sf", sf[i]);
                contentValues.put("fq", fq[i]);
                contentValues.put("sw", sw[i]);
                contentValues.put("fg", fg[i]);
                contentValues.put("time", time[i]);
                db.insert(CityDBHelper.TABLE_NAME, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "initTable: ", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 判断表是否为空
     *
     * @return 空为 true
     */
    public boolean isEmpty() {
        Cursor cursor = null;

        try {
            db = mCityDBHelper.getReadableDatabase();
            cursor = db.query(CityDBHelper.TABLE_NAME,
                    new String[]{"count(id)"},
                    null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) > 0) {
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "isEmpty: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return true;
    }

    /**
     * 清空数据库表
     *
     * @return boolean
     */
    public boolean clearTable() {
        try {
            db = mCityDBHelper.getWritableDatabase();
            db.delete(CityDBHelper.TABLE_NAME, null, null);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "clearTable: ", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return false;
    }

    /**
     * 查询所有数据
     *
     * @return list
     */
    public List<CityBean> selectAll() {
        Cursor cursor = null;

        try {
            db = mCityDBHelper.getReadableDatabase();
            cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS, null,null,null,null,null);

            if (cursor.getCount() > 0) {
                List<CityBean> list = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    list.add(parserCityBean(cursor));
                }

                return list;
            }
        } catch (Exception e) {
            Log.e(TAG, "selectAll: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 向表中添加一条数据
     *
     * @param cityBean 城市信息实体类
     */
    public boolean insertData(CityBean cityBean) {
        try {
            db = mCityDBHelper.getWritableDatabase();
            db.beginTransaction();

            // 向表中插入一条数据
            // insert into TABLE_NAME (id, province, city, district)
            // values (cityBean.getId(), cityBean.getProvince(),
            // cityBean.getCity(), cityBean.getDistrict())
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", Integer.parseInt(cityBean.getId()));
            contentValues.put("province", cityBean.getProvince());
            contentValues.put("city", cityBean.getCity());
            contentValues.put("district", cityBean.getDistrict());
            contentValues.put("date", cityBean.getDate());
            contentValues.put("qd", cityBean.getQd());
            contentValues.put("gm", cityBean.getGm());
            contentValues.put("tl", cityBean.getTl());
            contentValues.put("sf", cityBean.getSf());
            contentValues.put("fq", cityBean.getFq());
            contentValues.put("sw", cityBean.getSw());
            contentValues.put("fg", cityBean.getFg());
            contentValues.put("time", cityBean.getTime());
            db.insertOrThrow(CityDBHelper.TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();

            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(mContext, "球号信息重复", Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    /**
     * 从表中删除一条数据
     *
     * @param id 数据 id
     */
    public boolean delete(String id) {
        try {
            db = mCityDBHelper.getWritableDatabase();
            db.beginTransaction();

            // 删除一条 id = id 数据
            // delete TABLE_NAME where id = id
            db.delete(CityDBHelper.TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});

            db.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Log.e(TAG, "delete: ", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    /**
     * 修改表中的数据
     *
     * @param cityBean 城市信息实体类
     *
     * @return 修改成功返回 true
     */
    public boolean update(CityBean cityBean) {
        if (cityBean == null) {
            return false;
        }

        try {
            db = mCityDBHelper.getWritableDatabase();
            db.beginTransaction();

            // 修改一条数据
            // update TABLE_NAME set key = value where id = cityBean.getId()
            ContentValues contentValues = new ContentValues();

            if (!TextUtils.isEmpty(cityBean.getProvince())) {
                contentValues.put("province", cityBean.getProvince());
            }

            if (!TextUtils.isEmpty(cityBean.getCity())) {
                contentValues.put("city", cityBean.getCity());
            }

            if (!TextUtils.isEmpty(cityBean.getDistrict())) {
                contentValues.put("district", cityBean.getDistrict());
            }

            if (!TextUtils.isEmpty(cityBean.getDate())) {
                contentValues.put("date", cityBean.getDate());
            }
            if (!TextUtils.isEmpty(cityBean.getQd())) {
                contentValues.put("qd", cityBean.getQd());
            }
            if (!TextUtils.isEmpty(cityBean.getGm())) {
                contentValues.put("gm", cityBean.getGm());
            }

            if (!TextUtils.isEmpty(cityBean.getTl())) {
                contentValues.put("tl", cityBean.getTl());
            }

            if (!TextUtils.isEmpty(cityBean.getSf())) {
                contentValues.put("sf", cityBean.getSf());
            }

            if (!TextUtils.isEmpty(cityBean.getFq())) {
                contentValues.put("fq", cityBean.getFq());
            }
            if (!TextUtils.isEmpty(cityBean.getSw())) {
                contentValues.put("sw", cityBean.getSw());
            }
            if (!TextUtils.isEmpty(cityBean.getFg())) {
                contentValues.put("fg", cityBean.getFg());
            }
            if (!TextUtils.isEmpty(cityBean.getTime())) {
                contentValues.put("time", cityBean.getTime());
            }
            db.update(CityDBHelper.TABLE_NAME, contentValues, "id = ?",
                    new String[]{String.valueOf(cityBean.getId())});

            db.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Log.e(TAG, "update: ", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    /**
     * 根据条件查询数据
     *
     * @param cityBean 天气实体类
     *
     * @return 满足条件的实体类 List
     */
    public List<CityBean> select(CityBean cityBean) {
        Cursor cursor = null;

        try {
            Log.i(TAG, "select: ");

            db = mCityDBHelper.getReadableDatabase();

            if (!TextUtils.isEmpty(cityBean.getId())) {
                Log.i(TAG, "select: id" + cityBean.getId());

                // select * from TABLE_NAME where id = cityBean.getId()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "id = ?", new String[]{cityBean.getId()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getProvince())) {
                Log.i(TAG, "select: province" + cityBean.getProvince());

                // select * from TABLE_NAME where province = cityBean.getProvince()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "province = ?", new String[]{cityBean.getProvince()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getCity())) {
                Log.i(TAG, "select: city" + cityBean.getCity());

                // select * from TABLE_NAME where city = cityBean.getCity()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "city = ?", new String[]{cityBean.getCity()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getDistrict())) {
                Log.i(TAG, "select: district" + cityBean.getDistrict());

                // select * from TABLE_NAME where district = cityBean.getDistrict()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "district = ?", new String[]{cityBean.getDistrict()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getDate())) {
                Log.i(TAG, "select: date" + cityBean.getDate());

                // select * from TABLE_NAME where district = cityBean.getDistrict()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "date = ?", new String[]{cityBean.getDate()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getQd())) {
                Log.i(TAG, "select: qd" + cityBean.getQd());

                // select * from TABLE_NAME where district = cityBean.getDistrict()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "qd = ?", new String[]{cityBean.getQd()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getGm())) {
                Log.i(TAG, "select: gm" + cityBean.getGm());

                // select * from TABLE_NAME where city = cityBean.getCity()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "gm = ?", new String[]{cityBean.getGm()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getTl())) {
                Log.i(TAG, "select: tl" + cityBean.getTl());

                // select * from TABLE_NAME where city = cityBean.getCity()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "tl = ?", new String[]{cityBean.getTl()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getSf())) {
                Log.i(TAG, "select: sf" + cityBean.getSf());

                // select * from TABLE_NAME where city = cityBean.getCity()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "sf = ?", new String[]{cityBean.getSf()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getFq())) {
                Log.i(TAG, "select: fq" + cityBean.getFq());

                // select * from TABLE_NAME where city = cityBean.getCity()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "fq = ?", new String[]{cityBean.getFq()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getSw())) {
                Log.i(TAG, "select: sw" + cityBean.getSw());

                // select * from TABLE_NAME where city = cityBean.getCity()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "sw = ?", new String[]{cityBean.getSw()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getFg())) {
                Log.i(TAG, "select: fg" + cityBean.getFg());

                // select * from TABLE_NAME where city = cityBean.getCity()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "fg = ?", new String[]{cityBean.getFg()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

            if (!TextUtils.isEmpty(cityBean.getTime())) {
                Log.i(TAG, "select: time" + cityBean.getTime());

                // select * from TABLE_NAME where city = cityBean.getCity()
                cursor = db.query(CityDBHelper.TABLE_NAME, TABLE_COLUMNS,
                        "time = ?", new String[]{cityBean.getTime()}, null, null, null);

                if (cursor.getCount() > 0) {
                    List<CityBean> list = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        list.add(parserCityBean(cursor));
                    }

                    return list;
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "select: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 将查询到的 Cursor 转成城市信息实体类
     *
     * @param cursor 查询到的 Cursor
     *
     * @return CityBean 城市信息实体类
     */
    public CityBean parserCityBean(Cursor cursor) {
        CityBean cityBean = new CityBean();
        cityBean.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
        cityBean.setProvince(cursor.getString(cursor.getColumnIndex("province")));
        cityBean.setCity(cursor.getString(cursor.getColumnIndex("city")));
        cityBean.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
        cityBean.setDate(cursor.getString(cursor.getColumnIndex("date")));
        cityBean.setQd(cursor.getString(cursor.getColumnIndex("qd")));
        cityBean.setGm(cursor.getString(cursor.getColumnIndex("gm")));
        cityBean.setTl(cursor.getString(cursor.getColumnIndex("tl")));
        cityBean.setSf(cursor.getString(cursor.getColumnIndex("sf")));
        cityBean.setFq(cursor.getString(cursor.getColumnIndex("fq")));
        cityBean.setSw(cursor.getString(cursor.getColumnIndex("sw")));
        cityBean.setFg(cursor.getString(cursor.getColumnIndex("fg")));
        cityBean.setTime(cursor.getString(cursor.getColumnIndex("time")));
        return cityBean;
    }
}
