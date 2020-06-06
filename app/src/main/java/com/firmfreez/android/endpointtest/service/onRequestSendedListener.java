package com.firmfreez.android.endpointtest.service;

import com.firmfreez.android.endpointtest.model.LogItemData;

/**
 * Интерфейс слушателя для взаимодействия потока и GUI
 */
public interface onRequestSendedListener {
    void onSended(LogItemData logData);
}
