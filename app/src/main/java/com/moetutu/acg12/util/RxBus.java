package com.moetutu.acg12.util;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Description
 * Created by chengwanying on 2017/6/3.
 * Company BeiJing kaimijiaoyu
 * 基于 RxJava 简单的 EventBus
 */

public class RxBus {
    private static volatile RxBus mDefaultInstance;
    private RxBus() {
    }
    public static RxBus getDefault() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }
    PublishSubject<EventObject> publishSubject = PublishSubject.create();
    private final Subject<EventObject, EventObject> _bus = new SerializedSubject<>(publishSubject);
    public void send(String tag, Object object) {
        EventObject eventObject = new EventObject(tag, object);
        _bus.onNext(eventObject);
    }
    public interface ReceiveOnUiThread {
        void OnReceive(String tag, Object object);
    }
    public interface ReceiveOnIOThread {
        void OnReceive(String tag, Object object);
    }
    public void registerOnUiThread(final ReceiveOnUiThread receiveOnUiThread) {
        _bus.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EventObject>() {
                    @Override
                    public void call(EventObject eventObject) {
                        receiveOnUiThread.OnReceive(eventObject.getTag(), eventObject.getObject());
                    }
                });
    }
    public void registerOnIOThread(final ReceiveOnIOThread receiveOnIOThread) {
        _bus.observeOn(Schedulers.io())
                .subscribe(new Action1<EventObject>() {
                    @Override
                    public void call(EventObject eventObject) {
                        receiveOnIOThread.OnReceive(eventObject.getTag(), eventObject.getObject());
                    }
                });
    }
    private static class EventObject extends Object {
        private String tag;
        private Object object;
        public EventObject(String tag, Object object) {
            this.tag = tag;
            this.object = object;
        }
        public String getTag() {
            return tag;
        }
        public Object getObject() {
            return object;
        }
    }
}
