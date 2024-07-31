package store.itpick.backend.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PeriodType {
    BY_REAL_TIME("by_real_time"),
    BY_DAY("yy_MM_dd"),
    BY_WEEK("yy_MM_week[0-9]");

    private final String periodType;

    public String get() {
        return periodType;
    }
}
