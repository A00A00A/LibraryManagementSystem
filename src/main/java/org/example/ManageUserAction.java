package org.example;
public class ManageUserAction extends AbstractAuthenticatedAction {
    private UserFrame userFrame = null;
    public ManageUserAction(UserService userService, GlobalState globalState) {
        this.userFrame = new UserFrame(userService, globalState);
    }
    @Override
    protected void perform() {
        Role currentRole = super.getCurrentRole();
        if (currentRole == Role.ADMINISTRATOR) {
            userFrame.setVisible(true);
        } else {
            super.println("You are not Administrator.");
        }
    }
    @Override
    public String getActionName() {
        return "用户管理";
    }
}