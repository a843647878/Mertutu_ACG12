package com.moetutu.acg12.interf;

/**
 * Description
 * Company Beijing guokeyuzhou
 * author  chengwanying  E-mail:wanyingandroid@163.com
 * date createTime：16/10/28
 * version
 */
public interface OnUpdateDialogNoticeListener {

    void shouldUpdate(boolean isForce);//应该更新

    void shouldShutDown(String msg);//应该停止一切活动
}
