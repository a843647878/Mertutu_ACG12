package com.moetutu.acg12.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.dao.userinfo.DaoMaster;
import com.dao.userinfo.DaoSession;
import com.dao.userinfo.User;
import com.moetutu.acg12.constant.DbConfig;


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
        devOpenHelper = new DaoMaster.DevOpenHelper(context, DbConfig.DB_USER_LOGIN, null);
        daoSession = new DaoMaster(devOpenHelper.getWritableDatabase()).newSession();
    }

    public long inertOrReplace(User user) throws Exception {
        return daoSession.getUserDao().insertOrReplace(user);
    }

    public List<User> loadAll() throws Exception {
        return daoSession.getUserDao().loadAll();
    }

    public void delete(String uid) throws Exception {
        daoSession.getUserDao().deleteByKey(uid);
    }

    public User getLoginUser() throws Exception {
        List<User> users = loadAll();
        if (users == null || users.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                if (user == null) continue;
                if (!TextUtils.isEmpty(user.getToken())) {
                    return user;
                }
            }
            return null;
        }
    }

    public void exitLoginUser() throws Exception {
        User loginUser = getLoginUser();
        if (loginUser != null) {
            loginUser.setToken(null);
            daoSession.update(loginUser);
        }
    }

    public void close() throws Exception {
        daoSession.clear();
        if (daoSession.getDatabase() != null) {
            if (daoSession.getDatabase().isOpen()) {
                daoSession.getDatabase().close();
            }
        }
        daoSession = null;
        devOpenHelper.close();
        devOpenHelper = null;
    }
}
