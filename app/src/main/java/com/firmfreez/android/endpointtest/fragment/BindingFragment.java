package com.firmfreez.android.endpointtest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.firmfreez.android.endpointtest.model.FragmentViewModel;

/**
 * Абстрактный класс, упрощающий работу с привязками во фрагменте
 * @param <B> Binding класс, для работы с привязками
 * @param <VM> тип необходимой ViewModel
 */
public abstract class BindingFragment<B extends ViewDataBinding,
        VM extends FragmentViewModel> extends Fragment {
    private VM mViewModel;
    private B binding;

    /**
     * Конструктор ViewModel
     * @return ViewModel
     */
    public abstract VM onCreate();

    /**
     * Переменная, которая используется в Layout (<variables name=""/>)
     * @return int
     */
    public abstract @IdRes int getVariable();

    /**
     * Layout, натягивающийся на Fragment
     * @return int
     */
    public abstract @LayoutRes int getLayout();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind(inflater,container);
        return binding.getRoot();
    }

    /**
     * Метод, который должен вызываться в самом начале CreateView фрагмента.
     * Он инициализирует все привязки
     * @param inflater
     * @param container
     */
    public void bind(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater,getLayout(),container,false);
        mViewModel = mViewModel == null ? onCreate() : mViewModel;
        binding.setVariable(getVariable(), mViewModel);
        binding.executePendingBindings();
    }

    public B getBinding() {
        return binding;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mViewModel == null) {
            return;
        }
        mViewModel.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mViewModel == null) {
            return;
        }
        mViewModel.onResume();
    }


}
