package ru.yandex.prakticum;

import io.qameta.allure.Step;
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
        createCourier(courier);
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void courierCanLogin() {
        CourierCredentials creds = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response response = loginCourier(creds);
        assertSuccessfulLogin(response);
    }

    @Test
    @DisplayName("Нельзя авторизоваться без логина")
    public void loginWithoutLogin() {
        Response response = loginCourierWithoutLogin(courier.getPassword());
        assertErrorResponse(response, HttpStatus.SC_BAD_REQUEST, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Нельзя авторизоваться без пароля")
    public void loginWithoutPassword() {
        CourierCredentials creds = new CourierCredentials(courier.getLogin(), "");
        Response response = loginCourier(creds);
        assertErrorResponse(response, HttpStatus.SC_BAD_REQUEST, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Нельзя авторизоваться с несуществующим пользователем")
    public void loginWithInvalidCreds() {
        CourierCredentials creds = new CourierCredentials("nonexistent_user", "wrongpassword");
        Response response = loginCourier(creds);
        assertErrorResponse(response, HttpStatus.SC_NOT_FOUND, "Учетная запись не найдена");
    }

    @Step("Создание курьера: {courier}")
    private void createCourier(Courier courier) {
        courierClient.createCourier(courier);
    }

    @Step("Авторизация курьера: {creds}")
    private Response loginCourier(CourierCredentials creds) {
        return courierClient.loginCourier(creds);
    }

    @Step("Авторизация без логина")
    private Response loginCourierWithoutLogin(String password) {
        return courierClient.loginCourierWithoutLogin(password);
    }

    @Step("Проверка успешной авторизации")
    private void assertSuccessfulLogin(Response response) {
        response.then().statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue());
    }

    @Step("Проверка ошибки: статус {statusCode}, сообщение '{message}'")
    private void assertErrorResponse(Response response, int statusCode, String message) {
        response.then().statusCode(statusCode)
                .body("message", equalTo(message));
    }
}
