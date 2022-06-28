package rest;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class UserUpdateTest {
    private UserClient userClient;
    private User user;
    private User userEdited;

    private String accessToken;

    private ValidatableResponse updateResponse;

    @Before
    public void setup() {
        userClient = new UserClient();

        user = User.getRandom();
        userEdited = User.getRandom();

        accessToken = getAccessTokenFrom(userClient.register(user)
                .assertThat()
                .statusCode(SC_OK));
    }

    @After
    public void teardown() {
        userClient.deleteUserBy(accessToken);
    }

    @Test
    @DisplayName("Positive user update w/ auth")
    public void edit_w_Auth() {
        updateResponse = userClient.updateUser_w_Auth(userEdited, accessToken);

        Assert.assertTrue(isUpdateResponseOkFrom(updateResponse));
    }

    @Test
    @DisplayName("Negative user update w/o auth")
    public void edit_wo_Auth() {
        updateResponse = userClient.updateUser_wo_Auth(userEdited);

        Assert.assertFalse(isUpdateResponseFailureFrom((updateResponse)));
    }

    @Step("Get accessToken")
    public String getAccessTokenFrom(ValidatableResponse response) {
        return response
                .extract()
                .path("accessToken");
    }

    @Step("Checking for 200 status code and saving 'success' field from update response")
    public boolean isUpdateResponseOkFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("success");
    }

    @Step("Checking for 401 status code and saving 'success' field from update response")
    public boolean isUpdateResponseFailureFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .path("success");
    }
}

