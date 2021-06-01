package service;

import com.google.gson.Gson;
import model.Cats;
import model.FavCats;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CatService {
    public static void watchCats() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        //Cut the brackets
        String json = response.body().string();
        json = json.substring(1, json.length() - 1);

        //Phrase from json to object
        Gson gson = new Gson();
        Cats cats = gson.fromJson(json, Cats.class);

        //Resize image
        try {
            //Connect to the url
            URL url = new URL(cats.getUrl());
            //Catch the image
            Image image = ImageIO.read(url);
            //Turn into icon
            ImageIcon imageIcon = new ImageIcon();
            imageIcon.setImage(image);
            //If need, resize
            if (imageIcon.getIconHeight() > 800) {
                Image base = imageIcon.getImage();
                Image converted = base.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(converted);
            }

            String menu = "Options: \n"
                    + " 1. Watch another image \n"
                    + " 2. Favourite \n"
                    + " 3. Back \n";

            String[] buttons = {"Watch another image", "Favourite", "Back"};
            String opcion = (String) JOptionPane.showInputDialog(null, menu, cats.getId(),
                    JOptionPane.INFORMATION_MESSAGE, imageIcon, buttons, buttons[0]);

            int seleccion = -1;
            //validamos que opcion selecciona el usuario
            for (int i = 0; i < buttons.length; i++) {
                if (opcion.equals(buttons[i])) {
                    seleccion = i;
                }
            }

            switch (seleccion) {
                case 0:
                    watchCats();
                    break;
                case 1:
                    setFavoriteCat(cats);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setFavoriteCat(Cats cat) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.format("{\r\n    \"image_id\":\"%s\"\r\n}", cat.getId()));
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", cat.getApiKey())
                    .build();

            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getFavoriteCats(String apiKey) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apiKey)
                .build();
        Response response = client.newCall(request).execute();

        String json = response.body().string();
        Gson gson = new Gson();
        FavCats[] cats = gson.fromJson(json, FavCats[].class);

        if (cats.length > 0) {
            int min = 1;
            int max = cats.length;
            int random = (int) (Math.random() * ((max - min) + 1)) + min;
            int index = random - 1;

            FavCats favCat = cats[index];

            try {
                URL url = new URL(favCat.getImage().getUrl());
                Image image = ImageIO.read(url);

                ImageIcon imageIcon = new ImageIcon();
                imageIcon.setImage(image);

                if (imageIcon.getIconWidth() > 800) {
                    Image base = imageIcon.getImage();
                    Image converted = base.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(converted);
                }

                String menu = "Options: \n"
                        + " 1. Watch another favourite \n"
                        + " 2. Delete a favourite \n"
                        + " 3. Back \n";

                String[] buttons = {"Watch another favourite", "Detele a favourite", "Back"};
                String option = (String) JOptionPane.showInputDialog(null, menu, favCat.getId(),
                        JOptionPane.INFORMATION_MESSAGE, imageIcon, buttons, buttons[0]);

                int selection = -1;
                for (int i = 0; i < buttons.length; i++) {
                    if (option.equals(buttons[i])) {
                        selection = i;
                    }
                }

                switch (selection) {
                    case 0:
                        getFavoriteCats(apiKey);
                        break;
                    case 1:
                        deleteFavourite(favCat);
                        break;
                    default:
                        break;
                }

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public static void deleteFavourite(FavCats favCats) throws IOException {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\":\"5or\"\r\n}");
            Request request = new Request.Builder()
                    .url(String.format("https://api.thecatapi.com/v1/favourites/%s", favCats.getId()))
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", favCats.getApiKey())
                    .build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println(e.getMessage());        }
    }

}
