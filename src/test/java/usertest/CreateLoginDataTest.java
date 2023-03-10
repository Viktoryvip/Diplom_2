package usertest;
import user.*;
import skeleton.*;
import org.junit.*;
import base.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateLoginDataTest {

    int statusCode;
    private UsersData usersData;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        usersData = new UsersData();
        user = UserGenerator.getRandom();
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
    @DisplayName("Создание уникального пользователя")
    public void createUserTest() {
        ValidatableResponse createResponse = usersData.create(user);
        statusCode = createResponse
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_OK));
        accessToken = createResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createRegistredUserTest() {
        ValidatableResponse createResponse1 = usersData.create(user);
        ValidatableResponse createResponse2 = usersData.create(user);
        statusCode = createResponse2
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_FORBIDDEN));
        accessToken = createResponse1
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameTest() {
        user.setName(null);
        statusCode = usersData.create(user)
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_FORBIDDEN));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailTest() {
        user.setEmail(null);
        statusCode = usersData.create(user)
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_FORBIDDEN));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordTest() {
        user.setPassword(null);
        statusCode = usersData.create(user)
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_FORBIDDEN));
    }
}