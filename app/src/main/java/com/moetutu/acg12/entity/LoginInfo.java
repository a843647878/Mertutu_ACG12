package com.moetutu.acg12.entity;

import com.dao.userinfo.User;

/**
 * Description
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/7/3
 * version
 */
public class LoginInfo {
    public String token;
    public User user;
    public Easemob easemob;
    public String lastApproval;
    public int hasCompleted;

    public static class Easemob {
        public String username;
        public String password;
    }

    public String ppvUrl;
}
