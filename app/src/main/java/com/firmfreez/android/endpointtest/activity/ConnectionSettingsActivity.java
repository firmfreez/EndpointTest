package com.firmfreez.android.endpointtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.firmfreez.android.endpointtest.R;
import com.firmfreez.android.endpointtest.fragment.ConnectionSettingsFragment;

/**
 * Активити, удерживающее фрагмент, отвечающий за добавление и настройку соединений
 */
public class ConnectionSettingsActivity extends SingleFragmentActivity {
    private static final String TAG = "ConnectionSettingsA";
    private static final String EXTRA_ID = "com.firmfreez.android.endpointtest.extra_id";
    private static final String EXTRA_ISNEW = "com.firmfreez.android.endpointtest.is_new";
    /**
     * Необходимо для вызова активити из фрагмента
     * @param context Контекст родителя
     * @param id ID подключения (null, если новое подключение)
     * @return Intent
     */
    public static Intent newIntent(Context context, UUID id, boolean isNew) {
        Intent intent = new Intent(context, ConnectionSettingsActivity.class);
        intent.putExtra(EXTRA_ID,id);
        intent.putExtra(EXTRA_ISNEW,isNew);
        return intent;
    }

    /**
     * Передаём ID во фрагмент
     * @return Fragment
     */
    @Override
    public Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_ID);

        boolean isNew = getIntent().getBooleanExtra(EXTRA_ISNEW, true);
        if(isNew) {
            getSupportActionBar().setTitle(R.string.settings_add_connection_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setTitle(R.string.settings_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        return ConnectionSettingsFragment.newInstance(id);
    }

    /**
     * Сохранение названия и кнопки ActionBar - а
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_NAME, getSupportActionBar().getTitle().toString());
        outState.putString(NEED_BUTTON, "true");
    }

    /**
     * Действие кнопки "Стрелка назад"
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setResult(UUID id) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID,id);
        setResult(RESULT_OK,intent);
    }
}
