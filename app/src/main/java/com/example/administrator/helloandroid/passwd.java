package com.example.administrator.helloandroid;

/**
 * Created by BBlackB on 2016/10/5.
 */
public class passwd {
    private static String prefix = "陈明键";
    private static String suffix = "裸体";
    public static String getPasswd(String wifi_ssid){
        return MD5.stringMD5(prefix + wifi_ssid + suffix);
    }
}
