package org.example.sqlite;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.example.DatabaseConfig;
public class SQLiteDatabaseConfig implements DatabaseConfig {
    private static final String DATABASE_URL = "jdbc:sqlite:film_hub_tutorial.db"; // 修改为你的数据库路径
    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC"); // 加载SQLite驱动
            connection = DriverManager.getConnection(DATABASE_URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}