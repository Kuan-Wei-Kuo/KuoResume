package com.kuo.kuoresume.listener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Kuo on 2016/5/12.
 */
public interface ActivityListener {

    Context getContext();

    ProgressDialog getProgressDialog();

    AppCompatActivity getActivity();

    void showDialogContact();

    void showDialogShare();

    void showProgress();

    void dismissProgrees();
}
