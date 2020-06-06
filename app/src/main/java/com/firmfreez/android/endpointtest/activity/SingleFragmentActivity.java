package com.firmfreez.android.endpointtest.activity;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.firmfreez.android.endpointtest.R;

/**
 * Абстрактный класс, реализующий паттерн Активити-Фрагмент
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected static final String STATE_NAME = "state_name";
    protected static final String NEED_BUTTON = "need_button";
    /**
     * @return Layout - активити, на котором будет закреплен фрагмент
     */
    @LayoutRes
    public int getLayoutResId() {
        return R.layout.fragment_activity;
    }

    /**
     * Помогает подключать к активити любой фрагмент
     * @return подключаемый фрагмент
     */
    public abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(STATE_NAME)) {
                getSupportActionBar().setTitle(savedInstanceState.getString(STATE_NAME));
            }
            if(savedInstanceState.containsKey(NEED_BUTTON)) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }
}
