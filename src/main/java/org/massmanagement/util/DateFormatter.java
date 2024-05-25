package org.massmanagement.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static String formatDate(Timestamp timestamp) {

        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        // Format the Timestamp
        return dateFormat.format(timestamp);
    }

    public static LocalDate stringToLocalDate(String date) {

        // Create a SimpleDateFormat object with the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);
    }

    public static String formatDateTime(Timestamp timestamp) {

        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        // Format the Timestamp
        return dateFormat.format(timestamp);
    }

    public static String formatDate(LocalDate date) {
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return formatter.format(date);
    }
}
