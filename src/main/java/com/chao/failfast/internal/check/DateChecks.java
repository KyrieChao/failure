package com.chao.failfast.internal.check;

import java.util.Date;

/**
 * 日期校验工具类
 */
public final class DateChecks {

    private DateChecks() {}

    public static boolean after(Date date1, Date date2) {
        return date1 != null && date2 != null && date1.after(date2);
    }

    public static boolean before(Date date1, Date date2) {
        return date1 != null && date2 != null && date1.before(date2);
    }
}
