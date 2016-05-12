package com.kuo.kuoresume.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.kuo.kuoresume.R;
import com.kuo.kuoresume.view.LoadingScreen;

/**
 * Created by User on 2016/5/11.
 */
public class DialogProgress extends DialogFragment{

    private LoadingScreen loadingScreen;

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();

        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_progress, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        loadingScreen = (LoadingScreen) view.findViewById(R.id.loadingScreen);

        return view;
    }

}
