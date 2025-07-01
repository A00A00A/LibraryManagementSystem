package org.example;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.example.AbstractAuthenticatedAction.Role;
class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private boolean isClosed;
    private static final User ADMIN_USER = new User("admin", "ynu#rjgc", Role.ADMINISTRATOR);
    public LoginFrame(UserService userService, GlobalState globalState) {
        this.setTitle("登录");
        this.setSize(300, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10)); // 设置面板的布局为网格布局，4行2列，间隔为10
        JLabel usernameLabel = new JLabel("用户名：");
        usernameField = new JTextField();
        panel.add(usernameLabel);
        panel.add(usernameField);
        JLabel passwordLabel = new JLabel("密码：");
        passwordField = new JPasswordField();
        panel.add(passwordLabel);
        panel.add(passwordField);
        JLabel emptyLabel = new JLabel("");
        loginButton = new JButton("登录");
        panel.add(emptyLabel);
        panel.add(loginButton);
        JLabel emptyLabel2 = new JLabel("");
        registerButton = new JButton("注册");
        panel.add(emptyLabel2);
        panel.add(registerButton);
        this.add(panel);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            try {
                if (username.equals(ADMIN_USER.getUsername()) && password.equals(ADMIN_USER.getPassword())) {
                    globalState.setUsername(ADMIN_USER.getUsername());
                    globalState.setRole(ADMIN_USER.getRole());
                    globalState.setIsAuthenticated(true);
                    this.dispose();
                } else {
                    // 验证用户名和密码是否正确，并返回结果
                    User user = userService.login(username, password);
                    if (user != null) {
                        // 如果登录成功，则将当前用户信息存储到GlobalState中，并关闭登录窗口
                        globalState.setUsername(user.getUsername());
                        globalState.setRole(user.getRole());
                        globalState.setIsAuthenticated(true);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "无效的用户名或密码。", "错误", JOptionPane.ERROR_MESSAGE);
                        usernameField.setText("");
                        passwordField.setText("");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        registerButton.addActionListener(e -> {
            RegisterFrame registerFrame = new RegisterFrame(userService);
            registerFrame.setVisible(true);
        });
        // 为窗口添加一个窗口监听器，当窗口关闭时，将isClosed设为true
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                isClosed = true;
            }
        });
    }
    public boolean isClosed() {
        return isClosed;
    }
}