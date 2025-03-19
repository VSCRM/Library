package library.menu;

import library.PostgresSQL;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Клас {@code MainMenu} представляє головне меню програми для взаємодії з підменю
 * книг, бібліотек та відновлення даних.
 * <p>
 * Клас містить методи для відображення головного меню та обробки вибору користувача,
 * надаючи доступ до відповідних підменю {@code BookMenu}, {@code LibraryMenu}.
 * </p>
 *
 * Методи:
 * <ul>
 *   <li>{@link #MainMenu()} - Конструктор класу, який ініціалізує підменю книг, бібліотек та відновлення даних.
 *   </li>
 *
 *   <li>{@link #display()} - Відображає головне меню й обробляє введення користувача.
 *       <ul>
 *         <li>{@code @throws InputMismatchException} якщо користувач вводить некоректний тип даних.</li>
 *         <li>{@code @throws SQLException} якщо виникає помилка при доступі до бази даних.</li>
 *         <li>{@code @throws IOException} якщо виникає помилка при роботі з файлами.</li>
 *       </ul>
 *   </li>
 * </ul>
 */
public class MainMenu {
    private final BookMenu bookMenu;
    private final LibraryMenu libraryMenu;
    private final PostgresSQL sql;

    /**
     * Конструктор {@code MainMenu} ініціалізує підменю {@code BookMenu}, {@code LibraryMenu}.
     */
    public MainMenu() {
        bookMenu = new BookMenu();
        libraryMenu = new LibraryMenu();
        sql = PostgresSQL.getInstance();
    }

    /**
     * Метод {@code display()} відображає головне меню, надаючи користувачу можливість вибору дій з підменю книг,
     * бібліотек або відновлення даних. Обробляє введення користувача в нескінченному циклі до вибору виходу.
     * <p>
     * Варіанти меню:
     * <ul>
     *   <li>1 - Відкрити підменю "Книги" для перегляду та управління книгами.</li>
     *   <li>2 - Відкрити підменю "Бібліотеки" для перегляду та управління бібліотеками.</li>
     *   <li>3 - Відкрити підменю "Відновлення" для роботи з резервними копіями даних.</li>
     *   <li>4 - Вихід з програми.</li>
     * </ul>
     *
     * @throws InputMismatchException якщо користувач вводить некоректний тип даних.
     * @throws SQLException якщо виникає помилка при доступі до бази даних.
     * @throws IOException якщо виникає помилка при роботі з файлами.
     */
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
