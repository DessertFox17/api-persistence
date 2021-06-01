import model.Cats;
import service.CatService;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int menuOption = -1;
        String[] buttons = {"1. Watch cats", "2. Favourites","3. Exit"};

        do {
            String option = (String) JOptionPane.showInputDialog(null, "Java model.Cats", "Main menu",
                    JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);

            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])) menuOption = i;
            }

            switch (menuOption){
                case 0:
                    CatService.watchCats();
                    break;
                case 1:
                    Cats cats = new Cats();
                    CatService.getFavoriteCats(cats.getApiKey());
                default:
                    break;
            }
        } while (menuOption != 2);
    }
}
