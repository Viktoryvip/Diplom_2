package usertest;
import user.*;
import skeleton.*;
import org.junit.*;
import base.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LoginDataTest {

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
    @DisplayName("Логин под существующим пользователем")
    public void loginUserTest() {
        Login login = new Login(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = usersData.login(login);
        int statusCode = loginResponse
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_OK));
        accessToken = loginResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Логин с неверным логином")
    public void loginUserTestIncorrectEmail() {
        Login login = new Login("email", user.getPassword());
        ValidatableResponse loginResponse = usersData.login(login);
        int statusCode = loginResponse
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginUserTestIncorrectPassword() {
        Login login = new Login(user.getEmail(), "password");
        ValidatableResponse loginResponse = usersData.login(login);
        int statusCode = loginResponse
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
    }
}