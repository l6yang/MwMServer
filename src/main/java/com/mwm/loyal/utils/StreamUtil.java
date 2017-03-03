package com.mwm.loyal.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;

public class StreamUtil {
    public static InputStream byteTOInputStream(byte[] in) throws Exception {
        return new ByteArrayInputStream(in);
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
