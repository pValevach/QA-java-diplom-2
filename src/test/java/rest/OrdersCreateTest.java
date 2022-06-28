package rest;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.IngredientsResponse;
import pojo.Orders;
import pojo.User;

import java.util.List;

import static org.apache.http.HttpStatus.*;

public class OrdersCreateTest {

    private Orders orders;
    private RestClient restClient;
    private UserClient userClient;
    private OrdersClient ordersClient;
    private IngredientsClient ingredientsClient;
    private boolean isOrderCreated;
    private String accessToken;
    private String expectedResponse;
    private String actualResponse;
    private IngredientsResponse ingredients;
    private ValidatableResponse orderResponse;

    @Before
    public void setup() {
        restClient = new RestClient();
        userClient = new UserClient();
        ordersClient = new OrdersClient();
        ingredientsClient = new IngredientsClient();

        User user = User.getRandom();

        accessToken = userClient.getAccessTokenFrom(userClient.register(user)
                .assertThat()
                .statusCode(SC_OK));
    }

    @After
    public void teardown() {
        userClient.deleteUserBy(accessToken);
    }

    @Test
    @DisplayName("Positive creating order: w/ auth, w/ ingredients")
    public void create_w_Auth_w_Ingredients() {
        ingredients = ingredientsClient.getIngredients();

        orders = new Orders(List.of(ingredients.getData().get(0).get_id()));
        orderResponse = ordersClient.createOrder_w_Auth(orders, accessToken);

        isOrderCreated = restClient.isStatusCodeOkFrom(orderResponse);

        Assert.assertTrue(isOrderCreated);
    }

    @Test
    @DisplayName("Positive creating order: w/o auth, w/ ingredients")
    public void create_wo_Auth_w_Ingredients() {
        ingredients = ingredientsClient.getIngredients();

        orders = new Orders(List.of(ingredients.getData().get(0).get_id()));
        orderResponse = ordersClient.createOrder_wo_Auth(orders);

        isOrderCreated = restClient.isStatusCodeOkFrom(orderResponse);

        Assert.assertTrue(isOrderCreated);
    }

    @Test
    @DisplayName("Negative creating order: w/ auth, w/o ingredients")
    public void create_w_Auth_wo_Ingredients() {
        orders = new Orders(null);
        orderResponse = ordersClient.createOrder_w_Auth(orders, accessToken);

        expectedResponse = "Ingredient ids must be provided";
        actualResponse = userClient.getBadRequestErrorFrom(orderResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test()
    @DisplayName("Negative creating order: w/o auth, w/o ingredients")
    public void create_wo_Auth_wo_Ingredients() {
        orders = new Orders(null);
        orderResponse = ordersClient.createOrder_wo_Auth(orders);

        expectedResponse = "Ingredient ids must be provided";
        actualResponse = userClient.getBadRequestErrorFrom(orderResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Negative creating order: w/ auth, w/ wrong ingredient's id")
    public void create_w_Auth_w_WrongIngredients() {
        orders = new Orders(List.of("WRONG"));
        orderResponse = ordersClient.createOrder_w_Auth(orders, accessToken);

        checkResponseFor500From(orderResponse);
    }

    @Test
    @DisplayName("Negative creating order: w/o auth, w/ wrong ingredient's id")
    public void create_wo_Auth_w_WrongIngredients() {
        orders = new Orders(List.of("WRONG"));
        orderResponse = ordersClient.createOrder_wo_Auth(orders);

        checkResponseFor500From(orderResponse);
    }

    @Step("Checking for 500 status code from create response")
    public void checkResponseFor500From(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}