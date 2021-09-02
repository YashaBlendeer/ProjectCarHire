package com.yashablendeer.carhire.util;

import java.time.format.DateTimeFormatter;

/**
 * Util interface to format date
 *
 * @author yaroslava
 * @version 1.0
 */

public interface DateUtil {
    static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
}
