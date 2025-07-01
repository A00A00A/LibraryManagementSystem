package org.example;
import java.util.Date;
/**
 * CircleMember 类用于表示用户与圈子之间的成员关系。
 */
public class CircleMember {
    private int id;             // 成员记录ID
    private int circleId;       // 所属圈子ID
    private String userId;      // 用户ID
    private Date joinTime;      // 加入时间
    private String role;        // 圈子内角色，例如 "普通成员"、"圈主"
    // 默认构造函数
    public CircleMember() {
    }
    // 参数构造函数
    public CircleMember(int id, int circleId, String userId, Date joinTime, String role) {
        this.id = id;
        this.circleId = circleId;
        this.userId = userId;
        this.joinTime = joinTime;
        this.role = role;
    }
    // Getters 与 Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCircleId() {
        return circleId;
    }
    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Date getJoinTime() {
        return joinTime;
    }
    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return "CircleMember{" +
                "id=" + id +
                ", circleId=" + circleId +
                ", userId='" + userId + '\'' +
                ", joinTime=" + joinTime +
                ", role='" + role + '\'' +
                '}';
    }
}