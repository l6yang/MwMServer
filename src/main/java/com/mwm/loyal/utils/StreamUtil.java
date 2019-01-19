package com.mwm.loyal.utils;

import java.io.*;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StreamUtil {
    public static InputStream byteToInputStream(byte[] in) {
        return new ByteArrayInputStream(in);
    }

    public static byte[] file2ByteArray(String path) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path));
            return stream2ByteArray(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(fis);
        }
    }

    public static byte[] stream2ByteArray(InputStream stream) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = stream.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            bos.reset();
        }
        return bos.toByteArray();
    }

    public static byte[] blog2ByteArray(Blob blob) {
        if (null == blob)
            return null;
        InputStream inputStream = null;
        try {
            inputStream = blob.getBinaryStream();
           /* b = new byte[(int) blob.length()];
            inputStream.read(b);*/
            return stream2ByteArray(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close(inputStream);
        }
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
