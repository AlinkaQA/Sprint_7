package ru.yandex.prakticum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.prakticum.client.CourierClient;
import ru.yandex.prakticum.model.Courier;
import ru.yandex.prakticum.model.CourierCredentials;
import ru.yandex.prakticum.service.CourierGenerator;

import static org.hamcrest.Matchers.*;

public class LoginTest extends BaseTest {
    private final CourierClient courierClient = new CourierClient();
    private final CourierGenerator generator = new CourierGenerator();
    private Courier courier;

    @Before
    public void setUp() {
        courier = generator.getRandom();
        courierClient.createCourier(courier);
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void courierCanLogin() {
        CourierCredentials creds = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response response = courierClient.loginCourier(creds);
        response.then().statusCode(HttpStatus.SC_OK)
                .and().body("id", notNullValue());
    }

    @Test
    @DisplayName("Нельзя авторизоваться без логина")
    public void loginWithoutLogin() {
        Response response = courierClient.loginCourierWithoutLogin(courier.getPassword());
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться без пароля")
    public void loginWithoutPassword() {
        CourierCredentials creds = new CourierCredentials(courier.getLogin(), "");
        Response response = courierClient.loginCourier(creds);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and().body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("Нельзя авторизоваться с несуществующим пользователем")
    public void loginWithInvalidCreds() {
        CourierCredentials creds = new CourierCredentials("nonexistent_user", "wrongpassword");
        Response response = courierClient.loginCourier(creds);
        response.then().statusCode(HttpStatus.SC_NOT_FOUND)
                .and().body("message", equalTo("Учетная запись не найдена"));
    }
}
