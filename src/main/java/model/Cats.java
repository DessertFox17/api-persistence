package model;

import lombok.Data;

@Data
public class Cats {
    private String id;
    private String url;
    private final String apiKey = "57eb8315-74e8-4071-9e1a-58a92bf2bbef";
    private String image;
}
