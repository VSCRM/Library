package library;

import java.io.*;
import java.sql.*;
import java.util.Vector;
import java.awt.Desktop;

public class Search extends DBOutput {

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

    
    public Search() {
        super();
    }

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
}

