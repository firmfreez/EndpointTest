package com.firmfreez.android.endpointtest.activity;

import androidx.fragment.app.Fragment;

import com.firmfreez.android.endpointtest.fragment.EndpointTestFragment;

public class EndpointTestActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return EndpointTestFragment.newInstance();
    }
}
