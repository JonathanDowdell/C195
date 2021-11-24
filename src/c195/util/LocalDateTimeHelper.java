package c195.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeHelper {
    public static DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh");
    public static DateTimeFormatter formatter12Minute = DateTimeFormatter.ofPattern("mm");
    public static DateTimeFormatter formatter12AMPM = DateTimeFormatter.ofPattern("a");


    public static String get12Hour(LocalDateTime localDateTime) {
        final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.format(formatter12Hour);
    }

    public static String get12Minute(LocalDateTime localDateTime) {
        final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.format(formatter12Minute);
    }

    public static String get12AMPM(LocalDateTime localDateTime) {
        final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.format(formatter12AMPM);
    }
}
