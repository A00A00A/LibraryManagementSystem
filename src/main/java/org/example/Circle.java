package org.example;
import java.util.Date;
/**
 * Circle 类用于表示一个圈子（书友圈）的基本信息。
 */
public class Circle {
    private int id; // 圈子ID
    private String name; // 圈子名称
    private String description; // 圈子描述
    private String ownerUserId; // 圈子的所有者/导师的用户ID
    private Date creationTime; // 创建时间
    // 默认构造函数
    public Circle() {
    }
    // 参数构造函数
    public Circle(int id, String name, String description, String ownerUserId, Date creationTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerUserId = ownerUserId;
        this.creationTime = creationTime;
    }
    // Getters 与 Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getOwnerUserId() {
        return ownerUserId;
    }
    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }
    public Date getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
    @Override
    public String toString() {
        return "Circle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ownerUserId='" + ownerUserId + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}