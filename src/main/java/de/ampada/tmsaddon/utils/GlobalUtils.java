package de.ampada.tmsaddon.utils;

import java.util.Date;

public class GlobalUtils {
    public static Long convertDateToLong(Date date) {
        return date != null ? date.getTime() : null;
    }
}
