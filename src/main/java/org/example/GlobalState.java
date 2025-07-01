package org.example;
import org.example.AbstractAuthenticatedAction.Role;
public class GlobalState {
    private boolean isRunning = true;
    private String username = null;
    public boolean isRunning() {
        return isRunning;
    }
    public String getUsername() {
        return username;
    }
    public Role getRole() {
        return AbstractAuthenticatedAction.currentRole;
    }
    public boolean isAuthenticated() {
        return AbstractAuthenticatedAction.isAuthenticated;
    }
    public void setRunning(boolean running) {
        isRunning = running;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setRole(Role role) {
        AbstractAuthenticatedAction.currentRole = role;
    }
    public void setIsAuthenticated(boolean b) {
        AbstractAuthenticatedAction.isAuthenticated = b;
    }
}