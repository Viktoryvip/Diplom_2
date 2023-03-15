package ordertest;
import user.*;
import skeleton.*;
import org.junit.*;
import base.*;
import java.util.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetOrderTest {

    List<String> ingredients = Arrays.asList(
            "61c0c5a71d1f82001bdaaa6c", //булка
            "61c0c5a71d1f82001bdaaa75", //соус
            "61c0c5a71d1f82001bdaaa71", //котлета
            "61c0c5a71d1f82001bdaaa7a", //сыр
            "61c0c5a71d1f82001bdaaa6c"  //булка
            );
    private String accessToken;
    private UsersData usersData;
    private UsersOrder usersOrder;
    private User user;
    private Order order;

    @Before
    public void setUp() {
        usersData = new UsersData();
        user = UserGenerator.getRandom();
        usersData.create(user);
        usersOrder = new UsersOrder();
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
    @DisplayName("Получение заказа авторизованного пользователя")
    public void getOrderAuthorizedUser() {
        Login login = new Login(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = usersData.login(login);
        accessToken = loginResponse.extract().path("accessToken");
        int statusCodeLogin = loginResponse
                .body("success", equalTo(true))
                .extract()
                .statusCode();
        assertThat(statusCodeLogin, equalTo(SC_OK));

        order = new Order(ingredients);
        ValidatableResponse orderResponse = usersOrder.createOrderAuthorizedUser(order, accessToken);
        int statusCodeOrder = orderResponse
                .body("success", equalTo(true))
                .extract()
                .statusCode();
        assertThat(statusCodeOrder, equalTo(SC_OK));

        ValidatableResponse getResponseAuthorizedUser = usersOrder.getOrderAuthorizedUser(accessToken);
        int statusCodeGetOrder = getResponseAuthorizedUser
                .body("success", equalTo(true))
                .extract()
                .statusCode();
        assertThat(statusCodeGetOrder, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Получение заказа неавторизованного пользователя")
    public void getOrderUnauthorizedUser() {
        order = new Order(ingredients);
        ValidatableResponse orderResponseAuthorizedUser = usersOrder.createOrderUnauthorizedUser(order);
        int statusCodeGetOrderAuthorizedUser = orderResponseAuthorizedUser
                .body("success", equalTo(true))
                .extract()
                .statusCode();
        assertThat(statusCodeGetOrderAuthorizedUser, equalTo(SC_OK));

        ValidatableResponse orderResponseUnauthorizedUser = usersOrder.getOrderUnauthorizedUser();
        int statusCodeGetOrderUnauthorizedUser = orderResponseUnauthorizedUser
                .body("success", equalTo(false))
                .extract()
                .statusCode();
        System.out.println(statusCodeGetOrderUnauthorizedUser);
        assertThat(statusCodeGetOrderUnauthorizedUser, equalTo(SC_UNAUTHORIZED));
    }
}

