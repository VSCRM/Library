package library;

import library.menu.MainMenu;
import java.sql.SQLException;
import java.io.IOException;

/**
 * Клас {@code Main} є точкою входу в програму.
 * <p>
 * У цьому класі викликається головне меню програми через {@code MainMenu}, де користувач може вибирати різні опції
 * для роботи з бібліотечною системою, такі як редагування даних, виведення інформації, чи інші функції.
 * </p>
 *
 * Методи:
 * <ul>
 *   <li>{@link #main(String[])} - Точка входу в програму, яка викликає головне меню та обробляє можливі виключення.
 *       <ul>
 *         <li>{@code @param args} Аргументи командного рядка (не використовуються в цьому класі).</li>
 *         <li>{@code @throws SQLException} Якщо виникає помилка при роботі з базою даних.</li>
 *         <li>{@code @throws IOException} Якщо виникає помилка при вводу/виводу.</li>
 *       </ul>
 *   </li>
 * </ul>
 */
public class Main {

    /**
     * Точка входу в програму.
     * Запускає головне меню для взаємодії з користувачем.
     *
     * @param args Аргументи командного рядка (не використовуються в цьому класі).
     */
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
