package com.steinsgatezero.vdinidcard;

/**
 * 读卡进度
 */
public interface ReadProcess {

    /**
     * 读卡开始时
     */
    void readStart();

    /**
     * 读卡结束时
     */
    void readComplete();
}
