package store.itpick.backend.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

public class DateUtils {

    public static String getDate(LocalDate localDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy_MM_dd");
        return localDate.format(dateTimeFormatter);
    }

    public static LocalDate getLocalDate(String date) {
        int year = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(2, 4));
        int day = Integer.parseInt(date.substring(4, 6));
        return LocalDate.of(year, month, day);
    }

    public static LocalDate getMondayOfPreviousWeek() {
        LocalDate aWeekAgo = LocalDate.now().minusDays(7);
        int diffFromMonday = aWeekAgo.getDayOfWeek().getValue() - 1;   // 월요일 0, 화요일 1, ..., 일요일 6
        return aWeekAgo.minusDays(diffFromMonday);
    }

    public static LocalDate stringToLocalDate(String date) {
        int year = Integer.parseInt("20" + date.substring(0, 2));
        int month = Integer.parseInt(date.substring(2, 4));
        int day = Integer.parseInt(date.substring(4, 6));

        return LocalDate.of(year, month, day);
    }

    public static String localDateToString(LocalDate localDate) {
        int year = localDate.getYear() % 100;
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        return year + formatMonth(month) + formatMonth(day);
    }

    public static String getWeek(LocalDate localDate) {
        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);

        int weekOfMonth = localDate.get(weekFields.weekOfMonth());

        // 첫 주에 해당하지 않는 주의 경우 전 달 마지막 주차로 계산
        if (weekOfMonth == 0) {
            // 전 달의 마지막 날 기준
            LocalDate lastDayOfLastMonth = localDate.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);
            return getWeek(lastDayOfLastMonth);
        }

        // 이번 달의 마지막 날 기준
        LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        // 마지막 주차의 경우 마지막 날이 월~수 사이이면 다음달 1주차로 계산
        if (weekOfMonth == lastDayOfMonth.get(weekFields.weekOfMonth()) && lastDayOfMonth.getDayOfWeek().compareTo(DayOfWeek.THURSDAY) < 0) {
            LocalDate firstDayOfNextMonth = lastDayOfMonth.plusDays(1); // 마지막 날 + 1일 => 다음달 1일
            return getWeek(firstDayOfNextMonth);
        }

        return localDate.getYear() % 100 + "_" + formatMonth(localDate.getMonthValue()) + "_week" + weekOfMonth;
    }

//    public static String getCurrentWeek(LocalDate localDate) {
//        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
//        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
//
//        int weekOfMonth = localDate.get(weekFields.weekOfMonth());
//
//        // 첫 주에 해당하지 않는 주의 경우 전 달 마지막 주차로 계산
//        if (weekOfMonth == 0) {
//            // 전 달의 마지막 날 기준
//            LocalDate lastDayOfLastMonth = localDate.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);
//            return getCurrentWeek(lastDayOfLastMonth);
//        }
//
//        // 이번 달의 마지막 날 기준
//        LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
//        // 마지막 주차의 경우 마지막 날이 월~수 사이이면 다음달 1주차로 계산
//        if (weekOfMonth == lastDayOfMonth.get(weekFields.weekOfMonth()) && lastDayOfMonth.getDayOfWeek().compareTo(DayOfWeek.THURSDAY) < 0) {
//            LocalDate firstDayOfNextMonth = lastDayOfMonth.plusDays(1); // 마지막 날 + 1일 => 다음달 1일
//            return getCurrentWeek(firstDayOfNextMonth);
//        }
//
//        return localDate.getYear() % 100 + "_" + formatMonth(localDate.getMonthValue()) + "_week" + weekOfMonth;
//    }

    private static String formatMonth(int month) {
        if (month >= 10) {
            return String.valueOf(month);
        }
        return "0" + month;
    }
}
