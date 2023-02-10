package space.jachen.yygh.hosp.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * @author JaChen
 * @date 2023/2/10 11:34
 */
public class DateUtils {
    /**
     * 将Date日期（yyyy-MM-dd HH:mm）转换为DateTime
     */
    public static DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " "+ timeString;
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
    }
}
