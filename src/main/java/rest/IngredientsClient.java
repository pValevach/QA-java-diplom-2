package rest;

import io.qameta.allure.Step;
import pojo.IngredientsResponse;

public class IngredientsClient extends RestClient {

    private final String ROOT = "/ingredients";

    @Step("Send GET request to api/ingredients and save response")
    public IngredientsResponse getIngredients() {
        return reqSpec
                .get(ROOT)
                .as(IngredientsResponse.class);
    }
}

