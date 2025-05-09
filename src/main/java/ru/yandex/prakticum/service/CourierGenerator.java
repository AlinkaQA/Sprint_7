package ru.yandex.prakticum.service;

import com.github.javafaker.Faker;
import ru.yandex.prakticum.model.Courier;

public class CourierGenerator {

    private final Faker faker;

    public CourierGenerator() {
        // Инициализация Faker для генерации случайных данных
        this.faker = new Faker();
    }

    public Courier getRandom() {
        // Генерация случайных данных для курьера
        String login = "courier_" + faker.internet().uuid();  // Генерация случайного логина
        String password = faker.internet().password();       // Генерация случайного пароля
        String firstName = faker.name().firstName();           // Генерация случайного имени

        // Создаем объект курьера с сгенерированными данными
        return new Courier(login, password, firstName);
    }
}
