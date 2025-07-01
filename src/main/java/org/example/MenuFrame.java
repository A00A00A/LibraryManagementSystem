package org.example;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
class MenuFrame extends JFrame {
    private JList<String> actionList;
    public MenuFrame(List<AbstractAction> actions, GlobalState globalState) {
        this.setTitle("读书会书籍共享平台");
        this.setSize(700, 700);
        this.setLocationRelativeTo(null);
        // 修改关闭操作，改为不自动关闭
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // 添加窗口监听器，拦截右上角退出操作，提示确认
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showOptionDialog(MenuFrame.this,
                        "确定要退出程序吗？", "退出确认",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"取消退出", "退出程序"},
                        "取消退出");
                if(option == 1){ // 1对应“退出程序”
                    MenuFrame.this.dispose();
                    System.exit(0);
                }
            }
        });
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (AbstractAction action : actions) {
            listModel.addElement(action.getActionName());
        }
        actionList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(actionList);
        this.add(scrollPane, BorderLayout.CENTER);
        actionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 如果鼠标双击了列表项，则获取该项的索引
                if (e.getClickCount() == 2) {
                    int index = actionList.locationToIndex(e.getPoint());
                    if (index >= 0 && index < actions.size()) {
                        // 如果索引有效，则获取对应的action对象，并调用其run方法
                        AbstractAction action = actions.get(index);
                        action.run();
                    }
                }
            }
        });
    }
}