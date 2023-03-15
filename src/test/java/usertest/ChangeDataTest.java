package usertest;
import user.*;
import skeleton.*;
import org.junit.*;
import base.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ChangeDataTest {
    private String accessToken;
    private UsersData usersData;
    private User user;

    @Before
    public void setUp() {
        usersData = new UsersData();
        user = UserGenerator.getRandom();
        usersData.create(user);
    }

    @After
    public void tearDown() {
        try {
            usersData.delete(accessToken);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void changeDataForAuthorizedUserTest() {
        Login login = new Login(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = usersData.login(login);
        accessToken = loginResponse.extract().path("accessToken");
        ValidatableResponse updateResponse = usersData.updateAuthorizedUser(UserGenerator.getRandom(), accessToken);
        int statusCode = updateResponse
                .body("success", equalTo(true))
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changeDataForUnauthorizedUserTest() {
        ValidatableResponse updateResponse = usersData.updateUnauthorizedUser(UserGenerator.getRandom());
        int statusCode = updateResponse
                .body("success", equalTo(false))
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
    }
}