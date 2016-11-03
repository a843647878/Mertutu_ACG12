package com.moetutu.acg12.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/6/10
 * version
 */
public class DbConfig {
    //登录信息表
    public static final String DB_USER_LOGIN = "moetutu_db_user_login";

    //节目 浏览状态数据
    public static final String db_program_lookstate = "ProgramLookState_%s.realm";

    //下载 保存数据库
    public static final String DB_DOWNLOAD_COURSE = "download_course_%s.realm";


    /*
    课程音频下载
     */
    public final static String dbName = "downLesson.db";
    public final static String tableName = "LessonTable";
    public final static String tableNameKEY = "LessonKeyTable";


    public static final int TYPE_VIDEO = 0;//视频
    public static final int TYPE_AUDIO = 1;//音频

    @IntDef({TYPE_VIDEO, TYPE_AUDIO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RESOURCE_TYPE {
    }

    // 用户输入历史数据库
    public static final String DB_USER_INPUT_HISOTRY = "tcmooc_db_user_input_hisotry_%s.realm";

    // 用户最近选择的城市
    public static final String DB_USER_RECRENT_CITYS = "db_user_recent_click_citys_%s.realm";


    // msg 消息列表
    public static final String DB_USER_NEWS_LISTS = "tcmooc_db_user_news_%s.db";
}
