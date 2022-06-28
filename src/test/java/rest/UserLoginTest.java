package rest;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import pojo.UserCredentials;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class UserLoginTest {
    private UserClient userClient;
    private UserCredentials credentials;
    private User user;

    private String accessToken;
    private String expectedResponse;
    private String actualResponse;

    private ValidatableResponse loginResponse;

    @Before
    public void setup() {
        userClient = new UserClient();

        user = User.getRandom();
        accessToken = getAccessTokenFrom(userClient.register(user)
                .assertThat()
                .statusCode(SC_OK));
    }

    @After
    public void teardown() {
        userClient.deleteUserBy(accessToken);
    }

    @Test
    @DisplayName("Positive user login w/ valid credentials")
    public void loginCorrect() {
        credentials = UserCredentials.getCredsFrom(user);
        loginResponse = userClient.loginBy(credentials);

        Assert.assertTrue(isLoginResponseOkFrom(loginResponse));
    }

    @Test
    @DisplayName("Negative user login w/o valid credentials: email null")
    public void login_wo_Email() {
        credentials = UserCredentials.getCreds_wo_EmailFrom(user);
        loginResponse = userClient.loginBy(credentials);

        expectedResponse = "email or password are incorrect";
        actualResponse = getLoginErrorFrom(loginResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Negative user login w/o valid credentials: password null")
    public void login_wo_Password() {
        credentials = UserCredentials.getCreds_wo_PasswordFrom(user);
        loginResponse = userClient.loginBy(credentials);

        expectedResponse = "email or password are incorrect";
        actualResponse = getLoginErrorFrom(loginResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Step("Get accessToken")
    public String getAccessTokenFrom(ValidatableResponse response) {
        return response
                .extract()
                .path("accessToken");
    }

    @Step("Checking for 200 status code and saving 'success' field from login response")
    public boolean isLoginResponseOkFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("success");
    }

    @Step("Checking for 401 status code and saving 'message' field from login response")
    public String getLoginErrorFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .path("message");
    }
}
