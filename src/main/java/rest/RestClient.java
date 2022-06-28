package rest;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class RestClient {

    protected final String URL = "https://stellarburgers.nomoreparties.site/api";

    protected final RequestSpecification reqSpec = given()
            .header("Content-Type", "application/json")
            .baseUri(URL);

    @Step("Checking for 200 status code and saving 'success' field from response")
    public boolean isStatusCodeOkFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("success");
    }

    @Step("Checking for 400 status code and saving 'message' field from response")
    public String getBadRequestErrorFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .extract()
                .path("message");
    }
    @Step("Checking for 401 status code and saving 'message' field from response")
    public String getUnauthorizedErrorFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .path("message");
    }

    @Step("Checking for 403 status code and saving 'message' field from response")
    public String getForbiddenErrorFrom(ValidatableResponse response) {
        return response
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .extract()
                .path("message");
    }
}