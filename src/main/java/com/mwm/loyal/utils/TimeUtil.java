package com.mwm.loyal.utils;

import com.mwm.loyal.imp.ResListener;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil implements ResListener {
    public static String getWeek() {
        SimpleDateFormat format = new SimpleDateFormat(Str.TIME_WEEK, Locale.CHINA);
        return StringUtil.replaceTime(format.format(new Date()));
    }

    private static SimpleDateFormat setFormat(String format) {
        return new SimpleDateFormat(format, Locale.CHINA);
    }

    public static String getDate() {
        return StringUtil.replaceTime(setFormat(Str.TIME_YEAR_MONTH_DAY)
                .format(new Date()));
    }

    public static String getDateTime() {
        return StringUtil.replaceTime(setFormat(Str.TIME_ALL).format(new Date()));
    }

    public static String getDateTime(String format) {
        return StringUtil.replaceTime(setFormat(format).format(new Date()));
    }

    public static String getDateTime(Date date, String format) {
        return StringUtil.replaceTime(setFormat(format).format(date));
    }

    public static String getDate(Date date, String format) {
        return StringUtil.replaceTime(setFormat(format).format(date));
    }

    public static String getDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat(time, Locale.CHINA);
        return StringUtil.replaceTime(format.format(new Date()));
    }

    public static String getTime() {
        return StringUtil.replaceTime(setFormat(Str.HOURS_MIN).format(new Date()));
    }

    public static boolean afterDate(String startTime, String endTime, String format) {
        try {
            SimpleDateFormat sdf = setFormat(format);
            if (startTime.isEmpty() || endTime.isEmpty())
                return true;
            if (startTime.equals(endTime))
                return true;
            try {
                Date start = sdf.parse(startTime);
                Date end = sdf.parse(endTime);
                return end.after(start);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static int dateSpan(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = setFormat(Str.TIME_YEAR_MONTH_DAY);
            if (startTime.isEmpty() || endTime.isEmpty())
                return -1;
            if (startTime.equals(endTime))
                return 1;
            try {
                Date start = sdf.parse(StringUtil.replaceTime(startTime));
                Date end = sdf.parse(StringUtil.replaceTime(endTime));
                long span = (end.getTime() - start.getTime()) / 1000;
                int day = (int) span / (24 * 3600);
                return day >= 0 ? day + 1 : -1;
            } catch (ParseException e) {
                e.printStackTrace();
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public static float dateTime(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = setFormat(Str.TIME_ALL);
            if (startTime.isEmpty() || endTime.isEmpty())
                return -1;
            if (startTime.equals(endTime))
                return 0;
            try {
                Date start = sdf.parse(StringUtil.replaceTime(startTime));
                Date end = sdf.parse(StringUtil.replaceTime(endTime));
                long span = (end.getTime() - start.getTime()) / 1000;
                float hour = (float) span / (3600);
                hour = (float) Math.round(hour * 100) / 100;
                return hour >= 0 ? hour : -1;
            } catch (ParseException e) {
                e.printStackTrace();
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public static Timestamp date2Timestamp(String time) {
        try {
            Date date = setFormat(Str.TIME_ALL).parse(time);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Timestamp getTimestamp() {
        try {
            Date date = setFormat(Str.TIME_ALL).parse(getDateTime());
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String beforeHour(String startTime, String format, int hour) {
        try {
            SimpleDateFormat sdf = setFormat(format);
            Date start = sdf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            calendar.add(Calendar.HOUR, -hour);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            return "";
        }
    }

    public static String beforeMonth(String startTime, String format, int month) {
        try {
            SimpleDateFormat sdf = setFormat(format);
            Date start = sdf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            calendar.add(Calendar.MONTH, -month);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            return "";
        }
    }

    public static String beforeDay(String startTime, String format, int day) {
        try {
            SimpleDateFormat sdf = setFormat(format);
            Date start = sdf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            calendar.add(Calendar.DATE, -day);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            return "";
        }
    }

    public static String beforeWeek(String startTime, String format) {
        try {
            SimpleDateFormat sdf = setFormat(format);
            Date start = sdf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            calendar.add(Calendar.DATE, -7);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        System.out.println(date2Timestamp("2016-11-23 09:33:51"));
        String before = beforeMonth("2016-11-23 09:33:51", Str.TIME_ALL, 1);
        System.out.println(date2Timestamp(before));
        Object object = null;
        String str = (String) object;
        System.out.println(str == null);
    }
}
