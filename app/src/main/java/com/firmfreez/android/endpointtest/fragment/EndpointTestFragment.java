package com.firmfreez.android.endpointtest.fragment;

import android.content.Intent;
import android.os.Bundle;
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

import com.firmfreez.android.endpointtest.BR;
import com.firmfreez.android.endpointtest.activity.ConnectionLogActivity;
import com.firmfreez.android.endpointtest.activity.ConnectionSettingsActivity;
import com.firmfreez.android.endpointtest.databinding.FragmentEndpointListBinding;
import com.firmfreez.android.endpointtest.databinding.ItemConnectionListBinding;
import com.firmfreez.android.endpointtest.model.ConnectionItemData;
import com.firmfreez.android.endpointtest.model.ConnectionPull;
import com.firmfreez.android.endpointtest.R;
import com.firmfreez.android.endpointtest.model.ConnectionViewModel;

import java.util.List;

public class EndpointTestFragment extends
        BindingFragment<FragmentEndpointListBinding,ConnectionViewModel> {

    private RecyclerView mRecyclerView;

    public static EndpointTestFragment newInstance() {
        Bundle args = new Bundle();
        EndpointTestFragment fragment = new EndpointTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind(inflater,container);
        mRecyclerView = getBinding().itemList;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new ConnectionAdapter(ConnectionPull.get(getActivity()).getData()));

        return getBinding().getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Восстанавливаем адаптер в RecyclerView
        ConnectionAdapter adapter = new ConnectionAdapter(ConnectionPull.get(getContext()).getData());
        mRecyclerView.setAdapter(adapter);
    }


    /**
     * Холдер для RecyclerView (Отображение одного элемента)
     */
    private class ConnectionHolder extends RecyclerView.ViewHolder
                                                        implements View.OnClickListener {
        ItemConnectionListBinding mBinding;
        ConnectionItemData mData;


        public ConnectionHolder(@NonNull ItemConnectionListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bindData(ConnectionItemData data) {
            if(mBinding.getViewModel() == null) {
                ConnectionViewModel viewModel = new
                        ConnectionViewModel(getContext());
                mBinding.setViewModel(viewModel);
            }
            mData = data;
            mBinding.getViewModel().setData(mData);
            mBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = ConnectionLogActivity.newIntent(getContext(), mData.getId());
            startActivity(intent);
        }
    }

    /**
     * Адаптер для RecyclerView
     */
    private class ConnectionAdapter extends RecyclerView.Adapter<ConnectionHolder> {
        private List<ConnectionItemData> mDatas;

        public ConnectionAdapter(List<ConnectionItemData> datas) {
            mDatas = datas;
        }

        @NonNull
        @Override
        public ConnectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemConnectionListBinding binding = DataBindingUtil.inflate(getLayoutInflater(),
                    R.layout.item_connection_list,parent,false);
            return new ConnectionHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ConnectionHolder holder, int position) {
            ConnectionItemData data = mDatas.get(position);
            holder.bindData(data);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }

    /**
     * Меню
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_endpoint_test_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_connection:
                Intent i = ConnectionSettingsActivity.newIntent(getActivity(),null, true);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Настройка ViewModel
     */
    @Override
    public ConnectionViewModel onCreate() {
        return new ConnectionViewModel(getActivity());
    }

    @Override
    public int getVariable() {
        return BR.ViewModel;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_endpoint_list;
    }
}
