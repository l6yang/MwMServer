package com.mwm.loyal.utils;

import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StreamUtil {
    public static InputStream byteToInputStream(byte[] in) throws Exception {
        return new ByteArrayInputStream(in);
    }

    public static byte[] fileToByte(String path) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path));
            byte[] bytes = new byte[1024];
            int len;
            while ((len = fis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(fis);
            close(bos);
        }
    }

    public static byte[] blog2Byte(Blob blob) throws Exception {
        if (blob == null)
            return null;
        InputStream is = null;
        byte[] b = null;
        try {
            is = blob.getBinaryStream();
            b = new byte[(int) blob.length()];
            is.read(b);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(is);
        }
        return b;
    }

    public static void close(ResultSet rs, Connection conn, PreparedStatement pre) {
        close(rs);
        close(conn);
        close(pre);
    }

    public static void close(AutoCloseable close) {
        if (close != null) {
            try {
                close.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
