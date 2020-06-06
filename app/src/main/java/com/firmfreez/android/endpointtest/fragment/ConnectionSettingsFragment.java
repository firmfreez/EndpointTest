package com.firmfreez.android.endpointtest.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import java.util.UUID;

import com.firmfreez.android.endpointtest.R;
import com.firmfreez.android.endpointtest.activity.ConnectionSettingsActivity;
import com.firmfreez.android.endpointtest.databinding.FragmentConnectionSettingsBinding;
import com.firmfreez.android.endpointtest.model.ConnectionItemData;
import com.firmfreez.android.endpointtest.model.ConnectionPull;
import com.firmfreez.android.endpointtest.model.ConnectionViewModel;

import petrov.kristiyan.colorpicker.ColorPicker;

/**
 * Фрагмент, отвечающий за добавление и настройку подключения
 */
public class ConnectionSettingsFragment extends
        BindingFragment<FragmentConnectionSettingsBinding, ConnectionViewModel> {
    private static final String ARG_ID = "com.firmfreez.android.endpointtest.arg_id";
    private static final String TAG = "ConnectionSettingsF";
    public static final int RESULT_DELETE = 99;
    private UUID mUUID;

    /**
     * Получение ID подключения
     * @param id ID подключения (null, если новое подключение)
     * @return ConnectionSettingsFragment
     */
    public static ConnectionSettingsFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID, id);
        ConnectionSettingsFragment fragment = new ConnectionSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Утсанавливаем видимость значека УДАЛИТЬ в зависимости от типа
        //фрагмента (Настройки или Добавление)
        mUUID = (java.util.UUID) getArguments().getSerializable(ARG_ID);
        if(mUUID != null) {
            setHasOptionsMenu(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind(inflater,container);
        //Заполнили Spinner типов подключений
        ArrayAdapter<ConnectionItemData.RequestType> adapter =
                new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,
                        ConnectionItemData.RequestType.values());
        getBinding().requestType.setAdapter(adapter);
        //Если есть mUUID - значит реадактирвоание, иначе добавление
        if (mUUID != null) {
            ConnectionItemData data = ConnectionPull.get(getContext()).getConnection(mUUID);
            getBinding().name.setText(data.getName());
            getBinding().server.setText(data.getServer());
            getBinding().port.setText(String.valueOf(data.getPort()));
            getBinding().endpoint.setText(data.getEndpoint());
            for(int i = 0; i < adapter.getCount(); i++) {
                if(adapter.getItem(i).equals(data.getType())) {
                    getBinding().requestType.setSelection(i);
                    break;
                }
            }
            getBinding().serverTimeout.setText(String.valueOf(data.getServerTimeOut()));
            getBinding().connectionTimeout.setText(String.valueOf(data.getConnectionTimeOut()));
            getBinding().resultOkText.setText(data.getStatusOK());
            getBinding().resultWarningText.setText(data.getStatusWarning());
            getBinding().resultBadText.setText(data.getStatusBAD());
            getBinding().colorOkBtn.setBackgroundColor(data.getStatusOKColor());
            getBinding().colorBadBtn.setBackgroundColor(data.getStatusBADColor());
            getBinding().colorWarningBtn.setBackgroundColor(data.getStatusWarningColor());
        }

        //Инициализируем ColorPicker - ы
        addColorPickerListener(getBinding().colorOkBtn);
        addColorPickerListener(getBinding().colorWarningBtn);
        addColorPickerListener(getBinding().colorBadBtn);

        //Кнопка ОК
        getBinding().addConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionItemData data = validateFields();
                if(data == null) {
                    return;
                }
                ConnectionPull.get(getContext()).addOrUpdate(data);
                ConnectionSettingsActivity activity = (ConnectionSettingsActivity) getActivity();
                activity.setResult(mUUID);
                activity.finish();
            }
        });

        return getBinding().getRoot();
    }

    /**
     * Инициализатор для ColorPicker - а
     * @param button кнопка, по нажатию на которую он будет вызываться
     */
    private void addColorPickerListener(final Button button) {
        button.setOnClickListener(v -> {
            ColorPicker colorPicker = new ColorPicker(getActivity());
            ColorDrawable colorDrawable = (ColorDrawable) button.getBackground();
            colorPicker.setDefaultColorButton(colorDrawable.getColor());
            colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                @Override
                public void onChooseColor(int position,int color) {
                    // put code
                    button.setBackgroundColor(color);
                }

                @Override
                public void onCancel(){
                    // put code
                }
            });
            colorPicker.show();
        });

    }

    /**
     * Следит, чтобы поля были заполнены корректно и возвращает
     * заполенный объект, если успешно
     * @return ConnectionItemData
     */
    private ConnectionItemData validateFields() {
        String server = getBinding().server.getText().toString();
        if(server.isEmpty()) {
            Toast.makeText(getContext(),"Server is required", Toast.LENGTH_SHORT).show();
            return null;
        }
        String port = getBinding().port.getText().toString();
        String endpoint = getBinding().endpoint.getText().toString();
        if(endpoint.isEmpty()) {
            Toast.makeText(getContext(),"Endpoint is required", Toast.LENGTH_SHORT).show();
            return null;
        }
        String serverTimeout = getBinding().serverTimeout.getText().toString();
        if(serverTimeout.isEmpty()) {
            Toast.makeText(getContext(),"Server Timeout is required", Toast.LENGTH_SHORT).show();
            return null;
        }
        String connectionTimeout = getBinding().connectionTimeout.getText().toString();
        if(connectionTimeout.isEmpty()) {
            Toast.makeText(getContext(),"Connection Timeout is required", Toast.LENGTH_SHORT).show();
            return null;
        }
        String name = getBinding().name.getText().toString();
        if(name.isEmpty()) {
            name = getString(R.string.default_name);
        }
        String request_type = getBinding().requestType.getSelectedItem().toString();
        String resultOK = getBinding().resultOkText.getText().toString();
        if(resultOK.isEmpty()) {
            resultOK = getString(R.string.result_ok_default);
        }
        String resultBAD = getBinding().resultBadText.getText().toString();
        if(resultBAD.isEmpty()) {
            resultBAD = getString(R.string.result_bad_default);
        }
        String resultWARN = getBinding().resultWarningText.getText().toString();
        if(resultWARN.isEmpty()) {
            resultWARN = getString(R.string.result_warning_default);
        }


        ConnectionItemData data = mUUID == null ? new ConnectionItemData(name) : ConnectionPull.get(getContext()).getConnection(mUUID);
        data.setName(name);
        data.setServer(server);
        data.setPort(port.isEmpty() ? 0 : Integer.parseInt(port));
        data.setEndpoint(endpoint);
        data.setType(ConnectionItemData.RequestType.valueOf(request_type));
        data.setStatusOK(resultOK);
        data.setStatusWarning(resultWARN);
        data.setStatusBAD(resultBAD);
        data.setServerTimeOut(Integer.parseInt(serverTimeout));
        data.setConnectionTimeOut(Integer.parseInt(connectionTimeout));
        ColorDrawable colorDrawable = (ColorDrawable) getBinding().colorOkBtn.getBackground();
        data.setStatusOKColor(colorDrawable.getColor());
        colorDrawable = (ColorDrawable) getBinding().colorWarningBtn.getBackground();
        data.setStatusWarningColor(colorDrawable.getColor());
        colorDrawable = (ColorDrawable) getBinding().colorBadBtn.getBackground();
        data.setStatusBADColor(colorDrawable.getColor());

        return data;
    }

    /**
     * Настройки меню
     * Удаление подключения
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_connection_settings_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_connection:
                ConnectionPull.get(getContext()).removeConnection(mUUID);
                getActivity().setResult(RESULT_DELETE);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Настройка ViewModel
     * @return
     */
    @Override
    public ConnectionViewModel onCreate() {
        ConnectionViewModel viewModel = new ConnectionViewModel(getActivity());
        viewModel.setData(new ConnectionItemData(""));
        return viewModel;
    }

    @Override
    public int getVariable() {
        return BR.ViewModel;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_connection_settings;
    }
}
