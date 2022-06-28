package pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Ingredient {
    private int calories;
    private int carbohydrates;
    private int fat;
    private String image;
    private String image_mobile;
    private String image_large;
    private String name;
    private int price;
    private int proteins;
    private String type;
    private int __v;
    private String _id;
}
