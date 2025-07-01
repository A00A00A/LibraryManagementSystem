package org.example;
import java.awt.GridLayout;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.example.AbstractAuthenticatedAction.Role;
class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton forgetButton;
    UserService userService;
    public RegisterFrame(UserService userService) {
        this.userService = userService;
        this.setTitle("注册");
        this.setSize(300, 200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10)); // 设置面板的布局为网格布局，5行2列，间隔为10
        JLabel usernameLabel = new JLabel("用户名：");
        usernameField = new JTextField();
        panel.add(usernameLabel);
        panel.add(usernameField);
        JLabel passwordLabel = new JLabel("密码：");
        passwordField = new JPasswordField();
        panel.add(passwordLabel);
        panel.add(passwordField);
        JLabel confirmPasswordLabel = new JLabel("确认密码：");
        confirmPasswordField = new JPasswordField();
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);
        JLabel emptyLabel1 = new JLabel("");
        registerButton = new JButton("注册");
        panel.add(emptyLabel1);
        panel.add(registerButton);
        JLabel emptyLabel2 = new JLabel("");
        forgetButton = new JButton("忘记密码");
        panel.add(emptyLabel2);
        panel.add(forgetButton);
        this.add(panel);
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            try {
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "所有字段均为必填项。", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (username.length() < 5) {
                    JOptionPane.showMessageDialog(this, "用户名长度至少为5个字符。", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (userService.getUserByUsername(username) != null) {
                    JOptionPane.showMessageDialog(this, "用户名已存在。", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, "密码与确认密码不一致。", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 调用checkPasswordFormat方法，检查用户输入的密码是否符合要求
                boolean valid = checkPasswordFormat(password);
                if (valid) { // 如果符合要求，继续执行注册逻辑
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setRole(Role.STUDENT);
                    // 默认角色为STUDENT，且调用UserService的addUser方法，将用户信息添加到数据库中，并返回结果
                    boolean result = userService.addUser(user);
                    if (result) {
                        JOptionPane.showMessageDialog(this, "注册成功。", "成功", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "注册失败。", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else { // 如果不符合要求，弹出一个错误提示框，告诉用户密码的格式不正确，并返回
                    JOptionPane.showMessageDialog(this, "密码无效：必须超过8个字符，且包含大写字母、小写字母、数字及标点符号。", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        forgetButton.addActionListener(e -> {
            // 调用forgetPassword方法，实现忘记密码的逻辑
            forgetPassword();
        });
    }
    // 添加一个方法，用于检查密码的格式是否符合要求
    private boolean checkPasswordFormat(String password) {
        String regex1 = ".{9,}"; // 匹配长度大于8个字符的字符串
        String regex2 = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-\\[\\]{};':\"\\|,.<>/?]).*$"; // 匹配包含至少一个大写字母、至少一个小写字母、至少一个数字和至少一个标点符号的字符串
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);
        boolean match1 = pattern1.matcher(password).matches();
        boolean match2 = pattern2.matcher(password).matches();
        // 返回两个匹配结果的逻辑与，即只有当两个匹配结果都为真时，才返回真
        return match1 && match2;
    }
    // 添加一个方法，用于实现忘记密码的功能
    public void forgetPassword() {
        String username = JOptionPane.showInputDialog(null, "请输入您的用户名：", "忘记密码", JOptionPane.QUESTION_MESSAGE);
        if (username != null && !username.isEmpty()) { // 如果用户输入了用户名，根据用户名，从userService中获取用户对象
            User user = userService.getUserByUsername(username);
            if (user != null) { // 如果用户对象不为空，说明用户名存在，弹出一个输入框，让用户输入注册所使用的邮箱地址
                String email = JOptionPane.showInputDialog(null, "请输入您的电子邮箱地址：", "忘记密码", JOptionPane.QUESTION_MESSAGE);
                if (email != null && !email.isEmpty()) { // 如果用户输入了邮箱地址
                    // 生成一个随机的密码，长度为8个字符，包含大小写字母、数字和标点符号
                    String newPassword = generateRandomPassword(8);
                    // 更新用户对象的密码属性，并调用userService的updateUser方法更新数据库中的用户信息
                    user.setPassword(newPassword);
                    userService.updateUser(user);
                    // JOptionPane.showMessageDialog(null, "用户 " + username + " 的新密码为 " + newPassword + " ，已发送至 " + email + " 。", "忘记密码", JOptionPane.INFORMATION_MESSAGE);
                    // System.out.println("用户 " + username + " 的新密码为 " + newPassword + " ，已发送至 " + email + " 。");
                    JOptionPane.showMessageDialog(null, "请登录您的邮箱查看新密码。", "忘记密码", JOptionPane.INFORMATION_MESSAGE);
                } else { // 如果用户没有输入邮箱地址
                    JOptionPane.showMessageDialog(null, "未输入电子邮箱地址。", "忘记密码", JOptionPane.INFORMATION_MESSAGE);
                }
            } else { // 如果用户对象为空，说明用户名不存在
                JOptionPane.showMessageDialog(null, "用户名不存在。", "忘记密码", JOptionPane.ERROR_MESSAGE);
            }
        } else { // 如果用户没有输入用户名
            JOptionPane.showMessageDialog(null, "未输入用户名。", "忘记密码", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // 添加一个辅助方法，用于生成随机的密码
    private String generateRandomPassword(int length) {
        // 定义一个字符串，包含大小写字母、数字和标点符号
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{};':\"\\|,.<>/?";
        char[] password = new char[length];
        Random random = new Random();
        // 遍历字符数组，每次从字符串中随机选择一个字符，赋值给数组中的元素
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password[i] = chars.charAt(index);
        }
        return new String(password);
    }
}