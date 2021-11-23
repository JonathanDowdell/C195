package c195.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeWrapper implements Comparable<LocalDateTime> {

    private final LocalDateTime localDateTime;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

    public LocalDateTimeWrapper(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public int compareTo(LocalDateTime o) {
        return localDateTime.compareTo(o);
    }

    @Override
    public String toString() {
        return localDateTime.format(dateTimeFormatter);
    }
}
