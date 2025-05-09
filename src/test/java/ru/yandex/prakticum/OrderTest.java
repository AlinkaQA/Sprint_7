package ru.yandex.prakticum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Test;
import ru.yandex.prakticum.client.OrderClient;

import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class OrderTest {

    private static final String RESPONSE = "Получен ответ от сервера: {}";
    public static final String FIELD_ORDERS = "orders";
    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Получение списка заказов")
    public void getOrdersTest() {
        Response response = sendGetOrdersRequest();
        logResponse(response);
        checkResponseStatusAndBody(response);
    }

    @Step("Отправка запроса на получение списка заказов")
    public Response sendGetOrdersRequest() {
        return orderClient.getOrders();
    }

    @Step("Проверка кода ответа и тела: поле '{FIELD_ORDERS}' должно быть непустым")
    public void checkResponseStatusAndBody(Response response) {
        response.then().statusCode(HttpStatus.SC_OK)
                .and().assertThat().body(FIELD_ORDERS, notNullValue());
    }

    @Step("Логирование тела ответа")
    public void logResponse(Response response) {
        log.info(RESPONSE, response.body().asString());
    }
}


