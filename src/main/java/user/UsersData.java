package user;
import skeleton.*;
import base.*;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UsersData extends BaseSite {
    @Step("Регистрация пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .log().all()
                .post(Paths.USER_PATH + "register")
                .then()
                .log().all();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(Login login) {
        return given()
                .spec(getBaseSpec())
                .body(login)
                .log().all()
                .post(Paths.USER_PATH + "login")
                .then()
                .log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .body(accessToken)
                .log().all()
                .delete(Paths.USER_PATH + "user")
                .then()
                .log().all();
    }

    @Step("Обновление данных авторизованного пользователя")
    public ValidatableResponse updateAuthorizedUser(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(user)
                .log().all()
                .patch(Paths.USER_PATH + "user")
                .then()
                .log().all();
    }

    @Step("Обновление данных неавторизованного пользователя")
    public ValidatableResponse updateUnauthorizedUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .log().all()
                .patch(Paths.USER_PATH + "user")
                .then()
                .log().all();
    }
}