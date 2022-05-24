package com.example.jsonkiller2.dateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;



public class DateTime {
    @JsonProperty
    final String time;
    @JsonProperty
    final long milliseconds_since_epoch;
    @JsonProperty
    final String date;

    public DateTime(LocalDateTime dateTime) {
        this.time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss a"));
        this.milliseconds_since_epoch = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.date = dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
}
