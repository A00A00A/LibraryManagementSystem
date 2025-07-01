package org.example;
import java.util.List;
public class UserService {
    private UserDAO userDao;
    public UserService(UserDAO userDao) {
        this.userDao = userDao;
    }
    public boolean addUser(User user) {
        return userDao.addUser(user);
    }
    public boolean updateUser(User user) {
        return userDao.updateUser(user);
    }
    public boolean deleteUser(String username) {
        return userDao.deleteUser(username);
    }
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
    public boolean createTable(boolean dropExistingTable) {
        return userDao.createTable(dropExistingTable);
    }
    // 验证用户名和密码是否正确
	public User login(String username, String password) {
		User user = userDao.getUserByUsername(username);
		if (user != null) {
			if (user.getPassword().equals(userDao.encryptPassword(password))) {
				return user;
			}
		}
		return null;
	}
}