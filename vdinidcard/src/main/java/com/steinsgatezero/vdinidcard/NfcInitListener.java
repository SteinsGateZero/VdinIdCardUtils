package com.steinsgatezero.vdinidcard;

/**
 * NFC适配器监听(nfc是否打卡，nfc是否被支持)
 */
public interface NfcInitListener {

    /**
     * @param code 返回的状态码
     * @param msg 返回状态描述
     */
    void initStatus(int code, String msg);
}
