package com.kuo.kuoresume.dialog;

import android.content.Intent;
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

/**
 * Created by User on 2016/5/11.
 */
public class DialogContact extends DialogFragment implements Button.OnClickListener{

    private EditText editSubject, editMessage;
    private Button buttonCancel, buttonEnter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_contact, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        editSubject = (EditText) view.findViewById(R.id.edit_subject);
        editMessage = (EditText) view.findViewById(R.id.edit_message);

        buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        buttonEnter = (Button) view.findViewById(R.id.buttonEnter);

        buttonCancel.setOnClickListener(this);
        buttonEnter.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonCancel:
                dismiss();
                break;
            case R.id.buttonEnter:
                sendEmail();
                dismiss();
                break;
        }

    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"k.kuanwei@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, editSubject.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT   , editMessage.getText().toString());
        getContext().startActivity(Intent.createChooser(emailIntent, "Contact"));
    }
}
