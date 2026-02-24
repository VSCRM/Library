package library;

import library.menu.MainMenu;
import java.sql.SQLException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            MainMenu mainMenu = new MainMenu();
            mainMenu.display();
        } catch (SQLException e) {
            System.out.println("Помилка при роботі з базою даних: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Помилка при вводу/виводу: " + e.getMessage());
        }
    }
}
