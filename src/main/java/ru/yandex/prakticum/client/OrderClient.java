package ru.yandex.prakticum.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.yandex.prakticum.model.Order;
import ru.yandex.prakticum.model.TrackRequest;
import ru.yandex.prakticum.service.Config;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String BASE_PATH = "/api/v1/orders";

    static {
        RestAssured.baseURI = Config.BASE_URL;
        RestAssured.useRelaxedHTTPSValidation();
    }


    private RequestSpecification spec() {
        return given()
                .baseUri(Config.BASE_URL)
                .header("Content-type", "application/json");
    }

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return spec()
                .body(order)
                .when()
                .post(BASE_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrders() {
        return spec()
                .when()
                .get(BASE_PATH);
    }

    @Step("Отмена заказа по треку: {track}")
    public Response cancelOrder(int track) {
        TrackRequest trackRequest = new TrackRequest(track);
        return spec()
                .body(trackRequest)
                .when()
                .put(BASE_PATH + "/cancel");
    }

    @Step("Создание заказа с raw JSON")
    public Response createOrder(String rawJson) {
        return spec()
                .body(rawJson)
                .when()
                .post(BASE_PATH);
    }
}

