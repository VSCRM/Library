package library;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Клас {@code DBOutput} забезпечує виведення результатів SQL-запитів до текстових файлів.
 * <p>
 * Клас успадковує {@code PostgresSQL} для роботи з базою даних PostgresSQL, надаючи можливість
 * виконувати запити до бази даних та зберігати отримані результати у вигляді таблиць в текстовому файлі.
 * Також включає методи для виведення даних з різних таблиць бази даних, таких як книги, читачі, бібліотеки
 * та відвідування, з використанням форматованого вигляду.
 * </p>
 *
 * Методи:
 * <ul>
 *   <li>{@link #DBOutput()} - Конструктор класу, який ініціалізує підключення до бази даних.</li>
 *   <li>{@link #printLine(BufferedWriter, Vector)} - Формує лінію для таблиці, використовуючи ширину колонок.
 *       <ul>
 *         <li>{@code @param writer} - Потік для запису в файл.</li>
 *         <li>{@code @param columnWidths} - Вектор, що містить ширину для кожної колонки.</li>
 *         <li>{@code @throws IOException} - Якщо виникла помилка при записі.</li>
 *       </ul>
 *   </li>
 *   <li>{@link #padRight(String, int)} - Доповнює рядок пробілами до потрібної довжини або обрізає його.
 *       <ul>
 *         <li>{@code @param str} - Рядок, який потрібно доповнити або обрізати.</li>
 *         <li>{@code @param length} - Довжина, до якої потрібно доповнити або обрізати рядок.</li>
 *         <li>{@code @return} - Рядок, доповнений або обрізаний до заданої довжини.</li>
 *       </ul>
 *   </li>
 *   <li>{@link #displayBooks()} - Виводить таблицю з інформацією про книги.</li>
 *   <li>{@link #displayReaders()} - Виводить таблицю з інформацією про читачів.</li>
 *   <li>{@link #displayLibraries()} - Виводить таблицю з інформацією про бібліотеки.</li>
 *   <li>{@link #displayVisits()} - Виводить таблицю з інформацією про відвідування бібліотек.</li>
 *   <li>{@link #displayQueryResults(String, List)} - Виконує SQL-запит і виводить результати в текстовий файл.
 *       <ul>
 *         <li>{@code @param query} - SQL-запит, результати якого потрібно вивести.</li>
 *         <li>{@code @param columns} - Список колонок, які потрібно вивести.</li>
 *       </ul>
 *       <p>
 *       Принцип роботи:
 *       <ul>
 *           <li>Спершу виконується SQL-запит для отримання даних через `ResultSet`.</li>
 *           <li>Використовується прокручуваний `ResultSet` (`TYPE_SCROLL_INSENSITIVE`), який дозволяє багаторазово
 *               проходити результати для обчислення максимальних ширин колонок та форматованого виводу даних.</li>
 *           <li>У першому проході по `ResultSet` обчислюється максимальна ширина кожного стовпця для належного форматування.</li>
 *           <li>Дані форматуються і записуються у файл, включаючи заголовок таблиці, дані та розділові лінії між рядками.</li>
 *           <li>Файл `output.txt` автоматично відкривається за допомогою системного текстового редактора, якщо це підтримується.</li>
 *       </ul>
 *       </p>
 *   </li>
 * </ul>
 */
public class DBOutput extends PostgresSQL {
    private static final Logger logger = Logger.getLogger(DBOutput.class.getName());

    /**
     * Конструктор класу {@code DBOutput}, який ініціалізує підключення до бази даних.
     * Цей конструктор викликає конструктор суперкласу {@code PostgresSQL},
     * що налаштовує з'єднання з базою даних.
     */
    public DBOutput() {
        super();
    }

    /**
     * Формує лінію для таблиці, використовуючи ширину колонок.
     * <p>
     * Цей метод генерує лінію для таблиці, додаючи до кожної колонки
     * відповідну кількість дефісів для відображення коректного форматування.
     * </p>
     *
     * @param writer Потік для запису в файл.
     * @param columnWidths Вектор, що містить ширину для кожної колонки.
     * @throws IOException Якщо виникла помилка при записі.
     */
    protected void printLine(BufferedWriter writer, Vector<Integer> columnWidths) throws IOException {
        writer.write("+");
        for (int width : columnWidths) {
            writer.write("-".repeat(width + 2) + "+");
        }
        writer.newLine();
    }

    /**
     * Доповнює рядок пробілами до потрібної довжини або обрізає його, якщо він надто довгий.
     * <p>
     * Якщо рядок менший за зазначену довжину, до нього додаються пробіли з правого боку.
     * Якщо рядок більший, він обрізається до вказаної довжини з додаванням трьох крапок.
     * </p>
     *
     * @param str Рядок, який потрібно доповнити або обрізати.
     * @param length Довжина, до якої потрібно доповнити або обрізати рядок.
     * @return Рядок, доповнений або обрізаний до заданої довжини.
     */
    protected String padRight(String str, int length) {
        if (str.length() < length) {
            return String.format("%-" + length + "s", str);
        } else {
            return str.length() > length ? str.substring(0, length - 3) + "..." : str;
        }
    }

    /**
     * Виводить таблицю з даними про книги в текстовий файл.
     * <p>
     * Використовує {@link #displayQueryResults(String, List)} для отримання даних
     * про книги та їх виведення у файл.
     * </p>
     */
    public void displayBooks() {
        String query = "SELECT " +
                "B.ID, B.TITLE, BA.AUTHOR_ID, A.NAME AS AUTHOR_NAME, BD.GENRE, BD.PUBLISHER, BD.YEAR, BD.ISBN " +
                "FROM BOOKS B " +
                "JOIN BOOK_AUTHORS BA ON B.ID = BA.BOOK_ID " +
                "JOIN AUTHORS A ON BA.AUTHOR_ID = A.ID " +
                "JOIN BOOK_DETAILS BD ON B.ID = BD.BOOK_ID";

        displayQueryResults(query, List.of("ID", "TITLE", "AUTHOR_ID", "AUTHOR_NAME", "GENRE", "PUBLISHER",
                "YEAR", "ISBN"));
    }

    /**
     * Виводить таблицю з даними про читачів в текстовий файл.
     * <p>
     * Використовує {@link #displayQueryResults(String, List)} для отримання даних
     * про читачів та їх виведення у файл.
     * </p>
     */
    public void displayReaders() {
        String query = "SELECT " +
                "r.id AS reader_id, r.name AS reader_name, c.id AS category_id, c.category_type AS category_type, " +
                "cd.institution_name, cd.major, cd.school_number, cd.kindergarten_name, cd.research_field " +
                "FROM readers r " +
                "JOIN category_details cd ON r.id = cd.reader_id " +
                "JOIN categories c ON cd.category_id = c.id";

        displayQueryResults(query, List.of("reader_id", "reader_name", "category_id", "category_type",
                "institution_name", "major", "school_number", "kindergarten_name", "research_field"));
    }

    /**
     * Виводить таблицю з даними про бібліотеки в текстовий файл.
     * <p>
     * Використовує {@link #displayQueryResults(String, List)} для отримання даних
     * про бібліотеки та їх виведення у файл.
     * </p>
     */
    public void displayLibraries() {
        String query = "SELECT " +
                "l.id AS library_id, l.name AS library_name, l.location AS library_location, " +
                "lb.id AS librarian_id, lb.name AS librarian_name, " +
                "ws.start_day, ws.end_day, ws.start_time, ws.end_time " +
                "FROM libraries l " +
                "JOIN work_schedule ws ON l.id = ws.library_id " +
                "JOIN librarians lb ON ws.worker_id = lb.id";

        displayQueryResults(query, List.of("library_id", "library_name", "library_location", "librarian_id",
                "librarian_name", "start_day", "end_day", "start_time", "end_time"));
    }

    /**
     * Виводить таблицю з даними про відвідування бібліотек в текстовий файл.
     * <p>
     * Використовує {@link #displayQueryResults(String, List)} для отримання даних
     * про відвідування бібліотек та їх виведення у файл.
     * </p>
     */
    public void displayVisits() {
        String query = "SELECT " +
                "v.reader_id, r.name AS reader_name, l.name AS library_name, b.title AS book_title, v.visit_time " +
                "FROM visits v " +
                "JOIN readers r ON v.reader_id = r.id " +
                "JOIN libraries l ON v.library_id = l.id " +
                "LEFT JOIN books b ON v.book_id = b.id";

        displayQueryResults(query, List.of("reader_id", "reader_name", "library_name", "book_title", "visit_time"));
    }

    /**
     * Виводить результати SQL-запиту у файл у вигляді таблиці з форматованими стовпцями.
     *
     * <p>
     * Метод виконує SQL-запит, отримує результати, обчислює максимальну ширину кожного
     * стовпця для форматування, а потім записує ці дані у файл `output.txt`. Таблиця
     * з результатами SQL-запиту представлена у форматованому вигляді, де кожен рядок
     * і кожен стовпець вирівнюються за допомогою `|`. Після створення та збереження файлу
     * відкриває файл у системному текстовому редакторі, якщо це підтримується.
     * </p>
     *
     * <p>
     * Принцип роботи:
     * <ul>
     *   <li>Спершу виконується SQL-запит для отримання даних через `ResultSet`.</li>
     *   <li>Використовується прокручуваний `ResultSet` (`TYPE_SCROLL_INSENSITIVE`), який дозволяє багаторазово
     *       проходити результати для обчислення максимальних ширин колонок та форматованого виводу даних.</li>
     *   <li>У першому проході по `ResultSet` обчислюється максимальна ширина кожного стовпця для належного форматування.</li>
     *   <li>Дані форматуються і записуються у файл, включаючи заголовок таблиці, дані та розділові лінії між рядками.</li>
     *   <li>Файл `output.txt` автоматично відкривається за допомогою системного текстового редактора, якщо це підтримується.</li>
     * </ul>
     * </p>
     *
     * @param query SQL-запит, результати якого потрібно вивести.
     * @param columns Список колонок, які потрібно вивести.
     */
    private void displayQueryResults(String query, List<String> columns) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(query))  {

            Vector<Integer> columnWidths = new Vector<>(columns.size());
            for (String column : columns) {
                columnWidths.add(column.length());
            }
            while (rs.next()) {
                for (int i = 0; i < columns.size(); i++) {
                    String data = rs.getString(i + 1);
                    if (data != null && data.length() > columnWidths.get(i)) {
                        columnWidths.set(i, data.length());
                    }
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) {
                printLine(writer, columnWidths);
                writer.write("| " + padRight("id", columnWidths.get(0)) + " |");
                for (int i = 1; i < columns.size(); i++) {
                    writer.write(" " + padRight(columns.get(i), columnWidths.get(i)) + " |");
                }
                writer.newLine();
                printLine(writer, columnWidths);

                rs.beforeFirst();
                while (rs.next()) {
                    writer.write("| " + padRight(rs.getString(1), columnWidths.get(0)) + " |");
                    for (int i = 1; i < columns.size(); i++) {
                        String data = rs.getString(i + 1);
                        writer.write(" " + padRight(data != null ? data : "NULL", columnWidths.get(i)) + " |");
                    }
                    writer.newLine();
                    printLine(writer, columnWidths);
                }
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File("output.txt"));
            }
        } catch (SQLException | IOException e) {
            logger.log(Level.SEVERE, "Помилка при виконанні запиту", e);
        }
    }
}
