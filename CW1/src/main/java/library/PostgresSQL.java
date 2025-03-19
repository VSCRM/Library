package library;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Клас {@code PostgresSQL} надає функціональність для підключення до бази даних PostgresSQL
 * через пул з'єднань HikariCP.
 * <p>
 * Клас забезпечує методи для виконання SQL-запитів, отримання з'єднань з базою даних,
 * а також для закриття пулу з'єднань після завершення роботи. Це дозволяє ефективно працювати
 * з базою даних, мінімізуючи накладні витрати на створення нових з'єднань.
 * </p>
 * <p>
 * Клас реалізує шаблон Singleton, що гарантує наявність тільки одного екземпляра класу
 * протягом усього життєвого циклу програми.
 * </p>
 *
 * Методи:
 * <ul>
 *   <li>{@link #PostgresSQL()} - Конструктор класу, що ініціалізує пул з'єднань HikariCP.</li>
 *   <li>{@link #getInstance()} - Статичний метод для отримання єдиного екземпляра класу {@code PostgresSQL}.
 *       <ul>
 *         <li>{@code @return} Повертає єдиний екземпляр класу {@code PostgresSQL}, створений через шаблон Singleton.</li>
 *       </ul>
 *   </li>
 *   <li>{@link #getConnection()} - Повертає нове з'єднання з базою даних через пул з'єднань.
 *       <ul>
 *         <li>{@code @return} Об'єкт {@code Connection}, що представляє з'єднання з базою даних.</li>
 *         <li>{@code @throws SQLException} Якщо виникає помилка при отриманні з'єднання.</li>
 *       </ul>
 *   </li>
 *   <li>{@link #close()} - Закриває пул з'єднань після завершення роботи з базою даних.
 *       <ul>
 *         <li>{@code @return} Метод не повертає значення, але закриває ресурси пулу з'єднань.</li>
 *       </ul>
 *   </li>
 * </ul>
 */

public class PostgresSQL {

    /**
     * Пул з'єднань для доступу до бази даних PostgresSQL.
     */
    private static HikariDataSource dataSource;

    /**
     * Єдиний екземпляр класу {@code PostgresSQL}, що створюється за допомогою шаблону Singleton.
     */
    private static volatile PostgresSQL instance;

    /**
     * Логгер для виведення повідомлень.
     */
    private static final Logger LOGGER = Logger.getLogger(PostgresSQL.class.getName());

    /**
     * Конструктор класу {@code PostgresSQL}.
     * Ініціалізує пул з'єднань HikariCP з необхідною конфігурацією.
     * Викликається лише один раз при створенні екземпляра класу.
     * <p>
     * Конфігурація пулу включає:
     * <ul>
     *   <li>URL до бази даних.</li>
     *   <li>Ім'я користувача та пароль для підключення.</li>
     *   <li>Максимальний розмір пулу з'єднань (10 з'єднань).</li>
     * </ul>
     * </p>
     */
    protected PostgresSQL() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/Library");
        config.setUsername("postgres");
        config.setPassword("google24");
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    /**
     * Статичний метод для отримання єдиного екземпляра класу {@code PostgresSQL}.
     * Якщо екземпляр ще не створений, то він буде створений.
     *
     * @return Об'єкт {@code PostgresSQL} з налаштованим пулом з'єднань.
     */
    public static PostgresSQL getInstance() {
        if (instance == null) {
            synchronized (PostgresSQL.class) {
                if (instance == null) {
                    instance = new PostgresSQL();
                }
            }
        }
        return instance;
    }

    /**
     * Повертає нове з'єднання з базою даних з пулу з'єднань.
     * <p>
     * Цей метод забезпечує доступ до підключення для виконання SQL-запитів
     * з використанням пулу з'єднань HikariCP. Кожне з'єднання, отримане цим методом,
     * повинно бути закрите після використання для звільнення ресурсів пулу.
     * </p>
     *
     * @return Об'єкт {@code Connection}, що представляє з'єднання з базою даних.
     * @throws SQLException якщо виникає помилка при отриманні з'єднання.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Закриває пул з'єднань, коли він більше не потрібен.
     * Потрібно викликати після завершення всіх операцій з базою даних.
     * <p>
     * Цей метод закриває пул з'єднань і звільняє ресурси, що використовуються для підключення до бази даних.
     * </p>
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            try {
                dataSource.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error closing the data source: {0}", e.getMessage());
            }
        }
    }
}
