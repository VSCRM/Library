package library.menu;

import library.DBOutput;
import library.Search;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Клас {@code BookMenu} представляє підменю для роботи з книгами в бібліотечній системі.
 * <p>
 * Цей клас забезпечує інтерфейс для користувача, що дозволяє:
 * <ul>
 *   <li>Переглядати всі книги</li>
 *   <li>Переглядати книги в порядку популярності</li>
 *   <li>Повернутися до головного меню</li>
 * </ul>
 * Клас використовує об'єкт {@code DBOutput}, для виведення інформації про книги відповідно.
 * </p>
 * Методи:
 * <ul>
 *   <li>{@link #BookMenu()} - Конструктор класу, який ініціалізує об'єкт {@code DBOutput}.</li>
 *   <li>{@link #display()} - Відображає підменю "Книги" і обробляє вибір користувача.
 *       <ul>
 *         <li>{@code @throws SQLException} Якщо виникає помилка при доступі до бази даних.</li>
 *         <li>{@code @throws IOException} Якщо виникає помилка при роботі з файлами.</li>
 *       </ul>
 *   </li>
 * </ul>
 */
public class BookMenu {
    private final DBOutput output;
    private final Search search = new Search();

    /**
     * Конструктор {@code BookMenu} ініціалізує об'єкт {@code DBOutput},
     * який використовуються для виведення інформації про книги.
     */
    public BookMenu() {
        output = new DBOutput();
    }

    /**
     * Метод {@code display()} відображає підменю для роботи з книгами й обробляє вибір користувача.
     * <p>
     * Варіанти меню:
     * <ul>
     *   <li>1 - Вивести всі книги</li>
     *   <li>2 - Вивести книги в порядку найпопулярніші</li>
     *   <li>3 - Повернутися назад</li>
     * </ul>
     * </p>
     * @throws SQLException Якщо виникає помилка при доступі до бази даних.
     * @throws IOException Якщо виникає помилка при роботі з файлами.
     */
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
