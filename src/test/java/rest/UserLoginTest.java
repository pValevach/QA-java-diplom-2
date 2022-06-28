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

public class UserLoginTest {

    private User user;
    private UserCredentials credentials;
    private RestClient restClient;
    private UserClient userClient;
    private String accessToken;
    private String expectedResponse;
    private String actualResponse;
    private ValidatableResponse loginResponse;

    @Before
    public void setup() {
        restClient = new RestClient();
        userClient = new UserClient();

        user = User.getRandom();
        accessToken = userClient.getAccessTokenFrom(userClient.register(user)
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

        Assert.assertTrue(restClient.isStatusCodeOkFrom(loginResponse));
    }

    @Test
    @DisplayName("Negative user login w/o valid credentials: email null")
    public void login_wo_Email() {
        credentials = UserCredentials.getCreds_wo_EmailFrom(user);
        loginResponse = userClient.loginBy(credentials);

        expectedResponse = "email or password are incorrect";
        actualResponse = restClient.getUnauthorizedErrorFrom(loginResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Negative user login w/o valid credentials: password null")
    public void login_wo_Password() {
        credentials = UserCredentials.getCreds_wo_PasswordFrom(user);
        loginResponse = userClient.loginBy(credentials);

        expectedResponse = "email or password are incorrect";
        actualResponse = restClient.getUnauthorizedErrorFrom(loginResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }
}
