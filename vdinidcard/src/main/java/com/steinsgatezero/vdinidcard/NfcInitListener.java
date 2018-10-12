package com.steinsgatezero.vdinidcard;

/**
 * NFC初始化监听
 */
public interface NfcInitListener {

    /**
     * @param code 返回的状态码
     * @param msg 返回状态描述
     */
    void initStatus(int code, String msg);
}
