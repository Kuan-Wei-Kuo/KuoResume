package com.kuo.kuoresume.listener;

import android.content.Context;

/**
 * Created by Kuo on 2016/5/12.
 */
public interface ActivityListener {

    Context getContext();

    void showDialogContact();

    void showDialogShare();

    void showGitHubBrowser();
}
