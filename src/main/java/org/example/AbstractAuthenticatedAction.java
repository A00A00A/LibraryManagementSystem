package org.example;
public abstract class AbstractAuthenticatedAction extends AbstractAction {
    public enum Role {
        ADMINISTRATOR, // 管理员：拥有绝对且全局的系统管理权限。
        TUTOR,         // 导师：作为知识及指导核心，不仅可以管理个人主页，还可以维护其导师圈，并将重要消息自动推送给所带学生。
        STUDENT        // 学生：具有自己的个人空间，能够浏览图书库、发布读书心得，也能参与导师管理的圈子活动。
    }
    protected static boolean isAuthenticated = false;
    protected static Role currentRole = null;
    protected void validate() throws AuthenticationException {
        if (!isAuthenticated) {
            throw new AuthenticationException("You must be authenticated to perform this action.");
        }
    }
    protected Role getCurrentRole() {
        return currentRole;
    }
    protected boolean getIsAuthenticated() {
        return isAuthenticated;
    }
    @Override
    public final void run() {
        try {
            validate();
            perform();
        } catch (AuthenticationException e) {
            println("Authentication failed: " + e.getMessage());
        }
    }
    protected abstract void perform();
}
class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
}