package rest;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.Orders;

public class OrdersClient extends RestClient {

    private final String ROOT = "/orders";

    @Step("Send POST request to api/v1/orders w/ accessToken and save response")
    public ValidatableResponse createOrder_w_Auth(Orders orders, String token) {
        return reqSpec
                .auth().oauth2(getPureTokenFrom(token))
                .body(orders).log().all()
                .when()
                .post(ROOT)
                .then().log().all();
    }

    @Step("Send POST request to api/v1/orders w/o accessToken and save response")
    public ValidatableResponse createOrder_wo_Auth(Orders orders) {
        return reqSpec
                .body(orders).log().all()
                .when()
                .post(ROOT)
                .then().log().all();
    }


    @Step("Send GET request to api/v1/orders w/ accessToken and save response")
    public ValidatableResponse getOrders_w_Auth(String token) {
        return reqSpec
                .auth().oauth2(getPureTokenFrom(token))
                .get(ROOT)
                .then().log().all();
    }

    @Step("Send GET request to api/v1/orders w/o accessToken and save response")
    public ValidatableResponse getOrders_wo_Auth() {
        return reqSpec
                .get(ROOT)
                .then().log().all();
    }

    public String getPureTokenFrom(String token) {
        return token.substring(7);
    }
}

