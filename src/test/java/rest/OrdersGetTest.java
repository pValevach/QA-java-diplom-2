package rest;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.apache.http.HttpStatus.*;

public class OrdersGetTest {

    private RestClient restClient;
    private UserClient userClient;
    private OrdersClient ordersClient;
    private String accessToken;
    private ValidatableResponse orderResponse;

    @Before
    public void setup() {
        restClient = new RestClient();
        userClient = new UserClient();
        ordersClient = new OrdersClient();

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
    @DisplayName("Positive get orders list w/ auth")
    public void get_w_Auth() {
        orderResponse = ordersClient.getOrders_w_Auth(accessToken);

        Assert.assertTrue(restClient.isStatusCodeOkFrom(orderResponse));
    }

    @Test
    @DisplayName("Negative get orders list w/o auth ")
    public void get_wo_Auth() {
        orderResponse = ordersClient.getOrders_wo_Auth();

        String expectedResponse = "You should be authorised";
        String actualResponse = restClient.getUnauthorizedErrorFrom(orderResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }
}
