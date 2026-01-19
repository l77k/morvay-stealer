/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bouncycastle.util.Longs;

public class LocaleUtil {
    private static final Map localeCache = new HashMap();
    public static Locale EN_Locale = LocaleUtil.forEN();

    private static Locale forEN() {
        if ("en".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            return Locale.getDefault();
        }
        Locale[] localeArray = Locale.getAvailableLocales();
        for (int i2 = 0; i2 != localeArray.length; ++i2) {
            if (!"en".equalsIgnoreCase(localeArray[i2].getLanguage())) continue;
            return localeArray[i2];
        }
        return Locale.getDefault();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static Date epochAdjust(Date date) throws ParseException {
        Locale locale = Locale.getDefault();
        if (locale == null) {
            return date;
        }
        Map map = localeCache;
        synchronized (map) {
            Long l2 = (Long)localeCache.get(locale);
            if (l2 == null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
                long l3 = simpleDateFormat.parse("19700101000000GMT+00:00").getTime();
                l2 = LocaleUtil.longValueOf(l3);
                localeCache.put(locale, l2);
            }
            if (l2 != 0L) {
                return new Date(date.getTime() - l2);
            }
            return date;
        }
    }

    private static Long longValueOf(long l2) {
        return Longs.valueOf(l2);
    }
}

