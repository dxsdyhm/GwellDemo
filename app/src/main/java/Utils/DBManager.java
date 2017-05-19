package Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import entity.AlarmInfo;
import entity.AlarmInfoDao;
import entity.DaoMaster;
import entity.DaoSession;
import entity.LogInfo;
import entity.LogInfoDao;

/**
 * Created by USER on 2017/4/26.
 */

public class DBManager {
    private final static String dbName = "test_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }


    public void insertAlarmInfo(AlarmInfo info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AlarmInfoDao userDao = daoSession.getAlarmInfoDao();
        userDao.insert(info);
    }

    public void insertLogInfo(LogInfo info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LogInfoDao logInfoDao=daoSession.getLogInfoDao();
        logInfoDao.insert(info);
    }

    /**
     * 查询报警列表
     */
    public List<AlarmInfo> queryAlarmInfoList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AlarmInfoDao userDao = daoSession.getAlarmInfoDao();
        QueryBuilder<AlarmInfo> qb = userDao.queryBuilder();
        List<AlarmInfo> list = qb.list();
        return list;
    }

}
