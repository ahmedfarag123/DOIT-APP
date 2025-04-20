package com.example.doit1;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class delete extends Dialog {

    private Button buttonYes, buttonNo;
    private DeleteDialogListener listener;


    public delete(Context context, DeleteDialogListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_dialog);


        buttonYes = findViewById(R.id.button_yes);
        buttonNo = findViewById(R.id.button_no);


        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onYesClicked();
                }
                dismiss();
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNoClicked();
                }
                dismiss();
            }
        });
    }


    public interface DeleteDialogListener {
        void onYesClicked();
        void onNoClicked();
    }
}