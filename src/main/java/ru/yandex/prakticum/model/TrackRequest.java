package ru.yandex.prakticum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс для передачи номера трека в теле запроса при отмене заказа.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackRequest {
    private int track;
}
