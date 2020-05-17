package com.dmtech.iw;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.dmtech.iw.model.DaoMaster;
import com.dmtech.iw.model.DaoSession;

import org.greenrobot.greendao.database.Database;

public class iWeatherApp extends Application {
    // 数据库名字
    private static final String DB_NAME = "database";

    // GreenDao会话对象
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("iWeather", "iWeatherApp is created.");
        // 指定数据库名字（没有就创建）并建立会话对象
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME);
        Database db = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    /**
     * 获取会话对象
     * @return 会话对象
     */
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
