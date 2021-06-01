package model;

import lombok.Data;

@Data
public class FavCats {
    private String id;
    private String image_id;
    private final String apiKey = "57eb8315-74e8-4071-9e1a-58a92bf2bbef";
    private Imagex image;
}
