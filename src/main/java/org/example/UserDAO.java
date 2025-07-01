package org.example;
import java.util.List;
public interface UserDAO {
    boolean addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(String username);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean createTable(boolean dropExistingTable);
    User getUserById(int id);
    String encryptPassword(String password);
}