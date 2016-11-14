package com.fe.jdcommodityselectspecifications;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private SelectSpecificationsLayout mSelectSFLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSelectSFLayout = (SelectSpecificationsLayout) findViewById(R.id.SSLayout);
        mSelectSFLayout.mBottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectSFLayout.moveView();
                showDialog();
            }
        });
    }

    private void showDialog() {
        Dialog mDialog = new Dialog(this, R.style.dialog);
        mDialog.setContentView(View.inflate(this, R.layout.activity_dialog, null));
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        Window localWindow = mDialog.getWindow();
        localWindow.setWindowAnimations(R.style.dialog_animation);
        localWindow.setGravity(Gravity.CENTER);
        localWindow.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = localWindow.getAttributes();
        int[] wh = {ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT};
        lp.width = wh[0];
        lp.height = wh[1];
        localWindow.setAttributes(lp);

        if(mDialog != null && !mDialog.isShowing())
            mDialog.show();
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mSelectSFLayout.resetView();
            }
        });
    }
}
