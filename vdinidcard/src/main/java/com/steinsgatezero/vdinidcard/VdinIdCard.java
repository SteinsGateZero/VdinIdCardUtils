package com.steinsgatezero.vdinidcard;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.readTwoGeneralCard.ActiveCallBack;
import com.readTwoGeneralCard.EidUserInfo;
import com.readTwoGeneralCard.OTGReadCardAPI;
import com.readTwoGeneralCard.PassportInfo;
import com.readTwoGeneralCard.Serverinfo;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class VdinIdCard implements ActiveCallBack {
    //初始化NFC返回的状态码
    public static final int INIT_NFC_SUCCESS = 1;//初始化nfc成功
    public static final int INIT_NFC_CLOSE = 0;//初始化成功,但nfc关闭着
    public static final int INIT_NFC_NOTSUPPORT = -1;//不支持nfc
    //读卡返回的状态码
    public static final int READ_SUCCESS = 90;//成功
    public static final int READ_TIMEOUT = 2;//超时
    public static final int READ_FAIL = 41;//读卡失败
    public static final int READ_CONNECTFAIL = 42;//网络连接失败
    public static final int READ_BUSY = 43;//服务器繁忙

    private Context context;
    private NfcInitListener nfcInitListener;//nfc适配器监听
    private ReadProcess readProcess;//读卡进程监听
    private CardListenter cardListenter;//读卡结果监听回调
    private final boolean isNFC;//是否是nfc模式,默认为是
    private final ArrayList<Serverinfo> cardServerList;
    private final ArrayList<Serverinfo> eidServerList;
    private NfcAdapter adapter;//nfc适配器
    private boolean isTestServer;//是否是测试服务器,默认为否
    private OTGReadCardAPI readCardAPI;//读卡接口
    private PendingIntent pendingIntent;//延迟意图
    private IntentFilter tagDetected;
    private ThreadPoolExecutor readExecutor;//线程池
    private final String szFactory;//厂商标识

    @Override
    public String GetEidPin(int i) {
        // TODO: 2018/10/15 For EidPin
        return null;
    }

    @Override
    public EidUserInfo GetEidUserInfo() {
        // TODO: 2018/10/15 For Eid
        return null;
    }

    @Override
    public PassportInfo GetPassportUserInfo() {
        // TODO: 2018/10/15 For Passport
        return null;
    }

    @Override
    public void readProgress(int i) {
        // TODO: 2018/10/15 For readProgress
    }

    @Override
    public void onPacNewIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        readCard(intent);
    }

    @Override
    public void setUserInfo(String s) {

    }

    /**
     * builder
     */
    public static final class Builder {
        private final Context context;
        private boolean isNFC = true;//默认为NFC模式
        private final ArrayList<Serverinfo> cardServerList;
        private ArrayList<Serverinfo> eidServerList = null;
        private boolean isTestServer;//默认不是测试服务器
        private NfcInitListener nfcInitListener = null;
        private final CardListenter cardListenter;
        private ReadProcess readProcess = null;
        private final String szFactory;//厂商标识符

        public Builder(@NonNull Context context, @NonNull String szFactory, @NonNull ArrayList<Serverinfo> cardServerList, @NonNull CardListenter cardListenter) {
            this.context = context;
            this.cardServerList = cardServerList;
            this.szFactory = szFactory;
            this.cardListenter = cardListenter;
        }

        public Builder(@NonNull Context context, @NonNull String szFactory, @NonNull Serverinfo serverinfo, @NonNull CardListenter cardListenter) {
            this.context = context;
            this.cardServerList = new ArrayList<>();
            cardServerList.add(serverinfo);
            this.szFactory = szFactory;
            this.cardListenter = cardListenter;
        }

        public Builder isNFC(boolean isNFC) {
            this.isNFC = isNFC;
            return this;
        }

        public Builder setEidServerList(ArrayList<Serverinfo> eidServerList) {
            this.eidServerList = eidServerList;
            return this;
        }

        public Builder isTestServer(boolean isTestServer) {
            this.isTestServer = isTestServer;
            return this;
        }

        public Builder setNfcInitListenter(NfcInitListener nfcInitListener) {
            this.nfcInitListener = nfcInitListener;
            return this;
        }

        public Builder setReadProcessListenter(ReadProcess readProcess) {
            this.readProcess = readProcess;
            return this;
        }

        public VdinIdCard build() {
            return new VdinIdCard(this);
        }
    }

    private VdinIdCard(Builder builder) {
        //初始化线程池
        readExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1));
        context = builder.context;
        isNFC = builder.isNFC;
        cardServerList = builder.cardServerList;
        eidServerList = builder.eidServerList;
        isTestServer = builder.isTestServer;
        szFactory = builder.szFactory;
        readProcess = builder.readProcess;
        cardListenter = builder.cardListenter;
        initReadCardAPI();
        if (isNFC) {
            nfcInitListener = builder.nfcInitListener;
            pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, context.getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            tagDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
            tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
            initNfcAdapter();
        } else {
// TODO: 2018/10/15 For OTG init
        }
    }

    /**
     * 读卡
     *
     * @param intent 读取的数据
     */
    public void readCard(final Intent intent) {
        if (readProcess != null) {
            readProcess.readStart();
        }
        try {
            readExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    int readCode = READ_FAIL;
                    if (isNFC) {
                        readCode = readCardAPI.NfcReadCard(szFactory, intent, null);
                    } else {
// TODO: 2018/10/15 For OTG Read
                    }
                    parseCard(readCode);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取读卡信息
     *
     * @param code 读卡返回的状态码
     */
    private synchronized void parseCard(final int code) {
        if (context == null) {
            return;
        }
        switch (code) {
            case READ_SUCCESS:
                final IdCardBean cardBean = new IdCardBean();
                cardBean.setName(readCardAPI.Name().trim());
                cardBean.setAddress(readCardAPI.Address().trim());
                StringBuilder stringBuilder = new StringBuilder(readCardAPI.BornL());
                int yearIndex = stringBuilder.indexOf("年");
                int monthIndex = stringBuilder.indexOf("月");
                stringBuilder.replace(yearIndex, yearIndex + 1, "-");
                stringBuilder.replace(monthIndex, monthIndex + 1, "-");
                cardBean.setBirthDate(stringBuilder.toString());
                cardBean.setEthnicity(readCardAPI.NationL().trim());
                cardBean.setGender(readCardAPI.SexL());
                cardBean.setIssuingAuthorityName(readCardAPI.Police().trim());
                cardBean.setCardType(readCardAPI.CardType());
                if (readCardAPI.CardType().equals("J")) {
                    cardBean.setIdentificationNumber(readCardAPI.CardOtherNo().trim());
                } else {
                    cardBean.setIdentificationNumber(readCardAPI.CardNo().trim());
                }
                cardBean.setCardSignNum(readCardAPI.CardSignNum());
                StringBuilder builderDate = new StringBuilder(readCardAPI.Activity());
                builderDate.insert(4, "-");
                builderDate.insert(7, "-");
                builderDate.insert(15, "-");
                builderDate.insert(18, "-");
                cardBean.setValidityFromDate(builderDate.substring(0, 10));
                cardBean.setValidityThruDate(builderDate.substring(11, builderDate.length()));
                cardBean.setDnId(readCardAPI.GetDNID());
                cardBean.setSnId(readCardAPI.GetSNID());
                cardBean.setPortraits(readCardAPI.GetImage());
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cardListenter.onSuccessed(cardBean);
                    }
                });
                break;
            case READ_FAIL:
            case READ_CONNECTFAIL:
            case READ_TIMEOUT:
            case READ_BUSY:
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cardListenter.onFailed(code);
                    }
                });
                break;
        }
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (readProcess != null) {
                    readProcess.readComplete();
                }
            }
        });
    }

    /**
     * 获取nfc适配器
     *
     * @return 返回布尔值是否支持nfc
     */
    public boolean initNfcAdapter() {
        //if (adapter == null) {
        adapter = NfcAdapter.getDefaultAdapter(context);
        // }
        if (adapter != null) {
            if (adapter.isEnabled()) {
                if (this.nfcInitListener != null) {
                    this.nfcInitListener.initStatus(INIT_NFC_SUCCESS, "NFC已开启");
                }
            } else {
                if (this.nfcInitListener != null) {
                    this.nfcInitListener.initStatus(INIT_NFC_CLOSE, "NFC尚未开启");
                }
            }
            return true;
        } else {
            if (this.nfcInitListener != null) {
                this.nfcInitListener.initStatus(INIT_NFC_NOTSUPPORT, "本机不支持NFC功能");
            }
            return false;
        }
    }

    /**
     * 初始化 读卡api
     */
    private void initReadCardAPI() {
        readCardAPI = new OTGReadCardAPI(context, this, isNFC);
        readCardAPI.setServerInfo(cardServerList, eidServerList, isTestServer);
    }

    /**
     * 添加过滤，监听nfc标签读取
     */
    @RequiresPermission(Manifest.permission.NFC)
    public void registerNfc() {
        if (adapter == null) {
            return;
        }
        adapter.enableForegroundDispatch((Activity) context, pendingIntent, new IntentFilter[]{tagDetected}, new String[][]{
                new String[]{NfcF.class.getName()},
                new String[]{NfcA.class.getName()},
                new String[]{NfcB.class.getName()},
                new String[]{NfcV.class.getName()},
                new String[]{Ndef.class.getName()},
                new String[]{NdefFormatable.class.getName()},
                new String[]{MifareClassic.class.getName()}
        });
    }

    /**
     * 移除对nfc的监听
     */
    @RequiresPermission(Manifest.permission.NFC)
    public void unRegisterNfc() {
        if (adapter == null) {
            return;
        }
        adapter.disableForegroundDispatch((Activity) context);
    }

    /**
     * 销毁
     */
    public void destroy() {
        readCardAPI.release();
        readExecutor.shutdown();
        try {
            if (!readExecutor.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                readExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readCardAPI = null;
            readExecutor = null;
            adapter = null;
            pendingIntent = null;
            context = null;
            nfcInitListener = null;
            readProcess = null;
            eidServerList.clear();
            cardServerList.clear();
        }

    }

    /**
     * @return 判断是否为NFC模式
     */
    public boolean isNFC() {
        return isNFC;
    }
}
