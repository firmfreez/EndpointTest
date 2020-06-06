package com.firmfreez.android.endpointtest.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firmfreez.android.endpointtest.R;
import com.firmfreez.android.endpointtest.activity.ConnectionSettingsActivity;
import com.firmfreez.android.endpointtest.databinding.FragmentConnectionLogBinding;
import com.firmfreez.android.endpointtest.databinding.ItemLogBinding;
import com.firmfreez.android.endpointtest.model.ConnectionItemData;
import com.firmfreez.android.endpointtest.model.ConnectionPull;
import com.firmfreez.android.endpointtest.model.FragmentViewModel;
import com.firmfreez.android.endpointtest.model.LogItemData;
import com.firmfreez.android.endpointtest.model.LogViewModel;
import com.firmfreez.android.endpointtest.service.ThreadManager;
import com.firmfreez.android.endpointtest.service.onRequestSendedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConnectionLogFragment extends BindingFragment<FragmentConnectionLogBinding, FragmentViewModel> {
    private static final String EXTRA_ID = "com.firmfreez.android.endpointtest.extra_id"; //Для получения UUID
    private static final int REQUEST_SETTINGS = 0;                                        //Для вызова активити настроек
    private UUID mUUID;                                                                   //UUID подключения

    public static ConnectionLogFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ID, id);
        ConnectionLogFragment fragment = new ConnectionLogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUUID = (UUID) getArguments().getSerializable(EXTRA_ID);
        //Создание и присоединение Handler и Listener к потоку
        setupThread();
    }

    /**
     * Возобновление адаптера
     */
    @Override
    public void onResume() {
        super.onResume();
        getBinding().logList.setAdapter(new LogAdapter(ConnectionPull.get(getContext()).getLogList(mUUID)));
        setupThread();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind(inflater,container);
        getBinding().logList.setLayoutManager(new LinearLayoutManager(getContext()));
        getBinding().logList.setAdapter(new LogAdapter(ConnectionPull.get(getContext()).getLogList(mUUID)));

        return getBinding().getRoot();
    }

    /**
     * ViewHolder для отображения одного объекта на RecyclerView
     */
    private class LogHolder extends RecyclerView.ViewHolder {
        private ItemLogBinding mBinding;
        private Context mContext;

        public LogHolder(ItemLogBinding binding, Context context) {
            super(binding.getRoot());
            mBinding = binding;
            mContext = context;
        }

        public void bindData(LogItemData data) {
            if(mBinding.getViewModel() == null) {
                LogViewModel viewModel = new LogViewModel(mContext);
                mBinding.setViewModel(viewModel);
            }
            mBinding.getViewModel().setData(data);
        }
    }

    /**
     * Adapter для RecyclerView
     */
    private class LogAdapter extends RecyclerView.Adapter<LogHolder> {
        private List<LogItemData> mData;

        //Добавляет элемент лога
        public void addData(LogItemData data) {
            mData.add(data);
            notifyItemInserted(mData.size()-1);
        }

        public LogAdapter(List<LogItemData> data) {
            mData = data;
        }

        @NonNull
        @Override
        public LogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemLogBinding binding = DataBindingUtil.inflate(getLayoutInflater(),
                    R.layout.item_log,parent,false);
            return new LogHolder(binding,getContext());
        }

        @Override
        public void onBindViewHolder(@NonNull LogHolder holder, int position) {
            LogItemData data = mData.get(position);
            holder.bindData(data);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    /**
     * МЕНЮ
     * Взаимодействие с потоком (Start, Stop, Pause)
     * Поделиться
     * Настройки подключения
     * Очистить лог
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_connection_log_fragment,menu);
        MenuItem start = menu.findItem(R.id.start);
        MenuItem stop = menu.findItem(R.id.stop);
        MenuItem pause = menu.findItem(R.id.pause);
        MenuItem share = menu.findItem(R.id.share);

        if(!ThreadManager.getInstance(getContext()).hasThread(mUUID)) {
            ThreadManager.getInstance(getContext()).addThread(mUUID);
            setupThread();
        }
        stop.setVisible(false);
        pause.setVisible(false);
        start.setVisible(false);
        switch (ThreadManager.getInstance(getContext()).getThreadState(mUUID)) {
            case START:
                pause.setVisible(true);
                stop.setVisible(true);
                break;
            case PAUSE:
            case STOP:
                start.setVisible(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = ConnectionSettingsActivity.newIntent(getActivity(),mUUID,false);
                startActivityForResult(intent,REQUEST_SETTINGS);
                return true;
            case R.id.start:
                if(!ThreadManager.getInstance(getContext()).hasThread(mUUID)) {
                    ThreadManager.getInstance(getContext()).addThread(mUUID);
                    setupThread();
                }
                ThreadManager.getInstance(getContext()).startThread(mUUID);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.pause:
                ThreadManager.getInstance(getContext()).pauseThread(mUUID);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.stop:
                ThreadManager.getInstance(getContext()).stopThread(mUUID);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.clear:
                ConnectionPull.get(getContext()).clearLog(mUUID);
                getBinding().logList.setAdapter(new LogAdapter(ConnectionPull.get(getContext()).getLogList(mUUID)));
                return true;
            case R.id.share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT,getLogText());
                i.setType("text/plain");
                Intent sender = Intent.createChooser(i,"Share");
                startActivity(sender);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Возвращает название статуса заданное в настройках подключения
     * @param statusLog
     * @return
     */
    public String getStatus(LogItemData.StatusLog statusLog) {
        ConnectionItemData cData = ConnectionPull.get(getContext()).getConnection(mUUID);

        switch (statusLog) {
            case GOOD:
                return cData.getStatusOK();
            case BAD:
                return cData.getStatusBAD();
            default:
                return cData.getStatusWarning();
        }
    }

    /**
     * Составляет текст для отправки лога
     * @return
     */
    private String getLogText() {
        List<LogItemData> list = ConnectionPull.get(getContext()).getLogList(mUUID);
        StringBuilder result = new StringBuilder();

        for(LogItemData elem: list) {
            result.append(String.format("[%1$s] %2$s\n", elem.getTime().toString(), getStatus(elem.getStatus())));
        }
        return result.toString();
    }


    @Override
    public void onStop() {
        super.onStop();
        //Уничтожаем Handler и Listener в потоке
        ThreadManager.getInstance(getContext()).detachHandler(mUUID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_SETTINGS) {
            if (resultCode == ConnectionSettingsFragment.RESULT_DELETE) {
                //Завершаем работу при удалении подключения
                getActivity().finish();
            }
        }
    }

    /**
     * Метод для писоединения Handler и Listener к потоку
     */
    private void setupThread() {
        if(ThreadManager.getInstance(getContext()).hasThread(mUUID)) {
            ThreadManager.getInstance(getContext()).setOnRequestSendedListener(new onRequestSendedListener() {
                @Override
                public void onSended(LogItemData logItemData) {
                    LogAdapter adapter = (LogAdapter) getBinding().logList.getAdapter();
                    adapter.addData(logItemData);
                }
            }, new Handler(), mUUID);
        }
    }

    /**
     * Настройка фрагмента для работы
     * с привязками
     */
    @Override
    public FragmentViewModel onCreate() {
        return null;
    }

    @Override
    public int getVariable() {
        return 0;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_connection_log;
    }
}
