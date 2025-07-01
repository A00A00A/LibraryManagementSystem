package org.example;
import java.util.List;
/**
 * CircleService 类负责封装圈子系统的业务逻辑，
 * 并调用 CircleDAO 接口完成数据的增删改查操作。
 * 该类中提供以下功能：
 * Circle 操作：创建、更新、删除、根据 ID 查询单个圈子，以及查询所有圈子；
 * CircleMember 操作：添加、更新、删除圈子成员，以及根据圈子 ID 查询成员列表；
 * CircleMessage 操作：添加、更新、删除圈子消息，以及根据圈子 ID 查询消息列表。
 */
public class CircleService {
    private CircleDAO circleDao;
    /**
     * 构造方法，根据传入的 CircleDAO 构造业务逻辑层对象
     * @param circleDao 数据访问接口对象
     */
    public CircleService(CircleDAO circleDao) {
        this.circleDao = circleDao;
    }
    // ==============================
    // Circle 相关业务方法
    // ==============================
    /**
     * 创建一个新的圈子
     * @param circle 圈子对象
     * @return 操作结果，true 表示成功
     */
    public boolean createCircle(Circle circle) {
        return circleDao.createCircle(circle);
    }
    /**
     * 更新圈子信息
     * @param circle 圈子对象
     * @return 操作结果，true 表示成功
     */
    public boolean updateCircle(Circle circle) {
        return circleDao.updateCircle(circle);
    }
    /**
     * 根据圈子 ID 删除圈子记录
     * @param circleId 圈子 ID
     * @return 操作结果，true 表示成功
     */
    public boolean deleteCircle(int circleId) {
        return circleDao.deleteCircle(circleId);
    }
    /**
     * 根据圈子 ID 查询单个圈子
     * @param circleId 圈子 ID
     * @return 查询到的 Circle 对象
     */
    public Circle getCircleById(int circleId) {
        return circleDao.getCircleById(circleId);
    }
    /**
     * 查询所有圈子
     * @return 包含所有圈子的列表
     */
    public List<Circle> getAllCircles() {
        return circleDao.getAllCircles();
    }
    // ==============================
    // CircleMember 相关业务方法
    // ==============================
    /**
     * 向圈子中添加成员
     * @param member 成员对象
     * @return 操作结果，true 表示成功
     */
    public boolean addCircleMember(CircleMember member) {
        return circleDao.addCircleMember(member);
    }
    /**
     * 更新圈子成员信息
     * @param member 成员对象
     * @return 操作结果，true 表示成功
     */
    public boolean updateCircleMember(CircleMember member) {
        return circleDao.updateCircleMember(member);
    }
    /**
     * 从圈子中移除指定成员
     * @param memberId 成员记录的ID
     * @return 操作结果，true 表示成功
     */
    public boolean removeCircleMember(int memberId) {
        return circleDao.removeCircleMember(memberId);
    }
    /**
     * 根据圈子 ID 查询该圈子内所有成员
     * @param circleId 圈子 ID
     * @return 包含所有成员的列表
     */
    public List<CircleMember> getMembersByCircleId(int circleId) {
        return circleDao.getMembersByCircleId(circleId);
    }
    // ==============================
    // CircleMessage 相关业务方法
    // ==============================
    /**
     * 发布一条圈子消息
     * @param message 消息对象
     * @return 操作结果，true 表示成功
     */
    public boolean addCircleMessage(CircleMessage message) {
        return circleDao.addCircleMessage(message);
    }
    /**
     * 更新圈子消息内容
     * @param message 消息对象
     * @return 操作结果，true 表示成功
     */
    public boolean updateCircleMessage(CircleMessage message) {
        return circleDao.updateCircleMessage(message);
    }
    /**
     * 删除指定消息
     * @param messageId 消息 ID
     * @return 操作结果，true 表示成功
     */
    public boolean deleteCircleMessage(int messageId) {
        return circleDao.deleteCircleMessage(messageId);
    }
    /**
     * 根据圈子 ID 查询所有消息
     * @param circleId 圈子 ID
     * @return 包含所有消息的列表
     */
    public List<CircleMessage> getMessagesByCircleId(int circleId) {
        return circleDao.getMessagesByCircleId(circleId);
    }
}