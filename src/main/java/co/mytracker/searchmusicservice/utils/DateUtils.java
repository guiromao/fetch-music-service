package co.mytracker.searchmusicservice.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final int ONLY_YEAR_LENGTH = 4;
    private static final int YEAR_AND_MONTH_LENGTH = 7;

    public static LocalDate localDateOf(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; //January is 0
        int year = calendar.get(Calendar.YEAR);

        return LocalDate.of(year, month, day);
    }

    public static Date stringToDate(String dateStr) {
        try {
            String format = selectFormat(dateStr);
            return new SimpleDateFormat(format).parse(dateStr);
        } catch (java.text.ParseException e) {
            throw new IllegalArgumentException("Date given in an incorrect format: " + e);
        }
    }

    private static String selectFormat(String date) {
        String format;

        switch (date.length()) {
            case ONLY_YEAR_LENGTH: format = "yyyy"; break;
            case YEAR_AND_MONTH_LENGTH: format = "yyyy-MM"; break;
            default: format = "yyyy-MM-dd"; break;
        }

        return format;
    }

}
