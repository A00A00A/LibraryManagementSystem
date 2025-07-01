package org.example;
public class ManageBookAction extends AbstractAuthenticatedAction {
    // BookFrame 用于展示和管理书籍信息的图形界面
    private BookFrame bookFrame = null;
    /**
     * 构造方法，通过传入 BookService 与 GlobalState 构造 BookFrame 对象
     * @param bookService 图书管理业务层接口，用于处理书籍信息的增删改查
     * @param globalState 全局状态对象，用于存储当前登录用户及系统状态
     */
    public ManageBookAction(BookService bookService, GlobalState globalState) {
        // 创建 BookFrame 对象，传入业务层接口
        this.bookFrame = new BookFrame(bookService, globalState);
    }
    /**
     * 重写 perform 方法
     */
    @Override
    protected void perform() {
        // 显示 BookFrame 界面
        bookFrame.setVisible(true);
    }
    /**
     * 返回操作名称，供菜单显示使用
     */
    @Override
    public String getActionName() {
        return "图书管理系统";
    }
}