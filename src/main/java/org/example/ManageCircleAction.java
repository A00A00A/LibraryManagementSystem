package org.example;
/**
 * ManageCircleAction 类用于调用 CircleFrame 界面，实现圈子系统管理操作。
 */
public class ManageCircleAction extends AbstractAuthenticatedAction {
    private CircleFrame circleFrame;
    /**
     * 构造方法，传入 CircleService 和 GlobalState 对象，生成 CircleFrame 界面
     */
    public ManageCircleAction(CircleService circleService, GlobalState globalState) {
        this.circleFrame = new CircleFrame(circleService, globalState);
    }
    @Override
    protected void perform() {
        circleFrame.setVisible(true);
    }
    @Override
    public String getActionName() {
        return "圈子管理";
    }
}