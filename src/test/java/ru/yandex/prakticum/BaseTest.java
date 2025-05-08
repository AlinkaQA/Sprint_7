package ru.yandex.prakticum;

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
            Response loginResponse = courierClient.loginCourier(creds);
            Integer courierId = loginResponse.body().path("id");
            if (courierId != null) {
                courierClient.deleteCourier(courierId);
            }
        }
    }
}

