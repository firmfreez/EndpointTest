package com.firmfreez.android.endpointtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.UUID;

import com.firmfreez.android.endpointtest.fragment.ConnectionLogFragment;
import com.firmfreez.android.endpointtest.model.ConnectionPull;

/**
 * Активити для отображения фрагмента лога
 */
public class ConnectionLogActivity extends SingleFragmentActivity {
    private static final String ARG_ID = "com.firmfreez.android.endpointtest.uuuiid";

    /**
     * Возвращает Intent для вызова активити
     * @param context
     * @param id UUID подключения
     * @return
     */
    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context,ConnectionLogActivity.class);
        intent.putExtra(ARG_ID, id);
        return intent;
    }

    /**
     * Сохранение названия ActionBar - а
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_NAME,getSupportActionBar().getTitle().toString());
    }

    @Override
    public Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(ARG_ID);
        if(uuid != null) {
            String title = ConnectionPull.get(this).getConnection(uuid).getName();
            getSupportActionBar().setTitle(title);
        }

        return ConnectionLogFragment.newInstance(uuid);
    }
}
