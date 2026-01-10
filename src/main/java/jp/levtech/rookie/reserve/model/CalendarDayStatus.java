package jp.levtech.rookie.reserve.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalendarDayStatus {
    private LocalDate date;
    private String status; // "○", "△", "×"
}