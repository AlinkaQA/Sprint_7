package ru.yandex.prakticum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import ru.yandex.prakticum.client.CourierClient;
import ru.yandex.prakticum.model.Courier;
import ru.yandex.prakticum.model.CourierCredentials;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest extends BaseTest {

    private final CourierClient courierClient = new CourierClient();
    private Courier createdCourier;

    @After
    public void tearDown() {
        if (createdCourier != null) {
            CourierCredentials creds = new CourierCredentials(
                    createdCourier.getLogin(),
                    createdCourier.getPassword()
            );
            Response login = courierClient.loginCourier(creds);
            Integer id = login.body().path("id");

            if (id != null) {
                courierClient.deleteCourier(id);
            }
        }
    }

    @Test
    @DisplayName("Курьера можно создать с валидными данными")
    public void shouldCreateCourierSuccessfully() {
        createdCourier = new Courier("courier_" + UUID.randomUUID(), "1234", "TestName");

        courierClient.createCourier(createdCourier)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух курьеров с одинаковым логином")
    public void shouldNotAllowDuplicateCourier() {
        String login = "courier_" + UUID.randomUUID();
        createdCourier = new Courier(login, "1234", "First");

        // 1. Создать первого
        courierClient.createCourier(createdCourier)
                .then().statusCode(HttpStatus.SC_CREATED);

        // 2. Попробовать создать второго с тем же логином
        Courier duplicateCourier = new Courier(login, "abcd", "Second");

        courierClient.createCourier(duplicateCourier)
                .then()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void shouldNotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, "pass123", "NoLogin");

        courierClient.createCourier(courier)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    public void shouldNotCreateCourierWithoutPassword() {
        Courier courier = new Courier("courier_" + UUID.randomUUID(), null, "NoPassword");

        courierClient.createCourier(courier)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без имени")
    public void shouldCreateCourierWithoutName() {
        createdCourier = new Courier("courier_" + UUID.randomUUID(), "pass123", null);

        courierClient.createCourier(createdCourier)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина и пароля")
    public void shouldNotCreateCourierWithoutLoginAndPassword() {
        Courier courier = new Courier(null, null, "NoLoginPass");

        courierClient.createCourier(courier)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
