package org.example;
import java.sql.Connection;
public interface DatabaseConfig {
    public Connection getConnection();
}