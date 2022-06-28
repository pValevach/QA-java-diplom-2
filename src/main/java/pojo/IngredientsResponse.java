package pojo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class IngredientsResponse {
    private boolean success;
    private List<Ingredient> data;
}
