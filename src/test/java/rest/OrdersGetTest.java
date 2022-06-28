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
    private UserClient userClient;
    private OrdersClient ordersClient;

    private String accessToken;
    private String expectedResponse;
    private String actualResponse;

    private ValidatableResponse orderResponse;

    @Before
    public void setup() {
        userClient = new UserClient();
        ordersClient = new OrdersClient();

        User user = User.getRandom();

        accessToken = getAccessTokenFrom(userClient.register(user)
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

        Assert.assertTrue(isGetOrdersResponseOkFrom(orderResponse));
    }

    @Test
    @DisplayName("Negative get orders list w/o auth ")
    public void get_wo_Auth() {
        orderResponse = ordersClient.getOrders_wo_Auth();

        expectedResponse = "You should be authorised";
        actualResponse = getUnauthErrorFrom(orderResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Step("Get accessToken")
    public String getAccessTokenFrom(ValidatableResponse response) {
        return response
                .extract()
                .path("accessToken");
    }

    @Step("Checking for 200 status code and saving 'success' field from get response")
    public boolean isGetOrdersResponseOkFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("success");
    }

    @Step("Checking for 401 status code and saving 'message' field from get response")
    public String getUnauthErrorFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .path("message");
    }
}
