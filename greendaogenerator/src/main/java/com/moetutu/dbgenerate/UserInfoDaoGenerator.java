package com.moetutu.dbgenerate;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Description
 * Company Beijing acg12
 * author  cu  E-mail:wanyingandroid@163.com
 * date createTime：16/10/25
 * version
 */
public class UserInfoDaoGenerator {
    public static final int VERSION = 1;
    public static String CLASS_PATH = "com.dao.userinfo";

    public static String OUT_PATH = "D:/android/Mertutu_ACG12/app/src/main/java-gen";


    public static void main(String[] args) throws Exception {

        addNoticeSchema();
    }


    private static void addNoticeSchema() throws Exception {
        Schema schema = new Schema(VERSION, CLASS_PATH);
        Entity user = schema.addEntity("User");
        user.addStringProperty("uid").primaryKey().notNull();//目前不支持联合主键  这里选择拼凑
        user.addStringProperty("token");
        user.addStringProperty("nickname");

        new de.greenrobot.daogenerator.DaoGenerator().generateAll(schema, OUT_PATH);
    }
}
