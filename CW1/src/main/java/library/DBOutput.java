package library;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBOutput extends PostgresSQL {
    private static final Logger logger = Logger.getLogger(DBOutput.class.getName());

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

    
    protected void printLine(BufferedWriter writer, Vector<Integer> columnWidths) throws IOException {
        writer.write("+");
        for (int width : columnWidths) {
            writer.write("-".repeat(width + 2) + "+");
        }
        writer.newLine();
    }

    protected String padRight(String str, int length) {
        if (str.length() < length) {
            return String.format("%-" + length + "s", str);
        } else {
            return str.length() > length ? str.substring(0, length - 3) + "..." : str;
        }
    }

    
    public DBOutput() {
        super();
    }

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

    public void displayVisits() {
        String query = "SELECT " +
                "v.reader_id, r.name AS reader_name, l.name AS library_name, b.title AS book_title, v.visit_time " +
                "FROM visits v " +
                "JOIN readers r ON v.reader_id = r.id " +
                "JOIN libraries l ON v.library_id = l.id " +
                "LEFT JOIN books b ON v.book_id = b.id";

        displayQueryResults(query, List.of("reader_id", "reader_name", "library_name", "book_title", "visit_time"));
    }
}

