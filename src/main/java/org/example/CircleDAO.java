package org.example;
import java.util.List;
public interface CircleDAO {
    // 创建表的方法
    boolean createCircleTable(boolean dropExistingTable);
    boolean createCircleMemberTable(boolean dropExistingTable);
    boolean createCircleMessageTable(boolean dropExistingTable);
    // Circle 操作
    boolean createCircle(Circle circle);
    boolean updateCircle(Circle circle);
    boolean deleteCircle(int circleId);
    Circle getCircleById(int circleId);
    List<Circle> getAllCircles();
    // CircleMember 操作
    boolean addCircleMember(CircleMember member);
    boolean updateCircleMember(CircleMember member);
    boolean removeCircleMember(int memberId);
    List<CircleMember> getMembersByCircleId(int circleId);
    // CircleMessage 操作
    boolean addCircleMessage(CircleMessage message);
    boolean updateCircleMessage(CircleMessage message);
    boolean deleteCircleMessage(int messageId);
    List<CircleMessage> getMessagesByCircleId(int circleId);
}