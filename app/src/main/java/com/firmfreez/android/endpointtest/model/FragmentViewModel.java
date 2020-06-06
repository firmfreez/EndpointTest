package com.firmfreez.android.endpointtest.model;

import android.content.Context;

import androidx.databinding.BaseObservable;

/**
 * Абстрактный класс для упрощенного создания ViewModel для фрагмента
 * @param <F> Тип унаследованный от Context
 */
public abstract class FragmentViewModel<F extends Context> extends BaseObservable {

    private F context;

    public FragmentViewModel(F context) {
        this.context = context;
    }

    public F getFragment() {
        return context;
    }

    public void onStart() {
        //Override
    }

    public void onResume() {
        //Override
    }
}
