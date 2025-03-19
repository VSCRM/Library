package library;

import java.io.*;
import java.sql.*;
import java.util.Vector;
import java.awt.Desktop;

/**
 * Клас {@code Search} надає функціональність для пошуку та відображення популярних книг за кількістю відвідувань.
 * Клас наслідує {@code DBOutput}, що забезпечує функціональність виведення результатів SQL-запитів у текстовий файл.
 * <p>
 * Клас виконує SQL-запит для пошуку найпопулярніших книг на основі кількості відвідувань, обробляє дані
 * та записує результат у текстовий файл {@code output.txt}. Після завершення пошуку файл автоматично відкривається
 * за допомогою стандартного додатка для текстових файлів.
 * </p>
 *
 * Методи:
 * <ul>
 *   <li>{@link #Search()} - Конструктор класу, який ініціалізує підключення до бази даних через {@code DBOutput}.</li>
 *   <li>{@link #performPopularBookSearch()} - Виконує пошук найпопулярніших книг за кількістю відвідувань
 *       у таблиці {@code visits} та виводить результат у файл {@code output.txt}.
 *       <ul>
 *         <li>{@code @param} - не має параметрів, виконується пошук популярних книг.</li>
 *       </ul>
 *   </li>
 *   <li>{@link #calculateColumnWidths(ResultSet, Vector)} - Обчислює максимальні ширини стовпців для форматування таблиці.
 *       <ul>
 *         <li>{@code @param rs} - Результат запиту з даними.</li>
 *         <li>{@code @param columns} - Список назв колонок для форматування.</li>
 *         <li>{@code @return} - Вектор з максимальними ширинами для кожної колонки.</li>
 *       </ul>
 *   </li>
 *   <li>{@link #openFileWithDefaultApp(String)} - Відкриває файл за допомогою стандартного додатка для цього типу файлів.
 *       <ul>
 *         <li>{@code @param fileName} - Назва файлу для відкриття.</li>
 *       </ul>
 *   </li>
 * </ul>
 */
public class Search extends DBOutput {

    /**
     * Конструктор {@code Search}, який ініціалізує підключення до бази даних через {@code DBOutput}.
     */
    public Search() {
        super();
    }

    /**
     * Виконує пошук найпопулярніших книг за кількістю відвідувань у таблиці {@code visits}.
     * Виводить книгу та кількість її відвідувань у файл {@code output.txt}.
     */
    public void performPopularBookSearch() {
        String query = """
            SELECT
            	B.TITLE,
            	COUNT(V.BOOK_ID) AS REPETITIONS
            FROM
            	VISITS V
            	JOIN BOOKS B ON V.BOOK_ID = B.ID
            GROUP BY
            	B.TITLE
            ORDER BY
            	REPETITIONS DESC
            LIMIT
                100;
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(query)) {

            Vector<String> columns = new Vector<>();
            columns.add("title");
            columns.add("repetitions");

            Vector<Integer> columnWidths = calculateColumnWidths(rs, columns);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
                printLine(writer, columnWidths);
                writer.write("| " + padRight("Назва книги", columnWidths.get(0)) + " | "
                             + padRight("Відвідування", columnWidths.get(1)) + " |");
                writer.newLine();
                printLine(writer, columnWidths);

                rs.beforeFirst();
                while (rs.next()) {
                    String title = rs.getString("title");
                    String repetitions = String.valueOf(rs.getInt("repetitions"));

                    writer.write("| " + padRight(title, columnWidths.get(0)) + " | "
                                 + padRight(repetitions, columnWidths.get(1)) + " |");
                    writer.newLine();
                    printLine(writer, columnWidths);
                }
            }
            openFileWithDefaultApp("output.txt");

            System.out.println("Натисніть Enter для продовження...");
            System.in.read();

        } catch (SQLException | IOException e) {
            System.err.println("Помилка при виконанні пошуку популярних книг: " + e.getMessage());
        } finally {
            File file = new File("output.txt");
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Файл успішно видалений.");
                } else {
                    System.err.println("Не вдалося видалити файл output.txt");
                }
            }
        }
    }

    /**
     * Обчислює максимальні ширини стовпців для форматування таблиці.
     *
     * @param rs Результат запиту з даними.
     * @param columns Список назв колонок.
     * @return Вектор з максимальними ширинами кожного стовпця.
     */
    private Vector<Integer> calculateColumnWidths(ResultSet rs, Vector<String> columns) throws SQLException {
        Vector<Integer> columnWidths = new Vector<>();
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
        return columnWidths;
    }

    /**
     * Відкриває файл за допомогою стандартного додатка для цього типу файлів.
     *
     * @param fileName Назва файлу для відкриття.
     */
    private void openFileWithDefaultApp(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists() && !file.isDirectory()) {
                Desktop.getDesktop().open(file);
            } else {
                System.err.println("Файл не знайдений: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Помилка при відкритті файлу: " + e.getMessage());
        }
    }
}
