package org.example;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import org.example.AbstractAuthenticatedAction.Role;
/**
 * UserFrame 类用于展示和管理用户信息，供管理员使用。
 * 功能包括：
 * 1. 显示用户列表（表格显示用户名、密码、权限）。
 * 2. 删除用户、修改密码、修改权限、返回前一界面。
 * 3. 实时刷新：每隔一定时间自动刷新用户数据。
 * 4. 修改权限时，当当前管理员修改自己权限后立即退出该界面，防止权限问题。
 */
public class UserFrame extends JFrame {
    private JTable userTable;
    private JButton deleteButton, changePasswordButton, changeRoleButton, backButton;
    private JPanel buttonPanel;
    private JLabel messageLabel;
    private UserService userService;
    private GlobalState globalState;
    // 定时刷新定时器，每10秒刷新一次用户列表数据
    private Timer refreshTimer;
    public UserFrame(UserService userService, GlobalState globalState) {
        this.userService = userService;
        this.globalState = globalState;
        initComponents();
        initFrame();
        initRefreshTimer();
    }
    /**
     * 初始化所有UI组件
     */
    private void initComponents() {
        userTable = new JTable();
        deleteButton = new JButton("删除用户");
        changePasswordButton = new JButton("修改密码");
        changeRoleButton = new JButton("修改权限");
        backButton = new JButton("返回");
        buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(changeRoleButton);
        buttonPanel.add(backButton);
        messageLabel = new JLabel("用户列表", SwingConstants.CENTER);
        setButtonListeners();
    }
    /**
     * 初始化窗口及布局
     */
    private void initFrame() {
        this.setTitle("用户管理系统");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(userTable), BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(messageLabel, BorderLayout.NORTH);
        loadTableData();
    }
    /**
     * 初始化定时刷新定时器，每10秒刷新一次表格数据
     */
    private void initRefreshTimer() {
        refreshTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTableData();
            }
        });
        refreshTimer.start();
    }
    /**
     * 从业务层获取所有用户数据，并填充到表格中
     */
    private void loadTableData() {
        List<User> users = userService.getAllUsers();
        String[] columnNames = {"用户名", "密码", "权限"};
        Object[][] data = new Object[users.size()][3];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getUsername();
            data[i][1] = user.getPassword();
            data[i][2] = user.getRole().toString();
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable.setModel(model);
    }
    /**
     * 设置所有按钮的事件监听器
     */
    private void setButtonListeners() {
        // 删除用户按钮事件
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = userTable.getSelectedRow();
                if(row < 0){
                    messageLabel.setText("请选择要删除的用户！");
                    return;
                }
                String username = (String) userTable.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(UserFrame.this,
                        "确认删除用户 " + username + " 吗？", "删除确认", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION){
                    boolean result = userService.deleteUser(username);
                    if(result){
                        refreshTableData();
                        JOptionPane.showMessageDialog(UserFrame.this, "用户 " + username + " 删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        messageLabel.setText("用户 " + username + " 删除失败！");
                    }
                }
            }
        });
        // 修改密码按钮事件
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = userTable.getSelectedRow();
                if(row < 0){
                    messageLabel.setText("请选择要修改密码的用户！");
                    return;
                }
                String username = (String) userTable.getValueAt(row, 0);
                String newPassword = JOptionPane.showInputDialog(UserFrame.this, "请输入新密码：");
                if(newPassword != null && !newPassword.trim().isEmpty()){
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(newPassword);
                    // 取得当前行的权限（此处表格第三列存储的是权限）
                    user.setRole(AbstractAuthenticatedAction.Role.valueOf(userTable.getValueAt(row, 2).toString()));
                    boolean result = userService.updateUser(user);
                    if(result){
                        refreshTableData();
                        JOptionPane.showMessageDialog(UserFrame.this, "用户 " + username + " 密码修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        messageLabel.setText("用户 " + username + " 密码修改失败！");
                    }
                } else {
                    messageLabel.setText("请输入有效的新密码！");
                }
            }
        });
        // 修改权限按钮事件
        changeRoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = userTable.getSelectedRow();
                if(row < 0){
                    messageLabel.setText("请选择要修改权限的用户！");
                    return;
                }
                Map<String, Role> roles = new HashMap<>();
                roles.put("1", Role.ADMINISTRATOR);
                roles.put("2", Role.TUTOR);
                roles.put("3", Role.STUDENT);
                StringBuilder sb = new StringBuilder("请输入以下权限编号：\n");
                for (Map.Entry<String, Role> entry : roles.entrySet()) {
                    sb.append(entry.getKey()).append(" - ").append(entry.getValue().toString()).append("\n");
                }
                String username = (String) userTable.getValueAt(row, 0);
                String input = JOptionPane.showInputDialog(UserFrame.this, sb.toString() + "\n请输入新的权限编号：");
                if(roles.containsKey(input)){
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword((String) userTable.getValueAt(row, 1));
                    user.setRole(roles.get(input));
                    boolean result = userService.updateUser(user);
                    if(result){
                        refreshTableData();
                        JOptionPane.showMessageDialog(UserFrame.this, "用户 " + username + " 权限修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                        // 如果当前管理员修改的是自己的权限，则退出窗口
                        if(username.equalsIgnoreCase(globalState.getUsername())){
                            globalState.setRole(roles.get(input));
                            JOptionPane.showMessageDialog(UserFrame.this, "您已修改了自己的权限，窗口将自动关闭！", "提示", JOptionPane.INFORMATION_MESSAGE);
                            UserFrame.this.dispose();
                        }
                    } else {
                        messageLabel.setText("用户 " + username + " 权限修改失败！");
                    }
                } else {
                    messageLabel.setText("请输入有效的权限编号！");
                }
            }
        });
        // 返回按钮事件
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserFrame.this.dispose();
            }
        });
    }
    /**
     * 刷新表格数据（调用 loadTableData 方法）
     */
    private void refreshTableData() {
        loadTableData();
    }
}