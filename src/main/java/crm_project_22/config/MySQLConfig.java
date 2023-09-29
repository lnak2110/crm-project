package crm_project_22.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConfig {
//  Tạo kết nối CSDL
    public static Connection getConnection() {
        Connection connection = null;
//      Khai báo driver sử dụng cho CSDL tương ứng
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_crm", "root",
                    "1234");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database connection error: " + e.getLocalizedMessage());
        }
        return connection;
    }
}
