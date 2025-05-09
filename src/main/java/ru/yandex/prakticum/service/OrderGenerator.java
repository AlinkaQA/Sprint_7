package ru.yandex.prakticum.service;

import ru.yandex.prakticum.model.Order;

public class OrderGenerator {
    public Order getOrder(String firstName, String lastName, String address, String metroStation, String phone,
                          Integer rentTime, String deliveryDate, String comment, String[] color) {
        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}
