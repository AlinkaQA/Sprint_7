package ru.yandex.prakticum.service;

import ru.yandex.prakticum.model.Courier;

import java.util.UUID;

public class CourierGenerator {

    public Courier getRandom() {
        String login = "courier_" + UUID.randomUUID();
        String password = "1234";
        String firstName = "TestName";
        return new Courier(login, password, firstName);
    }
}
