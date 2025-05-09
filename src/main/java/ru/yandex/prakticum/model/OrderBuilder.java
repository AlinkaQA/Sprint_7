package ru.yandex.prakticum.model;

public class OrderBuilder {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public OrderBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public OrderBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public OrderBuilder address(String address) {
        this.address = address;
        return this;
    }

    public OrderBuilder metroStation(String metroStation) {
        this.metroStation = metroStation;
        return this;
    }

    public OrderBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public OrderBuilder rentTime(Integer rentTime) {
        this.rentTime = rentTime;
        return this;
    }

    public OrderBuilder deliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public OrderBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public OrderBuilder color(String[] color) {
        this.color = color;
        return this;
    }

    // Метод для создания объекта Order
    public Order build() {
        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}
