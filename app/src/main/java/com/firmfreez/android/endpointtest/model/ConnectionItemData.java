package com.firmfreez.android.endpointtest.model;

import java.util.UUID;

/**
 * Модель данных одного подключения
 */
public class ConnectionItemData {
    private UUID mId;               //Идентификатор подключения
    private String mName;           //Имя подключения
    private boolean isActive;       //Состояние активности подключения
    private int mGood;              //Счетчик количсетва успешных запросов
    private int mWarning;           //Счетчик количества предупреждений при запросах
    private int mBad;               //Счетчик количества неудачных запросов
    private RequestType mType;      //Тип подключения
    private String mServer;         //Хост
    private String mEndpoint;       //Endpoint
    private int mPort;              //Порт
    private int mServerTimeOut;     //Количество неудачных запросов, необходимых для вызова системной ошибки
    private int mConnectionTimeOut; //Интервал между запросами
    private String mStatusOK;       //Сообщение успешного запроса        (в лог)
    private String mStatusBAD;      //Сообщение неудачного запроса       (в лог)
    private String mStatusWarning;  //Сообщение предупреждающего запроса (в лог)
    private int mStatusOKColor;     //Цвет успешного запроса             (в лог)
    private int mStatusBADColor;    //Цвет неудачного запроса            (в лог)
    private int mStatusWarningColor;//Цвет предупреждающего запроса      (в лог)
    public enum RequestType {       //Возможные типы запросов
        POST,
        GET,
        PUT
    }

    public ConnectionItemData(String name) {
        mId = UUID.randomUUID();
        mName = name;
        isActive = false;
        mGood = 0;
        mBad = 0;
        mWarning = 0;
        mType = RequestType.GET;
        mServer = "https://portal.kubankredit.ru";
        mEndpoint = "/backend/rest/stateful/personal/ping";
        mPort = 0;
        mServerTimeOut = 4;
        mConnectionTimeOut = 5000;
    }

    //////////////ГЕТТЕРЫ СЕТТЕРЫ////////////////////////

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getGood() {
        return mGood;
    }

    public void setGood(int good) {
        mGood = good;
    }

    public int getWarning() {
        return mWarning;
    }

    public void setWarning(int warning) {
        mWarning = warning;
    }

    public int getBad() {
        return mBad;
    }

    public void setBad(int bad) {
        mBad = bad;
    }

    public RequestType getType() {
        return mType;
    }

    public void setType(RequestType type) {
        mType = type;
    }

    public String getServer() {
        return mServer;
    }

    public void setServer(String server) {
        mServer = server;
    }

    public String getEndpoint() {
        return mEndpoint;
    }

    public void setEndpoint(String endpoint) {
        mEndpoint = endpoint;
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int port) {
        mPort = port;
    }

    public int getServerTimeOut() {
        return mServerTimeOut;
    }

    public void setServerTimeOut(int serverTimeOut) {
        mServerTimeOut = serverTimeOut;
    }

    public int getConnectionTimeOut() {
        return mConnectionTimeOut;
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        mConnectionTimeOut = connectionTimeOut;
    }

    public String getStatusOK() {
        return mStatusOK;
    }

    public void setStatusOK(String statusOK) {
        mStatusOK = statusOK;
    }

    public String getStatusBAD() {
        return mStatusBAD;
    }

    public void setStatusBAD(String statusBAD) {
        mStatusBAD = statusBAD;
    }

    public String getStatusWarning() {
        return mStatusWarning;
    }

    public void setStatusWarning(String statusWarning) {
        mStatusWarning = statusWarning;
    }

    public int getStatusOKColor() {
        return mStatusOKColor;
    }

    public void setStatusOKColor(int statusOKColor) {
        mStatusOKColor = statusOKColor;
    }

    public int getStatusBADColor() {
        return mStatusBADColor;
    }

    public void setStatusBADColor(int statusBADColor) {
        mStatusBADColor = statusBADColor;
    }

    public int getStatusWarningColor() {
        return mStatusWarningColor;
    }

    public void setStatusWarningColor(int statusWarningColor) {
        mStatusWarningColor = statusWarningColor;
    }
}
