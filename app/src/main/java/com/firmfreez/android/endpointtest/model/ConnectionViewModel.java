package com.firmfreez.android.endpointtest.model;

import android.content.Context;

import androidx.databinding.Bindable;

/**
 * ViewModel подключения
 */
public class ConnectionViewModel extends FragmentViewModel {
    private ConnectionItemData mData;

    /**
     * Устанавливает данные
     * @param data
     */
    public void setData(ConnectionItemData data) {
        mData = data;
        notifyChange();
    }


    public ConnectionViewModel(Context fragment) {
        super(fragment);
    }

    /**
     * Возвращает имя
     * @return
     */
    @Bindable
    public String getName() {
        return mData.getName();
    }

    /**
     * Возвращает тип запроса
     * @return
     */
    @Bindable
    public String getRequestType() {
        return mData.getType().toString() + ":";
    }

    /**
     * Возвращает адрес, куда отсылаются запросы подключения
     * @return
     */
    @Bindable
    public String getAddress() {
        String port = mData.getPort() == 0 ? "" : ":" + mData.getPort();
        return mData.getServer() + port + mData.getEndpoint();
    }

}
