package com.example.jsonkiller2.dateTime;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class DateTimeTest {

    @Test
    void itReturnCorrectDateFormat() {
        final LocalDateTime dateTime = LocalDateTime.of(2005, 5, 14,
                0, 0, 0);
        final DateTime expectedDateTime = new DateTime(dateTime);
        final String expectedDate = "05/14/2005";
        final long expectedMillis = 1116046800000L;
        final String expectedTime = "00:00:00 AM";
        assertEquals(expectedDate, expectedDateTime.date);
        assertEquals(expectedTime, expectedDateTime.time);
        assertEquals(expectedMillis, expectedDateTime.milliseconds_since_epoch);
    }
}