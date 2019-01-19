package com.mwm.loyal.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {
    public static void closeStream(Closeable stream) {
        try {
            if (null != stream)
                stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
