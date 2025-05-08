package ru.yandex.prakticum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.prakticum.client.OrderClient;
import ru.yandex.prakticum.model.Order;
import ru.yandex.prakticum.service.OrderGenerator;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@RunWith(Parameterized.class)
public class OrderCreateTest {

    private static final String FIELD_TRACK = "track";

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final Integer rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    private final OrderClient orderClient = new OrderClient();
    private final OrderGenerator generator = new OrderGenerator();
    private Integer trackId;

    public OrderCreateTest(String firstName, String lastName, String address, String metroStation, String phone,
                           Integer rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment != null ? comment : "";
        this.color = color != null ? color : new String[]{};
    }

    @Parameterized.Parameters(name = "{index}: comment = {7}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {"Светлана", "Кузнецова", "ул. Садовая, д.3", "Площадь Ильича", "89998887766", 1, "2025-08-01", "Черный", new String[]{"BLACK"}},
                {"Артур", "Мартынов", "ул. Озерная, д.12", "Бауманская", "89998887767", 2, "2025-08-02", "Серый", new String[]{"GREY"}},
                {"Элина", "Гончарова", "ул. Тихая, д.8", "Сокол", "89998887768", 3, "2025-08-03", "Оба цвета", new String[]{"BLACK", "GREY"}},
                {"Игорь", "Миронов", "ул. Южная, д.15", "Речной вокзал", "89998887769", 4, "2025-08-04", "Без цвета", new String[]{}},
                {"Людмила", "Соловьева", "ул. Полевая, д.22", "Таганская", "89998887760", 5, "2025-08-05", "", new String[]{}}
        };
    }

    @After
    public void deleteOrder() {
        if (trackId != null && trackId > 0) {
            cancelOrder(trackId);
        }
    }

    @Test
    @DisplayName("Создание заказа (позитивные кейсы)")
    public void createOrderTest() {
        Order order = generateOrder();
        Response response = sendCreateOrderRequest(order);
        trackId = extractTrackId(response);
        assertOrderCreated(response);
    }

    @Step("Генерация заказа из параметров")
    public Order generateOrder() {
        Order order = generator.getOrder(firstName, lastName, address, metroStation, phone,
                rentTime, deliveryDate, comment, color);
        log.info("Создание заказа: {}", order);
        return order;
    }

    @Step("Отправка запроса на создание заказа")
    public Response sendCreateOrderRequest(Order order) {
        Response response = orderClient.createOrder(order);
        log.info("Ответ от сервера: {}", response.body().asString());
        return response;
    }

    @Step("Проверка, что заказ успешно создан (код 201 и track != null)")
    public void assertOrderCreated(Response response) {
        response.then().statusCode(HttpStatus.SC_CREATED)
                .and().assertThat().body(FIELD_TRACK, notNullValue());
    }

    @Step("Извлечение номера заказа (track)")
    public Integer extractTrackId(Response response) {
        Integer track = response.body().path(FIELD_TRACK);
        log.info("Создан заказ №: {}", track);
        return track;
    }

    @Step("Отмена заказа по номеру: {track}")
    public void cancelOrder(int track) {
        Response response = orderClient.cancelOrder(track);
        log.info("Ответ на отмену заказа: {}", response.body().asString());

        // Поддерживаем 200 (успешно), 400 (не нашёлся), 404 (не существует)
        response.then().statusCode(anyOf(equalTo(200), equalTo(400), equalTo(404)));
    }

}

