package org.example;
import java.util.ArrayList;
import java.util.List;

import org.example.sqlite.SQLiteBookDAO;
import org.example.sqlite.SQLiteCircleDAO;
import org.example.sqlite.SQLiteUserDAO;
public class Main {
    private List<AbstractAction> actions = null;
    private GlobalState globalState = null;
    private UserService userService = null;
    private BookService bookService = null;
    private CircleService circleService = null;
    protected void initialize() {
        // System.out.println("Initializing...");
        this.globalState = new GlobalState();
        this.actions = new ArrayList<>();
        this.userService = new UserService(new SQLiteUserDAO());
        this.bookService = new BookService(new SQLiteBookDAO());
        this.circleService = new CircleService(new SQLiteCircleDAO());
        // 在登录前不能确定当前权限，因此不在此处添加 ManageUserAction
        this.actions.add(new ManagePersonalSpaceAction(this.userService, this.globalState));
        this.actions.add(new ManageBookAction(this.bookService, this.globalState));
        this.actions.add(new ManageCircleAction(this.circleService, this.globalState));
        // System.out.println("Initialization complete.");
    }
    protected void run() {
        // System.out.println("Running...");
        LoginFrame loginFrame = new LoginFrame(this.userService, this.globalState);
        loginFrame.setVisible(true);
        while (!loginFrame.isClosed()) {
            try {
                // 如果登录窗口没有关闭，则让当前线程休眠100毫秒
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 判断是否登录成功
        if (this.globalState.getUsername() != null) {
            // System.out.println("Welcome to the application!");
            // 登录后可确定权限，此时重新构造 actions 列表
            this.actions = new ArrayList<>();
            this.actions.add(new ManagePersonalSpaceAction(this.userService, this.globalState));
            // 仅当当前用户为管理员时添加 ManageUserAction
            if (this.globalState.getRole() == AbstractAuthenticatedAction.Role.ADMINISTRATOR) {
                this.actions.add(new ManageUserAction(this.userService, this.globalState));
            }
            this.actions.add(new ManageBookAction(this.bookService, this.globalState));
            this.actions.add(new ManageCircleAction(this.circleService, this.globalState));
            MenuFrame menuFrame = new MenuFrame(this.actions, this.globalState);
            menuFrame.setVisible(true);
            while (this.globalState.isRunning()) {
                try {
                    // 如果菜单窗口可见，则让当前线程休眠100毫秒
                    if (menuFrame.isVisible()) {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // System.out.println("Login failed. Exit the program.");
            System.exit(0);
        }
        // System.out.println("Running complete.");
    }
    public static void main(String[] args) {
        Main app = new Main();
        app.initialize();
        try {
            app.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // System.out.println("Resources freed.");
        }
    }
}