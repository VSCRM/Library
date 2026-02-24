package library.menu;

import library.PostgresSQL;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenu {
    private final BookMenu bookMenu;
    private final LibraryMenu libraryMenu;
    private final PostgresSQL sql;

    
    public MainMenu() {
        bookMenu = new BookMenu();
        libraryMenu = new LibraryMenu();
        sql = PostgresSQL.getInstance();
    }

    public void display() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Головне меню");
            System.out.println("1. Показати підменю 'Книги'");
            System.out.println("2. Показати підменю 'Бібліотеки'");
            System.out.println("3. Вихід");
            System.out.print("Оберіть опцію: ");
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        bookMenu.display();
                        break;
                    case 2:
                        libraryMenu.display();
                        break;
                    case 3:
                        System.out.println("Вихід з програми.");
                        sql.close();
                        break;
                    default:
                        System.out.println("Некоректний вибір. Спробуйте ще раз.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Введено некоректний тип даних. Введіть, будь ласка, число.");
                scanner.next();
                choice = -1;
            }
        } while (choice != 3);
    }
}

