package com.kuo.kuoresume;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.kuo.kuoresume.dialog.DialogContact;
import com.kuo.kuoresume.listener.ActivityListener;
import com.kuo.kuoresume.view.GLResumeView;

public class MainActivity extends AppCompatActivity implements ActivityListener {

    private ProgressDialog progressDialog;
    private GLResumeView glResumeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");

        glResumeView = new GLResumeView(this, this);
        setContentView(glResumeView);

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

    private boolean isDismiss = false;

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this,
                "Loading", "Please Wait...",true);
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{

                    while(isDismiss) {
                        Thread.sleep(3000);
                    }

                    isDismiss = false;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void dismissProgrees() {
        isDismiss = true;
    }


}
