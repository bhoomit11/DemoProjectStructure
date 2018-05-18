package com.timeout72hours.attributes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.timeout72hours.R;


/**
 * Created by bhumit on 3/6/17.
 */
public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }
}