package org.example;
import java.util.Date;
/**
 * CircleMessage 类用于表示圈子内的消息记录，
 * 包括公告、讨论消息等。
 */
public class CircleMessage {
    private int id;             // 消息ID
    private int circleId;       // 所属圈子ID
    private String senderUserId;// 发送者用户ID
    private String content;     // 消息内容
    private String messageType; // 消息类型，例如 "公告"、"讨论"
    private Date timestamp;     // 消息发送时间
    // 默认构造函数
    public CircleMessage() {
    }
    // 参数构造函数
    public CircleMessage(int id, int circleId, String senderUserId, String content, String messageType, Date timestamp) {
        this.id = id;
        this.circleId = circleId;
        this.senderUserId = senderUserId;
        this.content = content;
        this.messageType = messageType;
        this.timestamp = timestamp;
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
    public String getSenderUserId() {
        return senderUserId;
    }
    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getMessageType() {
        return messageType;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public String toString() {
        return "CircleMessage{" +
                "id=" + id +
                ", circleId=" + circleId +
                ", senderUserId='" + senderUserId + '\'' +
                ", content='" + content + '\'' +
                ", messageType='" + messageType + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}