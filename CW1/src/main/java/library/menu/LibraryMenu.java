package library.menu;

import library.DBOutput;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Клас {@code LibraryMenu} представляє підменю для роботи з бібліотеками у бібліотечній системі.
 * <p>
 * Цей клас забезпечує інтерфейс для користувача, що дозволяє:
 * <ul>
 *   <li>Переглядати інформацію про всі бібліотеки</li>
 *   <li>Переглядати інформацію про читачів</li>
 *   <li>Переглядати відвідуваність бібліотек</li>
 *   <li>Повернутися до головного меню</li>
 * </ul>
 * Клас використовує об'єкт {@code Changes} для виведення
 * і редагування інформації про бібліотеки відповідно.
 * </p>
 *
 * Методи:
 * <ul>
 *   <li>{@link #LibraryMenu()} - Конструктор класу, який ініціалізує об'єкти {@code Changes} і {@code DBOutput}.</li>
 *   <li>{@link #display()} - Відображає підменю "Бібліотеки" і обробляє вибір користувача.
 *       <ul>
 *         <li>{@code @throws SQLException} Якщо виникає помилка при доступі до бази даних.</li>
 *         <li>{@code @throws IOException} Якщо виникає помилка при роботі з файлами.</li>
 *       </ul>
 *   </li>
 * </ul>
 */
public class LibraryMenu {
    private final DBOutput output;

    /**
     * Конструктор {@code LibraryMenu} ініціалізує об'єкт {@code DBOutput},
     * який використовуються для виведення та редагування інформації про бібліотеки.
     */
    public LibraryMenu() {
        output = new DBOutput();
    }

    /**
     * Метод {@code display()} відображає підменю для роботи з бібліотеками й обробляє вибір користувача.
     * <p>
     * Варіанти меню:
     * <ul>
     *   <li>1 - Вивести всі бібліотеки</li>
     *   <li>2 - Показати читачів</li>
     *   <li>3 - Показати відвідуваність</li>
     *   <li>4 - Повернутися назад</li>
     * </ul>
     * </p>
     * @throws SQLException Якщо виникає помилка при доступі до бази даних.
     * @throws IOException Якщо виникає помилка при роботі з файлами.
     */
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
