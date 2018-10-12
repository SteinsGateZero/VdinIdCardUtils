package com.steinsgatezero.vdinidcard;

/**
 * 读取结果监听回调
 */
public interface CardListenter {

    /**
     * 读取成功
     * @param cardBean 身份信息
     */
    void onSuccessed(IdCardBean cardBean);

    /**
     * 读取失败
     * @param code 状态码(参见VdinIdCard)
     */
    void onFailed(int code);
}
