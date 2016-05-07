package com.kuo.kuoresume;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.kuo.kuoresume.view.GLResumeView;

public class MainActivity extends AppCompatActivity {

    private GLResumeView glResumeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        glResumeView = new GLResumeView(this);
        setContentView(glResumeView);
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
    }
}
