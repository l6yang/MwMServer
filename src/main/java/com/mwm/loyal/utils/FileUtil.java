package com.mwm.loyal.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    // sd卡路径
    private static final String SD_PATH = "E://";
    private static final String path_main = SD_PATH + File.separator + "com.mwm.loyal" + File.separator;
    // 本地保存文件
    public static final String path_local = path_main + "local" + File.separator;
    public static final String path_icon = path_main + "icon" + File.separator;
    public static final String path_temp = path_main + "temp" + File.separator;
    public static final String path_voice = path_main + "voice" + File.separator;
    public static final String pic_UCrop = "UCrop.jpg";
    public static final String pic_temp = "temp.jpg";
    public static final String pic_tmp = "tmp.jpg";
    // apk更新下载的更新文件存放的目录
    public static final String path_apk = path_main + "apk" + File.separator;
    public static final String path_share = path_main + "share" + File.separator;
    public static final String apkFileName = "mwm.apk";

    public static boolean fileCreated(String path) {
        File file = new File(path);
        return file.exists();
    }

    private static boolean createFiles(File file) {
        return !file.exists() && file.mkdirs();
    }

    // 保存IP文件到本地 这里只需要把文件名传进去就行，不管保存的类型
    public static void createFiles(String path, String fileName, String content) {
        File file = new File(path + fileName);
        boolean delete = deleteFile(file);
        if (delete)
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(content.getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * @param file Eg:"F:\ss\新建文本文档.txt"
     * @ 删除文件
     */
    public static boolean deleteFile(File file) {
        try {
            return !file.exists() || file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    public static boolean deleteFile(String path, String fileName) {
        return deleteFile(new File(path, fileName));
    }

    public static boolean renameFile(File resFile, File goalFile) {
        try {
            return resFile.renameTo(goalFile);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean renameFile(String resFile, String goalFile) {
        try {
            File res = new File(resFile);
            File goal = new File(goalFile);
            return renameFile(res, goal);
        } catch (Exception e) {
            return false;
        }
    }
}
