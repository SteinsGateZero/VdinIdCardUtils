package com.steinsgatezero.vdinidcardutils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.readTwoGeneralCard.Serverinfo;
import com.steinsgatezero.vdinidcard.CardListenter;
import com.steinsgatezero.vdinidcard.IdCardBean;
import com.steinsgatezero.vdinidcard.NfcInitListener;
import com.steinsgatezero.vdinidcard.VdinIdCard;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NfcInitListener, CardListenter {

    VdinIdCard idCard;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.testcard);

        ArrayList<Serverinfo> twoCardServerlist = new ArrayList<Serverinfo>();
        twoCardServerlist.add(new Serverinfo("id.vdin01.com", 80));  // TygerZH server正式
        //twoCardServerlist.add(new Serverinfo("id.vdin01.com", 12345));  // TygerZH server测试
        idCard = new VdinIdCard.Builder(this, "4ba7441a8f7181c8c2cfd9e3de12995b", twoCardServerlist, this).isNFC(true).isTestServer(false).setNfcInitListenter(this).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        idCard.registerNfc();
    }

    @Override
    protected void onPause() {
        super.onPause();
        idCard.unRegisterNfc();
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

    public void readCard(View view) {
    }

    @Override
    public void initStatus(int code, String msg) {
        Log.e("nfcinit!", msg);
    }

    @Override
    public void onSuccessed(IdCardBean cardBean) {
        textView.setText(cardBean.toString());
    }

    @Override
    public void onFailed(int code) {
    }
}
