package org.example;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
/**
 * PersonalSpaceFrame 类用于提供个人空间界面，
 * 仅允许当前用户修改密码，不显示或修改用户权限信息。
 * 主要包括：
 * ① 显示用户名（不可编辑）；
 * ② 提供新密码和确认密码的输入框（必须满足至少8位且包含大写、小写字母、数字和标点符号）；
 */
public class PersonalSpaceFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton saveButton, refreshButton;
    private JLabel messageLabel;
    private UserService userService;
    private GlobalState globalState;
    // 定时器，每100秒刷新一次数据
    // private Timer refreshTimer;
    public PersonalSpaceFrame(UserService userService, GlobalState globalState) {
        this.userService = userService;
        this.globalState = globalState;
        initComponents();
        initFrame();
        // initRefreshTimer();
    }
    /**
     * 初始化UI组件
     */
    private void initComponents() {
        usernameField = new JTextField(20);
        usernameField.setEditable(false);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        saveButton = new JButton("保存");
        refreshButton = new JButton("刷新");
        messageLabel = new JLabel("个人空间", SwingConstants.CENTER);
        // 注册保存与刷新按钮的事件监听器
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUserInfo();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUserData();
                messageLabel.setText("数据已刷新");
            }
        });
    }
    /**
     * 初始化窗口布局
     */
    private void initFrame() {
        this.setTitle("个人空间");
        this.setSize(500, 250);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.add(new JLabel("用户名："));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("新密码："));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("确认密码："));
        formPanel.add(confirmPasswordField);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(refreshButton);
        this.setLayout(new BorderLayout(10, 10));
        this.add(messageLabel, BorderLayout.NORTH);
        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }
    /**
     * 初始化定时刷新，每100秒刷新一次用户数据，此处废弃
    
    private void initRefreshTimer() {
        refreshTimer = new Timer(100000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUserData();
            }
        });
        refreshTimer.start();
    }
     */
    /**
     * 从业务层加载当前用户信息，并刷新界面显示
     */
    private void loadUserData() {
        String username = globalState.getUsername();
        User user = userService.getUserByUsername(username);
        if (user != null) {
            usernameField.setText(user.getUsername());
            // 清空密码框，要求用户重新输入（如果不修改则留空）
            passwordField.setText("");
            confirmPasswordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "获取用户信息失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * 验证密码是否符合要求：至少8个字符，包含大写字母、小写字母、数字和标点符号
     */
    private boolean isValidPassword(String pwd) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        return Pattern.compile(regex).matcher(pwd).matches();
    }
    /**
     * 保存用户信息，仅允许修改密码
     */
    private void saveUserInfo() {
        String username = usernameField.getText();
        String newPwd = new String(passwordField.getPassword()).trim();
        String confirmPwd = new String(confirmPasswordField.getPassword()).trim();
        User user = userService.getUserByUsername(username);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "获取用户信息失败", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // 若密码输入框为空，则不进行密码修改
        if (newPwd.isEmpty() && confirmPwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入新密码或保持为空表示不修改", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidPassword(newPwd)) {
            JOptionPane.showMessageDialog(this, "新密码不符合要求：必须至少8个字符，包含大写字母、小写字母、数字及标点符号", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // 更新密码
        user.setPassword(newPwd);
        boolean result = userService.updateUser(user);
        if (result) {
            JOptionPane.showMessageDialog(this, "密码修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            loadUserData();
        } else {
            JOptionPane.showMessageDialog(this, "密码修改失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    // 重写 setVisible 方法，在每次窗口显示前调用 loadUserData() 方法
    @Override
    public void setVisible(boolean b) {
        if (b) {
            loadUserData();
        }
        super.setVisible(b);
    }
}