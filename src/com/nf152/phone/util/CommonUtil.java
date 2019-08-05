package com.nf152.phone.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
    public static String fmtTime (Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String fmtTime2 (Date date) {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
    }
}
