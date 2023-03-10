package user;
import skeleton.*;
import base.*;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UsersOrder extends BaseSite {

    @Step("Создание заказа авторизованного пользователя")
    public ValidatableResponse createOrderAuthorizedUser(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .log().all()
                .post(Paths.ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Создание заказа неавторизованного пользователя")
    public ValidatableResponse createOrderUnauthorizedUser(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .log().all()
                .post(Paths.ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Получение заказа авторизованного пользователя")
    public ValidatableResponse getOrderAuthorizedUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(Paths.ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Получение заказа неавторизованного пользователя")
    public ValidatableResponse getOrderUnauthorizedUser() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(Paths.ORDER_PATH)
                .then()
                .log().all();
    }
}
