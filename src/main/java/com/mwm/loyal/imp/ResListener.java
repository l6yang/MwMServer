package com.mwm.loyal.imp;

import com.mwm.loyal.utils.PubUtils;

public interface ResListener {

    class Str {
        public static final String TIME_ALL = "yyyy-MM-dd HH:mm:ss";
        public static final String TIME_WEEK = "yyyy-MM-dd EEEE";
        public static final String TIME_YEAR_MONTH_DAY = "yyyy-MM-dd";
        public static final String HOURS_MIN = "HH:mm";
        public static final String MONTH_DAY_HOUR_MIN = "MM-dd HH:mm";
        public static final String YEAR_MONTH = "yyyy-MM";
        public static final String KEY_IP = "ip";
        public static final String KEY_ACCOUNT = "account";
        public static final String KEY_PASSWORD = "password";
        public static final String SERVICE_ACTION_home = "com.zlkj.room.service.action.home";
        public static final String SERVICE_ACTION_empty = "com.zlkj.room.service.action.empty";
        public static final String SERVICE_ACTION_restart = "com.zlkj.room.service.action.restart";
        public static final String SERVICE_ACTION_map = "com.zlkj.room.service.action.map";
        public static final String action_register = "doRegister";
        public static final String action_login = "doLogin";
        public static final String action_showIcon = "doShowIconByIO";
        public static final String action_update = "doUpdateAccount";
        public static final String action_update_icon = "doUpdateIcon";
        public static final String action_feedBack = "doFeedBack";
        public static final String action_ucrop_test = "doUCropTest";
        public static final String action_scan = "doScanQuery";
        public static final String action_apkVer = "doApkVer";
        public static final String KAY_ENCRYPT_DECODE = "com.mwm.forLoyal";
        public static final String localhost = PubUtils.getLocalHostIpAddress();
    }

    class Int {
        public static final int DEFAULT_ITEM_SIZE = 10;
        public static final int RefreshData = -100;
        public static final int reqCode_Main_Setting = 102;
        public static final int reqCode_Main_Zing = 103;
        public static final int reqCode_Main_UCrop = 104;
        public static final int reqCode_Main_editDevice = 101;
        public static final int reqCode_Main_allPreview = 200;
        public static final int reqCode_Main_icon = 201;
        public static final int state_ups_work = 1000;
        public static final int state_ups_param = 1001;
        public static final int state_ups_error = 1002;
        public static final int reqCode_Main_noRequest = 5000;
    }
}
