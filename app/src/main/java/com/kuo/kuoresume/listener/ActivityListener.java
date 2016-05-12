package com.kuo.kuoresume.listener;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Kuo on 2016/5/12.
 */
public interface ActivityListener {

    Context getContext();

    ProgressDialog getProgressDialog();

    void showDialogContact();

    void showDialogShare();

    void showProgress();

    void dismissProgrees();
}
