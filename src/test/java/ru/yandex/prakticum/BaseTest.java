package ru.yandex.prakticum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.BeforeClass;
import ru.yandex.prakticum.client.CourierClient;
import ru.yandex.prakticum.model.Courier;
import ru.yandex.prakticum.model.CourierCredentials;
import ru.yandex.prakticum.service.Config;

public class BaseTest {
    protected Courier courier;
    protected CourierClient courierClient = new CourierClient();

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = Config.BASE_URL;
    }

    @After
    public void tearDown() {
        if (courier != null) {
            CourierCredentials creds = new CourierCredentials(
                    courier.getLogin(),
                    courier.getPassword()
            );
            Response loginResponse = loginCourier(creds);
            Integer courierId = loginResponse.body().path("id");
            if (courierId != null) {
                deleteCourier(courierId);
            }
        }
    }

    @Step("Авторизация курьера для удаления: {creds}")
    private Response loginCourier(CourierCredentials creds) {
        return courierClient.loginCourier(creds);
    }

    @Step("Удаление курьера по ID: {id}")
    private void deleteCourier(Integer id) {
        courierClient.deleteCourier(id);
    }
}

