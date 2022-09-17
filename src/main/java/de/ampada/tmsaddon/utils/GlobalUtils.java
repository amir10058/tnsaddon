package de.ampada.tmsaddon.utils;

import com.google.common.base.Strings;
import org.bson.types.ObjectId;

import java.util.Date;

public class GlobalUtils {
    public static Long convertDateToLong(Date date) {
        return date != null ? date.getTime() : null;
    }

    public static String convertObjectIdTOString(ObjectId objectId) {
        return objectId != null ? objectId.toString() : null;
    }

    public static ObjectId convertStringIdToObjectId(String id) {
        return !Strings.isNullOrEmpty(id) ? new ObjectId(id) : null;
    }
}
