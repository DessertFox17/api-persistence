package service;

import com.google.gson.Gson;
import model.Cats;
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

            String catId = cats.getId();

            String opcion = (String) JOptionPane.showInputDialog(null, menu, catId,
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
                    favoriteCat(cats);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void favoriteCat(Cats cat) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, String.format("{\r\n    \"image_id\":\"%s\"\r\n}",cat.getId()));
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

}
