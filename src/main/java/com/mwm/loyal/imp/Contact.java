package com.mwm.loyal.imp;

import com.mwm.loyal.utils.PubUtils;

public interface Contact {

    class Str {
        public static final String TIME_ALL = "yyyy-MM-dd HH:mm:ss";
        public static final String TIME_WEEK = "yyyy-MM-dd EEEE";
        public static final String TIME_YEAR_MONTH_DAY = "yyyy-MM-dd";
        public static final String HOURS_MIN = "HH:mm";
        public static final String MONTH_DAY_HOUR_MIN = "MM-dd HH:mm";
        public static final String YEAR_MONTH = "yyyy-MM";
        public static final String KAY_ENCRYPT_DECODE = "com.mwm.forLoyal";
        public static final String localhost = PubUtils.getLocalHostIpAddress();
    }
}
