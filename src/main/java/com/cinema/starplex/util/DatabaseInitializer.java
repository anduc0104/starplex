//package com.cinema.starplex.util;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class DatabaseInitializer {
//    private static final String DB_URL = "jdbc:postgresql://localhost:5432/";
//    private static final String DB_NAME = "starplex";
//    private static final String DB_USER = "postgres";
//    private static final String DB_PASSWORD = "12345677";
//    private static final String MIGRATIONS_FOLDER = "src/main/resources/db/migration/";
//
//    public static void initializeDatabase() throws SQLException, IOException {
//        // Kết nối không có database để kiểm tra database tồn tại
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//             Statement stmt = conn.createStatement()) {
//
//            // Kiểm tra xem database đã tồn tại chưa
//            ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname ='" + DB_NAME + "'");
//            if (!rs.next()) {
//                // Nếu không tồn tại, tạo database
//                System.out.println("Database '" + DB_NAME + "' không tồn tại. Đang tạo...");
//                stmt.executeUpdate("CREATE DATABASE " + DB_NAME);
//                System.out.println("Database '" + DB_NAME + "' đã được tạo thành công!");
//            }
//            System.out.println(conn);
//        }
//
//        // Kết nối lại với database vừa tạo để thực hiện migration
//        try (Connection conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
//             Statement stmt = conn.createStatement()) {
//
//            // Lấy danh sách file migration
//            List<Path> sqlFiles = Files.list(Paths.get(MIGRATIONS_FOLDER))
//                    .filter(path -> path.toString().endsWith(".sql"))
//                    .sorted(Comparator.comparing(Path::getFileName))
//                    .collect(Collectors.toList());
//
//            // Chạy từng file SQL
//            for (Path file : sqlFiles) {
//                System.out.println("Running migration: " + file.getFileName());
//                String sql = new String(Files.readAllBytes(file));
////                stmt.executeUpdate(sql);
//            }
//
//            System.out.println("All migrations executed successfully!");
//        }
//    }
//}
