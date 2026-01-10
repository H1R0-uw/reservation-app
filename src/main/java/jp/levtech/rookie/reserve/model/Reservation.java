package jp.levtech.rookie.reserve.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Reservation {

    private Long id;

    // DBカラム: name_kanji
    private String nameKanji;

    // DBカラム: name_kana
    private String nameKana;

    // DBカラム: phone_number
    private String phoneNumber;

    // DBカラム: email
    private String email;

    // DBカラム: date
    private LocalDate date;

    // DBカラム: time
    private LocalTime time;

    // DBカラム: number_of_people
    private Integer numberOfPeople;

    // DBカラム: message（もし使うなら）
    private String message;
}