package org.example;
/**
 * ManagePersonalSpaceAction 类用于展示和管理个人空间界面，其在构造方法中接收 UserService 与 GlobalState 对象，然后创建 PersonalSpaceFrame 界面，并在 perform() 方法中显示之。
 */
public class ManagePersonalSpaceAction extends AbstractAuthenticatedAction {
    // PersonalSpaceFrame 用于展示和管理用户的个人信息
    private PersonalSpaceFrame personalSpaceFrame = null;
    /**
     * 构造方法，通过传入 UserService 与 GlobalState 构造 PersonalSpaceFrame 对象
     * @param userService 用户业务接口，用于处理用户信息的操作
     * @param globalState 全局状态对象，保存当前登录用户及系统状态信息
     */
    public ManagePersonalSpaceAction(UserService userService, GlobalState globalState) {
        // 创建 PersonalSpaceFrame 界面，传入业务接口和全局状态
        this.personalSpaceFrame = new PersonalSpaceFrame(userService, globalState);
    }
    /**
     * 重写perform方法，显示个人空间界面
     */
    @Override
    protected void perform() {
        // 显示个人空间界面
        personalSpaceFrame.setVisible(true);
    }
    /**
     * 返回操作名称，用于菜单中显示
     */
    @Override
    public String getActionName() {
        return "个人空间";
    }
}