package com.kuo.kuoresume;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kuo.kuoresume.listener.ActivityListener;
import com.kuo.kuoresume.view.GLResumeView;
import com.kuo.kuoresume.view.LoadingScreen;

public class MainActivity extends AppCompatActivity implements ActivityListener {

    private GLResumeView glResumeView;

    private LoadingScreen loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        glResumeView = (GLResumeView) findViewById(R.id.glResumeView);
        glResumeView.setGLRenderer(this, this);
        glResumeView.setOnGLViewListener(onGLViewListener);

        loadingScreen = (LoadingScreen) findViewById(R.id.loadingScreen);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glResumeView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glResumeView.onResume();

        loadingScreen.setRun(true);
        loadingScreen.postInvalidate();
        loadingScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showDialogContact() {
        //DialogContact dialogContact = new DialogContact();
        //dialogContact.show(getSupportFragmentManager(), "contact");

        Intent intent = new Intent();
        intent.setClass(this, ResumeActivity.class);
        startActivity(intent);

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == 0) {
                loadingScreen.setRun(false);
                loadingScreen.setVisibility(View.GONE);
            }
        }
    };

    private GLResumeView.OnGLViewListener onGLViewListener = new GLResumeView.OnGLViewListener() {
        @Override
        public void onFirstDraw() {

            Message message = handler.obtainMessage();
            message.what = 0;
            handler.sendMessage(message);
        }
    };

}
