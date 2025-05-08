package ru.yandex.prakticum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.prakticum.model.Courier;
import ru.yandex.prakticum.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String COURIER_PATH = "/api/v1/courier";

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Логин курьера")
    public Response loginCourier(CourierCredentials creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(COURIER_PATH + "/login");
    }

    @Step("Логин без логина")
    public Response loginCourierWithoutLogin(String password) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body("{ \"password\": \"" + password + "\" }")
                .when()
                .post(COURIER_PATH + "/login");
    }

    @Step("Удаление курьера по ID")
    public Response deleteCourier(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }
}
