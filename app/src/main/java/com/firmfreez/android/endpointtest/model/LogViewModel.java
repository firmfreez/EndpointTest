package com.firmfreez.android.endpointtest.model;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.databinding.Bindable;

/**
 * ViewModel для Объекта списка лога
 */
public class LogViewModel extends FragmentViewModel {
    private LogItemData mData;

    public void setData(LogItemData data) {
        mData = data;
        notifyChange();
    }

    /**
     * Возвращает время сохдания объекта
     * @return String
     */
    @Bindable
    public String getTime() {
        return mData.getTime().toString();
    }

    /**
     * Возвращает цвет объекта лога
     * @return int
     */
    @ColorInt
    @Bindable
    public int getColor() {
        ConnectionItemData cData = ConnectionPull.get(getFragment()).getConnection(mData.getUUID());
        switch (mData.getStatus()) {
            case GOOD:
                return cData.getStatusOKColor();
            case BAD:
                return cData.getStatusBADColor();
            default:
                return cData.getStatusWarningColor();
        }
    }

    /**
     * Возвращает Статус-текст лога
     * @return String
     */
    @Bindable
    public String getStatus() {
        ConnectionItemData cData = ConnectionPull.get(getFragment()).getConnection(mData.getUUID());

        switch (mData.getStatus()) {
            case GOOD:
                return cData.getStatusOK();
            case BAD:
                return cData.getStatusBAD();
            default:
                return cData.getStatusWarning();
        }
    }

    public LogViewModel(Context context) {
        super(context);
    }
}
