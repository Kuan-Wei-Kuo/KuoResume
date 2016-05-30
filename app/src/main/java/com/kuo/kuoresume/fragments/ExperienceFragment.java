package com.kuo.kuoresume.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuo.kuoresume.R;

/**
 * Created by Kuo on 2016/5/30.
 */
public class ExperienceFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null)
            rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        return rootView;
    }
}
