package org.example;
import org.example.AbstractAuthenticatedAction.Role;
public class User {
    private String username = null;
    private String password = null;
    private Role role = null;
    public User() {
    }
    public User(String string, String string2, Role administrator) {
        this.username = string;
        this.password = string2;
        this.role = administrator;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}