package c195.util;

import c195.model.Appointment;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Jonathan Dowdell
 */
public class LocalDateTimeHelper {
    public static DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh");
    public static DateTimeFormatter formatter12Minute = DateTimeFormatter.ofPattern("mm");
    public static DateTimeFormatter formatter12AMPM = DateTimeFormatter.ofPattern("a");
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");

    /**
     * Get Hour from LocalDateTime using 12 Hour
     * @param localDateTime LocalDateTime
     * @return Hour String
     */
    public static String get12Hour(LocalDateTime localDateTime) {
        final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.format(formatter12Hour);
    }

    /**
     * Get Minute from LocalDateTime using 12 Hour
     * @param localDateTime LocalDateTime
     * @return Minute String
     */
    public static String get12Minute(LocalDateTime localDateTime) {
        final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.format(formatter12Minute);
    }

    /**
     * Get AMPM from LocalDateTime using 12 Hour
     * @param localDateTime LocalDateTime
     * @return AMPM String
     */
    public static String get12AMPM(LocalDateTime localDateTime) {
        final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.format(formatter12AMPM);
    }

    public static TableCell<Appointment, LocalDateTime> dateTimeCell(TableColumn<Appointment, LocalDateTime> column) {
        return new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime localDateTime, boolean b) {
                super.updateItem(localDateTime, b);
                if (b) {
                    setText(null);
                } else {
                    final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
                    setText(LocalDateTimeHelper.formatter.format(zonedDateTime));
                }
            }
        };
    }
}
