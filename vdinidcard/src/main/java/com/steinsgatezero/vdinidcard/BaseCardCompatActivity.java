package com.steinsgatezero.vdinidcard;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseCardCompatActivity extends AppCompatActivity {

    @NonNull
    protected VdinIdCard idCard;
    private boolean isFirst = true;//防止两次获取，第一次onCreate中也会初始化一次NFC适配器

    @RequiresPermission(Manifest.permission.NFC)
    @Override
    protected void onResume() {
        super.onResume();
        if (idCard != null && idCard.isNFC()) {
            if (!isFirst) {
                idCard.initNfcAdapter();
            }
            isFirst = false;
            idCard.registerNfc();
        }
    }

    @RequiresPermission(Manifest.permission.NFC)
    @Override
    protected void onPause() {
        super.onPause();
        if (idCard != null && idCard.isNFC()) {
            idCard.unRegisterNfc();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        idCard.destroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        idCard.readCard(intent);
    }
}
