# VdinIdCardUtils
An utils for nfc

需要权限Need permission:

    <uses-permission android:name="android.permission.NFC" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    
使用范例:
    见app用例与注释
    
    NfcInitListener（非必须实现的接口，NFC适配器初始化监听）, CardListenter(必须实现的接口，读卡返回结果监听)，ReadProcess(非必须实现的接口，读卡过程监听)
