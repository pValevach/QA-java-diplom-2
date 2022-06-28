package rest;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;

public class UserRegisterTest {
    private UserClient userClient;
    private User user;

    private String accessToken;
    private String expectedResponse;
    private String actualResponse;
    private boolean isUserCreated;

    private ValidatableResponse registerResponse;

    @Before
    public void setup() {
        userClient = new UserClient();
    }

    @After
    public void teardown() {
        if (isUserCreated) {
            userClient.deleteUserBy(accessToken);
        }
    }

    @Test
    @DisplayName("Positive user register w/ required fields")
    public void registerCorrect() {
        user = User.getRandom();

        registerResponse = userClient.register(user);
        accessToken = getAccessTokenFrom(registerResponse);

        isUserCreated = isRegisterResponseOkFrom(registerResponse);

        Assert.assertTrue(isUserCreated);
    }


    @Test
    @DisplayName("Negative user register w/ already existing user")
    public void register_w_SameUser() {
        user = User.getRandom();

        //First register
        registerResponse = userClient.register(user);
        isUserCreated = isRegisterResponseOkFrom(registerResponse);
        accessToken = getAccessTokenFrom(registerResponse);
        //Second register w/ the same user
        registerResponse = userClient.register(user);

        expectedResponse = "User already exists";
        actualResponse = getRegisterErrorFrom(registerResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Negative user register w/o required fields: email null")
    public void register_wo_Email() {
        user = new User(null, "password", "name");

        registerResponse = userClient.register(user);

        expectedResponse = "Email, password and name are required fields";
        actualResponse = getRegisterErrorFrom(registerResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Negative user register w/o required fields: password null")
    public void register_wo_Password() {
        user = new User("test@yandex.ru", null, "name");

        registerResponse = userClient.register(user);

        expectedResponse = "Email, password and name are required fields";
        actualResponse = getRegisterErrorFrom(registerResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Negative user register w/o required fields: name null")
    public void register_wo_Name() {
        user = new User("test@yandex.ru", "password", null);

        registerResponse = userClient.register(user);

        expectedResponse = "Email, password and name are required fields";
        actualResponse = getRegisterErrorFrom(registerResponse);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Step("Get accessToken")
    public String getAccessTokenFrom(ValidatableResponse response) {
        return response
                .extract()
                .path("accessToken");
    }

    @Step("Checking for 200 status code and saving 'success' field from register response")
    public boolean isRegisterResponseOkFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("success");
    }

    @Step("Checking for 403 status code and saving 'message' field from register response")
    public String getRegisterErrorFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .extract()
                .path("message");
    }
}


