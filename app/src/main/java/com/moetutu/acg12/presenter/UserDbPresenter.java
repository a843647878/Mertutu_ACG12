package com.moetutu.acg12.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.dao.userinfo.DaoMaster;
import com.dao.userinfo.DaoSession;
import com.dao.userinfo.UserEntityDao;
import com.moetutu.acg12.constant.DbConfig;
import com.moetutu.acg12.entity.UserEntity;


import org.greenrobot.greendao.database.Database;

import java.util.List;

/**
 * Description
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/7/3
 * version
 */
public class UserDbPresenter {

    private DaoSession daoSession;

    DaoMaster.DevOpenHelper devOpenHelper;

    public UserDbPresenter(Context context) {
        devOpenHelper = new DaoMaster.DevOpenHelper(context, UserEntityDao.TABLENAME, null);
        daoSession = new DaoMaster(devOpenHelper.getWritableDb()).newSession();
    }



    public long inertOrReplace(UserEntity user) throws Exception {
        return daoSession.getUserEntityDao().insertOrReplace(user);
    }

    public List<UserEntity> loadAll() throws Exception {
        return daoSession.getUserEntityDao().loadAll();
    }

    public void update(UserEntity user) throws Exception {
        daoSession.getUserEntityDao().update(user);
    }

    public void delete(long id) throws Exception {
        daoSession.getUserEntityDao().deleteByKey(id);
    }

    public UserEntity getLoginUser() throws Exception {
        List<UserEntity> users = loadAll();
        if (users == null || users.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < users.size(); i++) {
                UserEntity user = users.get(i);
                if (user == null) continue;
                if (!TextUtils.isEmpty(user.getToken())) {
                    return user;
                }
            }
            return null;
        }
    }

    public void exitLoginUser() throws Exception {
        UserEntity loginUser = getLoginUser();
        if (loginUser != null) {
            loginUser.setToken(null);
            daoSession.update(loginUser);
        }
    }

    public void close() throws Exception {
        daoSession.clear();
        if (daoSession.getDatabase() != null) {
            if (daoSession.getDatabase().inTransaction()) {
                daoSession.getDatabase().close();
            }
        }
        daoSession = null;
        devOpenHelper.close();
        devOpenHelper = null;
    }
}
