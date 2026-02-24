package library.menu;

import library.DBOutput;
import library.Search;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class BookMenu {
    private final DBOutput output;
    private final Search search = new Search();

    
    public BookMenu() {
        output = new DBOutput();
    }

    public void display() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Підменю 'Книги'");
            System.out.println("1. Вивести всі книги");
            System.out.println("2. Вивести книги в порядку найпопулярніші");
            System.out.println("3. Повернутися назад");
            System.out.print("Оберіть опцію: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    output.displayBooks();
                    break;
                case 2:
                    search.performPopularBookSearch();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Некоректний вибір. Спробуйте ще раз.");
            }
        } while (choice != 3);
    }
}

