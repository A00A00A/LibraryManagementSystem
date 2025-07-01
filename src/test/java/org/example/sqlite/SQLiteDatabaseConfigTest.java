package org.example.sqlite;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class SQLiteDatabaseConfigTest {
    private SQLiteDatabaseConfig config;
    private Connection connection;
    @Before
    public void setUp() {
        config = new SQLiteDatabaseConfig();
        connection = config.getConnection();
    }
    @After
    public void tearDown() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testConnectionAndCalculation() {
        try {
            Statement statement = connection.createStatement();
            String sqlQuery = "SELECT 2 + 3"; // 计算 2 + 3
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            int result = 0;
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            assertEquals(5, result); // 检查计算结果是否为 5
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}