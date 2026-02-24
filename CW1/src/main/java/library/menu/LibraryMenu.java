package library.menu;

import library.DBOutput;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class LibraryMenu {
    private final DBOutput output;

    
    public LibraryMenu() {
        output = new DBOutput();
    }

    public void display() throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Підменю 'Бібліотеки'");
            System.out.println("1. Вивести всі бібліотеки");
            System.out.println("2. Вивести всіх читачів");
            System.out.println("3. Вивести дані про відвідуваність");
            System.out.println("4. Повернутися назад");
            System.out.print("Оберіть опцію: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    output.displayLibraries();
                    break;
                case 2:
                    output.displayReaders();
                    break;
                case 3:
                    output.displayVisits();
                    break;
                case 4:
                    System.out.println("Повернення до головного меню.");
                    break;
                default:
                    System.out.println("Некоректний вибір. Спробуйте ще раз.");
            }
        } while (choice != 4);
    }
}
