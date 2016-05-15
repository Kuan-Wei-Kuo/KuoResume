package com.kuo.kuoresume;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.kuo.kuoresume.dialog.DialogContact;
import com.kuo.kuoresume.dialog.DialogProgress;
import com.kuo.kuoresume.listener.ActivityListener;
import com.kuo.kuoresume.view.GLResumeView;
import com.kuo.kuoresume.view.LoadingScreen;

public class MainActivity extends AppCompatActivity implements ActivityListener {

    private ProgressDialog progressDialog;
    private GLResumeView glResumeView;

    private Dialog dialog;

    private LoadingScreen loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        glResumeView = (GLResumeView) findViewById(R.id.glResumeView);
        glResumeView.setGLRenderer(this, this);

        Log.d("onCreate", "true");
    }

    @Override
    protected void onPause() {
        super.onPause();
        glResumeView.onPause();
        Log.d("onPause", "true");
    }

    @Override
    protected void onResume() {
        super.onResume();
        glResumeView.onResume();
        Log.d("onResume", "true");
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public void showDialogContact() {
        DialogContact dialogContact = new DialogContact();
        dialogContact.show(getSupportFragmentManager(), "contact");
    }

    @Override
    public void showDialogShare() {
        Intent shareIntent = new Intent();
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://kuan-wei-kuo-blog.logdown.com");
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    @Override
    public void showGitHubBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Kuan-Wei-Kuo"));
        startActivity(browserIntent);
    }

    DialogProgress dialogProgress = new DialogProgress();

    @Override
    public void showProgress() {
        dialogProgress.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void dismissProgrees() {
        dialogProgress.dismiss();
    }


}
