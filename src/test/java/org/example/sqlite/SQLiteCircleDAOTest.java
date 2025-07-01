package org.example.sqlite;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.example.Circle;
import org.example.CircleDAO;
import org.example.CircleMember;
import org.example.CircleMessage;
import org.example.DatabaseConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class SQLiteCircleDAOTest {
    private CircleDAO circleDao; // 用于执行圈子系统操作的 DAO 对象
    private DatabaseConfig config = new SQLiteDatabaseConfig(); // 数据库配置对象
    @Before
    public void setUp() {
        // 初始化 DAO 对象，使用配置中数据库文件的路径
        circleDao = new SQLiteCircleDAO();
        // 每次测试前重新创建相关表，保证测试环境干净
        circleDao.createCircleTable(true);
        circleDao.createCircleMemberTable(true);
        circleDao.createCircleMessageTable(true);
    }
    @After
    public void tearDown() {
        // 测试结束后删除创建的表，清理环境
        /**
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS circle_message");
            statement.executeUpdate("DROP TABLE IF EXISTS circle_member");
            statement.executeUpdate("DROP TABLE IF EXISTS circle");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }
    // -------------------------------
    // 测试 Circle 相关操作
    // -------------------------------
    @Test
    public void testCreateCircleTable() {
        // 调用 createCircleTable(true) 重新创建表，并检查 sqlite_master 是否存在 circle 表
        circleDao.createCircleTable(true);
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='circle'");
            assertTrue("表 circle 应该存在", rs.next());
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("在 testCreateCircleTable 测试中发生异常");
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testAddCircle() {
        // 构造一个 Circle 对象，并调用 DAO 添加
        Circle circle = new Circle();
        circle.setName("测试圈子");
        circle.setDescription("这是一个测试圈子");
        circle.setOwnerUserId("tutor001");
        circle.setCreationTime(new Date());
        boolean added = circleDao.createCircle(circle);
        assertTrue("添加圈子应成功", added);
        assertTrue("圈子 ID 应被赋值", circle.getId() > 0);
        
        // 直接查询数据库验证数据写入是否正确
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM circle WHERE name='测试圈子'");
            assertTrue("查询到添加的圈子", rs.next());
            assertEquals("这是一个测试圈子", rs.getString("description"));
            assertEquals("tutor001", rs.getString("owner_user_id"));
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("在 testAddCircle 测试中发生异常");
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testUpdateCircle() {
        // 添加圈子后更新其数据，再查询验证
        Circle circle = new Circle();
        circle.setName("原始圈子");
        circle.setDescription("原始描述");
        circle.setOwnerUserId("tutor002");
        circle.setCreationTime(new Date());
        circleDao.createCircle(circle);
        circle.setName("更新后圈子");
        circle.setDescription("更新后描述");
        boolean updated = circleDao.updateCircle(circle);
        assertTrue("更新圈子应成功", updated);
        Circle fetched = circleDao.getCircleById(circle.getId());
        assertNotNull("更新后的圈子不应为 null", fetched);
        assertEquals("更新后圈子名称不匹配", "更新后圈子", fetched.getName());
        assertEquals("更新后描述不匹配", "更新后描述", fetched.getDescription());
    }
    @Test
    public void testDeleteCircle() {
        // 添加一个待删除的圈子，然后删除，再查询验证删除结果
        Circle circle = new Circle();
        circle.setName("待删除圈子");
        circle.setDescription("删除测试");
        circle.setOwnerUserId("tutor003");
        circle.setCreationTime(new Date());
        circleDao.createCircle(circle);
        boolean deleted = circleDao.deleteCircle(circle.getId());
        assertTrue("删除圈子应成功", deleted);
        Circle fetched = circleDao.getCircleById(circle.getId());
        assertNull("删除后的圈子应为 null", fetched);
    }
    @Test
    public void testGetAllCircles() {
        // 添加多个圈子，测试 getAllCircles 方法
        Circle circle1 = new Circle();
        circle1.setName("圈子一");
        circle1.setDescription("描述1");
        circle1.setOwnerUserId("tutor001");
        circle1.setCreationTime(new Date());
        circleDao.createCircle(circle1);
        Circle circle2 = new Circle();
        circle2.setName("圈子二");
        circle2.setDescription("描述2");
        circle2.setOwnerUserId("tutor002");
        circle2.setCreationTime(new Date());
        circleDao.createCircle(circle2);
        List<Circle> circles = circleDao.getAllCircles();
        assertNotNull("返回的圈子列表不应为空", circles);
        assertTrue("圈子列表中应至少有2个圈子", circles.size() >= 2);
    }
    // -------------------------------
    // 测试 CircleMember 相关操作
    // -------------------------------
    @Test
    public void testAddAndGetCircleMember() {
        // 先创建圈子，再添加成员，并查询验证
        Circle circle = new Circle();
        circle.setName("成员测试圈子");
        circle.setDescription("测试添加成员");
        circle.setOwnerUserId("tutor004");
        circle.setCreationTime(new Date());
        circleDao.createCircle(circle);
        CircleMember member = new CircleMember();
        member.setCircleId(circle.getId());
        member.setUserId("student001");
        member.setJoinTime(new Date());
        member.setRole("普通成员");
        boolean added = circleDao.addCircleMember(member);
        assertTrue("添加圈子成员应成功", added);
        assertTrue("成员 ID 应被赋值", member.getId() > 0);
        List<CircleMember> members = circleDao.getMembersByCircleId(circle.getId());
        boolean found = false;
        for (CircleMember m : members) {
            if ("student001".equals(m.getUserId())) {
                found = true;
                break;
            }
        }
        assertTrue("应能查询到添加的成员", found);
    }
    @Test
    public void testUpdateAndRemoveCircleMember() {
        // 创建圈子，并添加成员，再更新成员信息后删除该成员
        Circle circle = new Circle();
        circle.setName("更新删除成员测试圈子");
        circle.setDescription("测试更新和删除成员");
        circle.setOwnerUserId("tutor005");
        circle.setCreationTime(new Date());
        circleDao.createCircle(circle);
        CircleMember member = new CircleMember();
        member.setCircleId(circle.getId());
        member.setUserId("student002");
        member.setJoinTime(new Date());
        member.setRole("普通成员");
        circleDao.addCircleMember(member);
        // 更新成员角色
        member.setRole("圈子管理员");
        boolean updated = circleDao.updateCircleMember(member);
        assertTrue("更新成员应成功", updated);
        List<CircleMember> members = circleDao.getMembersByCircleId(circle.getId());
        boolean roleUpdated = false;
        for (CircleMember m : members) {
            if (m.getId() == member.getId() && "圈子管理员".equals(m.getRole())) {
                roleUpdated = true;
                break;
            }
        }
        assertTrue("成员角色应更新为圈子管理员", roleUpdated);
        // 删除成员
        boolean removed = circleDao.removeCircleMember(member.getId());
        assertTrue("删除成员应成功", removed);
        List<CircleMember> afterRemoval = circleDao.getMembersByCircleId(circle.getId());
        boolean exists = false;
        for (CircleMember m : afterRemoval) {
            if (m.getId() == member.getId()) {
                exists = true;
                break;
            }
        }
        assertFalse("删除后的成员应不存在", exists);
    }
    // -------------------------------
    // 测试 CircleMessage 相关操作
    // -------------------------------
    @Test
    public void testAddUpdateDeleteCircleMessage() {
        // 创建圈子用于测试消息操作
        Circle circle = new Circle();
        circle.setName("消息测试圈子");
        circle.setDescription("测试消息相关操作");
        circle.setOwnerUserId("tutor006");
        circle.setCreationTime(new Date());
        circleDao.createCircle(circle);
        // 添加消息
        CircleMessage message = new CircleMessage();
        message.setCircleId(circle.getId());
        message.setSenderUserId("tutor006");
        message.setContent("欢迎加入圈子！");
        message.setMessageType("公告");
        message.setTimestamp(new Date());
        boolean added = circleDao.addCircleMessage(message);
        assertTrue("添加圈子消息应成功", added);
        assertTrue("消息 ID 应被赋值", message.getId() > 0);
        // 更新消息内容
        message.setContent("更新后的公告：欢迎大家多交流！");
        boolean updated = circleDao.updateCircleMessage(message);
        assertTrue("更新圈子消息应成功", updated);
        List<CircleMessage> messages = circleDao.getMessagesByCircleId(circle.getId());
        boolean contentUpdated = false;
        for (CircleMessage m : messages) {
            if (m.getId() == message.getId() 
                && "更新后的公告：欢迎大家多交流！".equals(m.getContent())) {
                contentUpdated = true;
                break;
            }
        }
        assertTrue("消息内容应更新", contentUpdated);
        // 删除消息
        boolean deleted = circleDao.deleteCircleMessage(message.getId());
        assertTrue("删除圈子消息应成功", deleted);
        List<CircleMessage> afterDeletion = circleDao.getMessagesByCircleId(circle.getId());
        boolean exists = false;
        for (CircleMessage m : afterDeletion) {
            if (m.getId() == message.getId()) {
                exists = true;
                break;
            }
        }
        assertFalse("删除后的消息应不存在", exists);
    }
}