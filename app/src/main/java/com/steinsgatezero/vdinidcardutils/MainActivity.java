package com.steinsgatezero.vdinidcardutils;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.readTwoGeneralCard.Serverinfo;
import com.steinsgatezero.vdinidcard.BaseCardActivity;
import com.steinsgatezero.vdinidcard.CardListenter;
import com.steinsgatezero.vdinidcard.IdCardBean;
import com.steinsgatezero.vdinidcard.NfcInitListener;
import com.steinsgatezero.vdinidcard.VdinIdCard;

public class MainActivity extends BaseCardActivity implements NfcInitListener, CardListenter {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.testcard);
        //new Serverinfo("id.vdin01.com", 12345)// TygerZH server测试
        idCard = new VdinIdCard.Builder(this, "4ba7441a8f7181c8c2cfd9e3de12995b", new Serverinfo("id.vdin01.com", 80), this).isNFC(true).isTestServer(false).setNfcInitListenter(this).build();
    }

    public void readCard(View view) {
    }

    @Override
    public void initStatus(int code, String msg) {
        Log.e("nfcinit!", msg);
    }

    @Override
    public void onSucceed(IdCardBean cardBean) {
        textView.setText(cardBean.toString());
    }

    @Override
    public void onFailed(int code) {
    }
}
