package org.example;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
/**
 * CircleFrame 类用于展示圈子系统的图形界面，
 * 包括显示圈子列表、添加/更新/删除圈子、刷新列表，以及管理圈子成员、查看圈子消息和发布圈子消息等功能。
 * 同时根据 GlobalState 动态调整按钮的可见性和可操作性，以区分管理员、导师、学生等不同角色的访问权限。
 */
public class CircleFrame extends JFrame {
    private JTable circleTable;
    private JButton addButton, updateButton, deleteButton, refreshButton;
    private JButton manageMembersButton, viewMessagesButton, postMessageButton;
    private JPanel buttonPanel;
    private JLabel messageLabel;
    private CircleService circleService; // 业务层接口
    private GlobalState globalState; // 全局状态对象
    /**
     * 构造方法，传入业务层接口和全局状态对象
     */
    public CircleFrame(CircleService circleService, GlobalState globalState) {
        this.circleService = circleService;
        this.globalState = globalState;
        initializeComponents();
        initializeFrame();
    }
    /**
     * 初始化所有界面组件，包括表格、按钮、提示信息等
     */
    private void initializeComponents() {
        // 初始化圈子列表表格
        circleTable = new JTable();
        // 初始化功能按钮
        addButton = new JButton("添加圈子");
        updateButton = new JButton("修改圈子");
        deleteButton = new JButton("删除圈子");
        refreshButton = new JButton("刷新列表");
        manageMembersButton = new JButton("管理圈子成员");
        viewMessagesButton = new JButton("查看圈子消息");
        postMessageButton = new JButton("发布圈子消息");
        // 将按钮统一添加到按钮面板中
        buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(manageMembersButton);
        buttonPanel.add(viewMessagesButton);
        buttonPanel.add(postMessageButton);
        // 顶部提示信息标签
        messageLabel = new JLabel("欢迎使用圈子管理系统", SwingConstants.CENTER);
        // 注册各按钮事件监听器
        addButton.addActionListener(e -> addCircle());
        updateButton.addActionListener(e -> updateCircle());
        deleteButton.addActionListener(e -> deleteCircle());
        refreshButton.addActionListener(e -> loadTableData());
        manageMembersButton.addActionListener(e -> manageMembers());
        viewMessagesButton.addActionListener(e -> viewMessages());
        postMessageButton.addActionListener(e -> postMessage());
    }
    /**
     * 初始化窗口属性及布局
     */
    private void initializeFrame() {
        this.setTitle("圈子管理系统");
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(circleTable), BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(messageLabel, BorderLayout.NORTH);
        loadTableData();
    }
    /**
     * 加载圈子数据并填充到表格中
     */
    private void loadTableData() {
        List<Circle> circles = circleService.getAllCircles();
        String[] columnNames = { "编号", "圈子名称", "描述", "所有者", "创建时间" };
        Object[][] data = new Object[circles.size()][5];
        for (int i = 0; i < circles.size(); i++) {
            Circle c = circles.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getName();
            data[i][2] = c.getDescription();
            data[i][3] = c.getOwnerUserId();
            data[i][4] = c.getCreationTime().toString();
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        circleTable.setModel(model);
    }
    /**
     * 根据全局状态更新按钮显示状态，区别不同角色的权限
     */
    private void updateRoleBasedButtons() {
        if (!globalState.isAuthenticated()) {
            addButton.setVisible(false);
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
            refreshButton.setVisible(true);
            manageMembersButton.setVisible(false);
            viewMessagesButton.setVisible(true);
            postMessageButton.setVisible(false);
            messageLabel.setText("请先登录");
        } else {
            String role = globalState.getRole().toString();
            if (role.equalsIgnoreCase("ADMINISTRATOR")) {
                // 管理员显示全部功能
                addButton.setVisible(true);
                updateButton.setVisible(true);
                deleteButton.setVisible(true);
                refreshButton.setVisible(true);
                manageMembersButton.setVisible(true);
                viewMessagesButton.setVisible(true);
                postMessageButton.setVisible(true);
                messageLabel.setText("管理员模式");
            } else if (role.equalsIgnoreCase("TUTOR")) {
                // 导师只能管理成员、查看消息和发布消息，但不添加、修改、删除圈子
                addButton.setVisible(false);
                updateButton.setVisible(false);
                deleteButton.setVisible(false);
                refreshButton.setVisible(true);
                manageMembersButton.setVisible(true);
                viewMessagesButton.setVisible(true);
                postMessageButton.setVisible(true);
                messageLabel.setText("导师模式");
            } else if (role.equalsIgnoreCase("STUDENT")) {
                // 学生仅允许浏览圈子和查看消息和讨论消息
                addButton.setVisible(false);
                updateButton.setVisible(false);
                deleteButton.setVisible(false);
                refreshButton.setVisible(true);
                manageMembersButton.setVisible(false);
                viewMessagesButton.setVisible(true);
                postMessageButton.setVisible(true);
                messageLabel.setText("学生模式");
            } else {
                addButton.setVisible(false);
                updateButton.setVisible(false);
                deleteButton.setVisible(false);
                refreshButton.setVisible(true);
                manageMembersButton.setVisible(false);
                viewMessagesButton.setVisible(true);
                postMessageButton.setVisible(false);
                messageLabel.setText("角色未知");
            }
        }
    }
    @Override
    public void setVisible(boolean b) {
        if (b) {
            updateRoleBasedButtons();
        }
        super.setVisible(b);
    }
    // --------------------
    // 圈子操作方法
    // --------------------
    private void addCircle() {
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        // 默认所有者为当前登录用户
        JTextField ownerField = new JTextField(globalState.getUsername());
        Object[] fields = { "圈子名称：", nameField, "描述：", descField, "所有者：", ownerField };
        int option = JOptionPane.showConfirmDialog(this, fields, "添加圈子", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Circle circle = new Circle();
            circle.setName(nameField.getText().trim());
            circle.setDescription(descField.getText().trim());
            circle.setOwnerUserId(ownerField.getText().trim());
            circle.setCreationTime(new Date());
            boolean result = circleService.createCircle(circle);
            if (result) {
                messageLabel.setText("圈子添加成功");
                loadTableData();
            } else {
                messageLabel.setText("圈子添加失败");
            }
        }
    }
    private void updateCircle() {
        int row = circleTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择要修改的圈子", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int circleId = (int) circleTable.getValueAt(row, 0);
        Circle circle = circleService.getCircleById(circleId);
        if (circle == null) {
            JOptionPane.showMessageDialog(this, "未找到选中的圈子", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JTextField nameField = new JTextField(circle.getName());
        JTextField descField = new JTextField(circle.getDescription());
        Object[] fields = { "圈子名称：", nameField, "描述：", descField };
        int option = JOptionPane.showConfirmDialog(this, fields, "修改圈子", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            circle.setName(nameField.getText().trim());
            circle.setDescription(descField.getText().trim());
            boolean result = circleService.updateCircle(circle);
            if (result) {
                messageLabel.setText("圈子更新成功");
                loadTableData();
            } else {
                messageLabel.setText("圈子更新失败");
            }
        }
    }
    private void deleteCircle() {
        int row = circleTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的圈子", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int circleId = (int) circleTable.getValueAt(row, 0);
        int option = JOptionPane.showConfirmDialog(this, "确认删除选中的圈子吗？", "删除确认", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            boolean result = circleService.deleteCircle(circleId);
            if (result) {
                messageLabel.setText("圈子删除成功");
                loadTableData();
            } else {
                messageLabel.setText("圈子删除失败");
            }
        }
    }
    // --------------------
    // 管理圈子成员操作：打开内部对话框
    // --------------------
    private void manageMembers() {
        int row = circleTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择要管理成员的圈子", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // 获取选中行对应的圈子ID
        int circleId = (int) circleTable.getValueAt(row, 0);
        // 根据圈子ID获取圈子对象
        Circle circle = circleService.getCircleById(circleId);
        if (circle == null) {
            JOptionPane.showMessageDialog(this, "未找到选中的圈子", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // 如果当前用户角色为 TUTOR，则判断是否为该圈子所有者
        if (globalState.getRole().toString().equalsIgnoreCase("TUTOR")) {
            if (!circle.getOwnerUserId().equalsIgnoreCase(globalState.getUsername())) {
                JOptionPane.showMessageDialog(this, "您没有权限管理非本人拥有的圈子成员", "权限不足", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        // 权限检查通过后，打开管理圈子成员对话框
        ManageMembersDialog dialog = new ManageMembersDialog(this, circleService, circleId, globalState);
        dialog.setVisible(true);
    }
    // --------------------
    // 查看圈子消息操作
    // --------------------
    private void viewMessages() {
        int row = circleTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择一个圈子以查看消息", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int circleId = (int) circleTable.getValueAt(row, 0);
        List<CircleMessage> messages = circleService.getMessagesByCircleId(circleId);
        if (messages == null || messages.isEmpty()) {
            JOptionPane.showMessageDialog(this, "该圈子暂无消息", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (CircleMessage msg : messages) {
            sb.append("发送者：").append(msg.getSenderUserId()).append("\n");
            sb.append("内容：").append(msg.getContent()).append("\n");
            sb.append("类型：").append(msg.getMessageType()).append("\n");
            sb.append("时间：").append(msg.getTimestamp()).append("\n");
            sb.append("----------------------------------------------------\n");
        }
        JTextArea ta = new JTextArea(sb.toString(), 15, 30);
        ta.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "圈子消息", JOptionPane.INFORMATION_MESSAGE);
    }
    // --------------------
    // 发布圈子消息操作
    // --------------------
    private void postMessage() {
        int row = circleTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择一个圈子以发布消息", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int circleId = (int) circleTable.getValueAt(row, 0);
        JTextField typeField = new JTextField();
        JTextField contentField = new JTextField();
        Object[] fields = { "消息类型：", typeField, "消息内容：", contentField };
        int option = JOptionPane.showConfirmDialog(this, fields, "发布圈子消息", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            CircleMessage message = new CircleMessage();
            message.setCircleId(circleId);
            message.setSenderUserId(globalState.getUsername());
            message.setMessageType(typeField.getText().trim());
            message.setContent(contentField.getText().trim());
            message.setTimestamp(new Date());
            boolean result = circleService.addCircleMessage(message);
            if (result) {
                messageLabel.setText("消息发布成功");
            } else {
                messageLabel.setText("消息发布失败");
            }
        }
    }
    // 内部类：管理圈子成员对话框
    private class ManageMembersDialog extends JDialog {
        private CircleService circleService;
        private int circleId;
        private GlobalState globalState;
        private JTable memberTable;
        private JButton addMemberButton, updateMemberButton, removeMemberButton, refreshMemberButton;
        private JLabel dialogMessageLabel;
        public ManageMembersDialog(Frame owner, CircleService circleService, int circleId, GlobalState globalState) {
            super(owner, "管理圈子成员", true);
            this.circleService = circleService;
            this.circleId = circleId;
            this.globalState = globalState;
            initializeComponents();
            initializeDialog();
        }
        private void initializeComponents() {
            memberTable = new JTable();
            addMemberButton = new JButton("添加成员");
            updateMemberButton = new JButton("修改成员");
            removeMemberButton = new JButton("删除成员");
            refreshMemberButton = new JButton("刷新列表");
            dialogMessageLabel = new JLabel("管理圈子成员", SwingConstants.CENTER);
            addMemberButton.addActionListener(e -> addMember());
            updateMemberButton.addActionListener(e -> updateMember());
            removeMemberButton.addActionListener(e -> removeMember());
            refreshMemberButton.addActionListener(e -> loadMemberTableData());
        }
        private void initializeDialog() {
            this.setLayout(new BorderLayout());
            this.add(new JScrollPane(memberTable), BorderLayout.CENTER);
            JPanel btnPanel = new JPanel();
            btnPanel.add(addMemberButton);
            btnPanel.add(updateMemberButton);
            btnPanel.add(removeMemberButton);
            btnPanel.add(refreshMemberButton);
            this.add(btnPanel, BorderLayout.SOUTH);
            this.add(dialogMessageLabel, BorderLayout.NORTH);
            this.setSize(600, 400);
            this.setLocationRelativeTo(getOwner());
            loadMemberTableData();
            updateMemberButtonsByRole();
        }
        private void updateMemberButtonsByRole() {
            String role = globalState.getRole().toString();
            if (role.equalsIgnoreCase("ADMINISTRATOR") || role.equalsIgnoreCase("TUTOR")) {
                addMemberButton.setEnabled(true);
                updateMemberButton.setEnabled(true);
                removeMemberButton.setEnabled(true);
            } else if (role.equalsIgnoreCase("STUDENT")) {
                addMemberButton.setEnabled(false);
                updateMemberButton.setEnabled(false);
                removeMemberButton.setEnabled(false);
            } else {
                addMemberButton.setEnabled(false);
                updateMemberButton.setEnabled(false);
                removeMemberButton.setEnabled(false);
            }
        }
        private void loadMemberTableData() {
            List<CircleMember> members = circleService.getMembersByCircleId(circleId);
            String[] columnNames = { "ID", "用户ID", "加入时间", "角色" };
            Object[][] data = new Object[members.size()][4];
            for (int i = 0; i < members.size(); i++) {
                CircleMember m = members.get(i);
                data[i][0] = m.getId();
                data[i][1] = m.getUserId();
                data[i][2] = m.getJoinTime().toString();
                data[i][3] = m.getRole();
            }
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            memberTable.setModel(model);
        }
        private void addMember() {
            JTextField userIdField = new JTextField();
            JTextField roleField = new JTextField();
            Object[] fields = { "用户ID:", userIdField, "角色:", roleField };
            int option = JOptionPane.showConfirmDialog(this, fields, "添加成员", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                CircleMember member = new CircleMember();
                member.setCircleId(circleId);
                member.setUserId(userIdField.getText().trim());
                member.setRole(roleField.getText().trim());
                member.setJoinTime(new Date());
                boolean result = circleService.addCircleMember(member);
                if (result) {
                    loadMemberTableData();
                    dialogMessageLabel.setText("添加成员成功");
                } else {
                    dialogMessageLabel.setText("添加成员失败");
                }
            }
        }
        private void updateMember() {
            int row = memberTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请选择要修改的成员", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int memberId = (int) memberTable.getValueAt(row, 0);
            List<CircleMember> members = circleService.getMembersByCircleId(circleId);
            CircleMember member = null;
            for (CircleMember m : members) {
                if (m.getId() == memberId) {
                    member = m;
                    break;
                }
            }
            if (member == null) {
                JOptionPane.showMessageDialog(this, "未找到选中的成员", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JTextField roleField = new JTextField(member.getRole());
            Object[] fields = { "新角色:", roleField };
            int option = JOptionPane.showConfirmDialog(this, fields, "修改成员", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                member.setRole(roleField.getText().trim());
                boolean result = circleService.updateCircleMember(member);
                if (result) {
                    loadMemberTableData();
                    dialogMessageLabel.setText("成员更新成功");
                } else {
                    dialogMessageLabel.setText("成员更新失败");
                }
            }
        }
        private void removeMember() {
            int row = memberTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请选择要删除的成员", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int memberId = (int) memberTable.getValueAt(row, 0);
            int option = JOptionPane.showConfirmDialog(this, "确认删除该成员吗？", "删除确认", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                boolean result = circleService.removeCircleMember(memberId);
                if (result) {
                    loadMemberTableData();
                    dialogMessageLabel.setText("成员删除成功");
                } else {
                    dialogMessageLabel.setText("成员删除失败");
                }
            }
        }
    }
}