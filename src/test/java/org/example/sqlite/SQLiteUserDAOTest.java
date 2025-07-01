package org.example.sqlite;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.example.AbstractAuthenticatedAction.Role;
import org.example.DatabaseConfig;
import org.example.User;
import org.example.UserDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class SQLiteUserDAOTest {
    private UserDAO userDao;
    private DatabaseConfig config = new SQLiteDatabaseConfig();
    @Before
    public void setUp() {
        userDao = new SQLiteUserDAO();
        userDao.createTable(true);
    }
    @After
    public void tearDown() {
        // 执行一些清理操作
    }
    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setRole(Role.STUDENT); // 设置角色
        // 添加用户
        userDao.addUser(user);
        // 查询数据库，检查是否添加成功
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username='testUser'");
            assertTrue(resultSet.next()); // 确保查询结果存在
            assertEquals("testUser", resultSet.getString("username"));
            assertEquals(userDao.encryptPassword("testPassword"), resultSet.getString("password"));
            assertEquals(Role.STUDENT.toString(), resultSet.getString("role"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("newPassword");
        user.setRole(Role.TUTOR); // 设置新角色
        // 添加用户
        userDao.addUser(user);
        // 更新用户信息
        user.setPassword("updatedPassword");
        user.setRole(Role.ADMINISTRATOR); // 设置新角色
        userDao.updateUser(user);
        // 查询数据库，检查是否更新成功
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username='testUser'");
            assertTrue(resultSet.next()); // 确保查询结果存在
            assertEquals("testUser", resultSet.getString("username"));
            assertEquals(userDao.encryptPassword("updatedPassword"), resultSet.getString("password"));
            assertEquals(Role.ADMINISTRATOR.toString(), resultSet.getString("role"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUsername("userToDelete");
        user.setPassword("password");
        user.setRole(Role.TUTOR); // 设置角色
        // 添加用户
        userDao.addUser(user);
        // 查询数据库，确保用户添加成功
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username='userToDelete'");
            assertTrue(resultSet.next()); // 确保查询结果存在
            assertEquals("userToDelete", resultSet.getString("username"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 删除用户
        userDao.deleteUser(user.getUsername()); // 假设 User 类有一个 getId() 方法获取用户 ID
        // 再次查询数据库，确保用户已被删除
        connection = null;
        statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username='userToDelete'");
            assertFalse(resultSet.next()); // 确保查询结果不存在
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testGetUserByUsername() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setRole(Role.STUDENT); // 设置角色
        // 添加用户
        userDao.addUser(user);
        // 查询数据库，检查是否添加成功
        User retrievedUser = userDao.getUserByUsername("testUser");
        assertNotNull(retrievedUser); // 确保检索到用户
        assertEquals("testUser", retrievedUser.getUsername());
        assertEquals(userDao.encryptPassword("testPassword"), retrievedUser.getPassword());
        assertEquals(Role.STUDENT, retrievedUser.getRole());
    }
    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole(Role.STUDENT);
        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setRole(Role.TUTOR);
        // 添加用户
        userDao.addUser(user1);
        userDao.addUser(user2);
        // 查询数据库，检查是否添加成功
        List<User> userList = userDao.getAllUsers();
        assertNotNull(userList); // 确保返回的用户列表不为空
        assertEquals(2, userList.size()); // 确保列表中有两个用户
        // 检查每个用户的信息
        User retrievedUser1 = userList.get(0);
        assertEquals("user1", retrievedUser1.getUsername());
        assertEquals(userDao.encryptPassword("password1"), retrievedUser1.getPassword());
        assertEquals(Role.STUDENT, retrievedUser1.getRole());
        User retrievedUser2 = userList.get(1);
        assertEquals("user2", retrievedUser2.getUsername());
        assertEquals(userDao.encryptPassword("password2"), retrievedUser2.getPassword());
        assertEquals(Role.TUTOR, retrievedUser2.getRole());
    }
    @Test
    public void testCreateTable() {
        userDao = new SQLiteUserDAO();
        userDao.createTable(true); // 传入 true 表示删除已存在的表
        // 检查表是否创建成功
        Connection connection = null;
        Statement statement = null;
        try {
            connection = this.config.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement
                    .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='users'");
            assertTrue(resultSet.next());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}