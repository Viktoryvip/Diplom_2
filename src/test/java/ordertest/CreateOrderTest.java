package ordertest;
import user.*;
import base.*;
import skeleton.*;
import org.junit.*;
import java.util.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
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
            usersData.delete(accessToken);        }
       catch (Throwable e) {
           e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Создание заказа после создания пользователя и его авторизации")
    public void createOrderAuthorizedUser() {
        Login login = new Login(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = usersData.login(login);
        accessToken = loginResponse.extract().path("accessToken");
        int statusCodeLogin = loginResponse
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .statusCode();

        order = new Order(ingredients);
        ValidatableResponse orderResponse = usersOrder.createOrderAuthorizedUser(order, accessToken);
        int statusCodeOrder = orderResponse
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .statusCode();
        assertThat(statusCodeLogin, equalTo(SC_OK));
        assertThat(statusCodeOrder, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderUnauthorizedUser() {
        order = new Order(ingredients);
        ValidatableResponse orderResponse = usersOrder.createOrderUnauthorizedUser(order);
        int statusCodeOrder = orderResponse
                .statusCode(200)
                .body("success", equalTo(true))
                .extract()
                .statusCode();
        assertThat(statusCodeOrder, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Создание заказа после создания пользователя и его авторизации, но без ингредиентов")
    public void createOrderWithoutIngredients() {
        Login login = new Login(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = usersData.login(login);
        accessToken = loginResponse.extract().path("accessToken");
        int statusCodeLogin = loginResponse
               .statusCode(200)
                .extract()
                .statusCode();

        order = new Order(null);
        ValidatableResponse orderResponse = usersOrder.createOrderAuthorizedUser(order, accessToken);
        int statusCodeOrder = orderResponse
                .statusCode(400)
                .body("success", equalTo(false))
                .extract()
                .statusCode();
        assertThat(statusCodeLogin, equalTo(SC_OK));
        assertThat(statusCodeOrder, equalTo(SC_BAD_REQUEST));
    }

    @Test
    @DisplayName("Создание заказа после создания пользователя и его авторизации, с неверным хешем ингредиентов")
    public void createOrderWithWrongHashIngredient() {
        Login login = new Login(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = usersData.login(login);
        accessToken = loginResponse.extract().path("accessToken");
        int statusCodeLogin = loginResponse
                .statusCode(200)
                .extract()
                .statusCode();

        order = new Order(List.of("60d3463f7034a000269f45e7"));
        ValidatableResponse orderResponse = usersOrder.createOrderAuthorizedUser(order, accessToken);
        int statusCodeOrder = orderResponse
                .statusCode(400)
                .body("success", equalTo(false))
                .extract()
                .statusCode();
        assertThat(statusCodeLogin, equalTo(SC_OK));
        assertThat(statusCodeOrder, equalTo(SC_BAD_REQUEST));
    }
}