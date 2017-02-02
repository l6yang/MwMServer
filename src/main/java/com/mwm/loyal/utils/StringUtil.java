package com.mwm.loyal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    //端口号
    public static final String http = "http://";
    //private static final String url = http + ip + port;
    //http://192.168.1.140:8080/mvvm/login.do?method=loginCode
    //private static final String ip = "wg.gsgajt.gov.cn";
    //private static String ip = "61.178.79.18";
    //private static final String ip = "7.200.10.91";
    public static final String ip = "192.168.1.140";
    public static final String port = ":8080";
    private static final String url = http + ip + port + "/mvvm/action.do?method=";

    //测试环境  gslkyw  真实环境  vehregister
    //public static String Uri = http + ip + port;
    public static String getServiceUrl(String method) {
        return url + method;
    }

    public static String replaceNull(Object object) {
        return object == null || object.toString().equals("null") ? "" : object.toString();
    }

    public static boolean isEmpty(String str) {
        return ((str == null) || (str.trim().length() == 0));
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    public static boolean isMobileNo(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean ipValue(String address) {
        if (isEmpty(address))
            return false;
        String ipPattern = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pattern = Pattern.compile(ipPattern);
        Matcher matcher = pattern.matcher(address);
        return matcher.matches();
    }

    public static String getSpinItem(String str) {
        return str.split(":").length == 2 ? str.split(":")[1] : str;
    }

    public static String replaceTime(String str) {
        return str.toLowerCase().replace("上午", "").replace("下午", "").replace("am", "").replace("pm", "");
    }
}
