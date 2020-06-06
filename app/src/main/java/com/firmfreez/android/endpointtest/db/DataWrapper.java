package com.firmfreez.android.endpointtest.db;

import android.database.Cursor;
import android.database.CursorWrapper;
import java.util.UUID;

import com.firmfreez.android.endpointtest.db.DbSchema.DataTable.Cols;
import com.firmfreez.android.endpointtest.model.ConnectionItemData;

/**
 * CursorWrapper для удобного преобразования записей таблицы в объект подключения
 */
public class DataWrapper extends CursorWrapper {
    public DataWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * Составляет объект ConnectionItemData из курсора
     * @return
     */
    public ConnectionItemData getData() {
        ConnectionItemData data = new ConnectionItemData("");
        UUID id = UUID.fromString(getString(getColumnIndex(Cols.id)));
        String name = getString(getColumnIndex(Cols.Name));
        boolean isActive = Boolean.parseBoolean(getString(getColumnIndex(Cols.isActive)));
        int Good = getInt(getColumnIndex(Cols.Good));
        int Bad = getInt(getColumnIndex(Cols.Bad));
        int Warning = getInt(getColumnIndex(Cols.Warning));
        ConnectionItemData.RequestType Type = ConnectionItemData.RequestType
                .valueOf(getString(getColumnIndex(Cols.RequestType)));
        String Server = getString(getColumnIndex(Cols.Server));
        int Port = getInt(getColumnIndex(Cols.Port));
        String Endpoint = getString(getColumnIndex(Cols.Endpoint));
        int ServerTimeout = getInt(getColumnIndex(Cols.ServerTimeout));
        int ConnectionTimeout = getInt(getColumnIndex(Cols.ConnectionTimeout));
        String StatusOK = getString(getColumnIndex(Cols.StatusOK));
        String StatusWarning = getString(getColumnIndex(Cols.StatusWarning));
        String StatusBAD = getString(getColumnIndex(Cols.StatusBAD));
        int OkColor = getInt(getColumnIndex(Cols.ColorOK));
        int BadColor = getInt(getColumnIndex(Cols.ColorBAD));
        int WarnColor = getInt(getColumnIndex(Cols.ColorWarning));

        data.setId(id);
        data.setName(name);
        data.setActive(isActive);
        data.setGood(Good);
        data.setBad(Bad);
        data.setWarning(Warning);
        data.setType(Type);
        data.setServer(Server);
        data.setPort(Port);
        data.setEndpoint(Endpoint);
        data.setServerTimeOut(ServerTimeout);
        data.setConnectionTimeOut(ConnectionTimeout);
        data.setStatusOK(StatusOK);
        data.setStatusWarning(StatusWarning);
        data.setStatusBAD(StatusBAD);
        data.setStatusOKColor(OkColor);
        data.setStatusBADColor(BadColor);
        data.setStatusWarningColor(WarnColor);
        return data;
    }
}
