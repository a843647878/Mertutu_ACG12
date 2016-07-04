package com.moetutu.acg12.http.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-04-20 18:38
 */
public abstract class BaseCallBack<T> implements Callback<T> {
    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        dispatchResponse(call, response);
    }

    private void dispatchResponse(Call<T> call, Response<T> response) {
        if (response.code() == 200) {
            dispatchHttpSuccess(call, response);
        } else {
            onFailure(call, new HttpException(response));
        }
    }

    protected abstract void dispatchHttpSuccess(Call<T> call, Response<T> response);

    public abstract void onSuccess(Call<T> call, Response<T> response);

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }
}
