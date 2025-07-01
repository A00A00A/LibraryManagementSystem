package org.example.sqlite;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.example.AbstractAuthenticatedAction;
import org.example.AbstractAuthenticatedAction.Role;
import org.example.DatabaseConfig;
import org.example.User;
import org.example.UserDAO;
public class SQLiteUserDAO implements UserDAO {
    private DatabaseConfig config = new SQLiteDatabaseConfig();
    @Override
    public boolean addUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.config.getConnection();
            String insertQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, user.getUsername());
            // 调用encryptPassword方法，对用户输入的密码进行加密
            String encryptedPassword = encryptPassword(user.getPassword());
            // 将加密后的密码作为参数，保存到数据库中
            preparedStatement.setString(2, encryptedPassword);
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean updateUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.config.getConnection();
            String updateQuery = "UPDATE users SET password = ?, role = ? WHERE username = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            // 调用encryptPassword方法，对用户输入的密码进行加密
            String encryptedPassword = encryptPassword(user.getPassword());
            // 将加密后的密码作为参数，更新到数据库中
            preparedStatement.setString(1, encryptedPassword);
            preparedStatement.setString(2, user.getRole().toString());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    // 添加一个辅助方法，用于对用户密码进行加密
    public String encryptPassword(String password) {
        try {
            // 创建一个MessageDigest对象，指定使用SHA-256算法
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            // 将StringBuilder对象转换为字符串，即为最终的哈希值，赋值给一个字符串变量
            String encryptedPassword = sb.toString();
            // 返回加密后的密码
            return encryptedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
    @Override
    public boolean deleteUser(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.config.getConnection();
            String deleteQuery = "DELETE FROM users WHERE username = ?";
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public User getUserByUsername(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            connection = this.config.getConnection();
            String selectQuery = "SELECT * FROM users WHERE username = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                String roleStr = resultSet.getString("role");
                AbstractAuthenticatedAction.Role role = AbstractAuthenticatedAction.Role.valueOf(roleStr);
                user.setRole(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }
    @Override
    public List<User> getAllUsers() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> userList = new ArrayList<>();
        try {
            connection = this.config.getConnection();
            String selectQuery = "SELECT * FROM users";
            preparedStatement = connection.prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                String roleStr = resultSet.getString("role");
                AbstractAuthenticatedAction.Role role = AbstractAuthenticatedAction.Role.valueOf(roleStr);
                user.setRole(role);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userList;
    }
    @Override
    public boolean createTable(boolean dropExistingTable) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = this.config.getConnection();
            statement = connection.createStatement();
            if (dropExistingTable) {
                String dropQuery = "DROP TABLE IF EXISTS users";
                statement.executeUpdate(dropQuery);
            }
            String createQuery = "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL UNIQUE," + // 添加 UNIQUE 约束
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL" +
                    ")";
            statement.executeUpdate(createQuery);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("创建用户表时出错：" + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public User getUserById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.config.getConnection();
            String query = "SELECT username, password, role FROM users WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id); // 设置查询参数
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // 从结果集中获取用户信息
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                // 创建并返回一个 User 对象
                return new User(username, password, Role.valueOf(role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("根据 id 查询用户时出错：" + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null; // 如果没有找到用户，返回 null
    }
}