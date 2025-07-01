package org.example.sqlite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.example.Circle;
import org.example.CircleDAO;
import org.example.CircleMember;
import org.example.CircleMessage;
import org.example.DatabaseConfig;
public class SQLiteCircleDAO implements CircleDAO {
    private DatabaseConfig config = new SQLiteDatabaseConfig();
    // ------------------------------
    // 创建表方法
    // ------------------------------
    @Override
    public boolean createCircleTable(boolean dropExistingTable) {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = config.getConnection();
            stmt = connection.createStatement();
            if (dropExistingTable) {
                stmt.executeUpdate("DROP TABLE IF EXISTS circle");
            }
            String sql = "CREATE TABLE IF NOT EXISTS circle (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "description TEXT, " +
                    "owner_user_id TEXT, " +
                    "creation_time DATETIME" +
                    ")";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("创建 circle 表时错误: " + e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean createCircleMemberTable(boolean dropExistingTable) {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = config.getConnection();
            stmt = connection.createStatement();
            if (dropExistingTable) {
                stmt.executeUpdate("DROP TABLE IF EXISTS circle_member");
            }
            String sql = "CREATE TABLE IF NOT EXISTS circle_member (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "circle_id INTEGER NOT NULL, " +
                    "user_id TEXT NOT NULL, " +
                    "join_time DATETIME, " +
                    "role TEXT" +
                    ")";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("创建 circle_member 表时错误: " + e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean createCircleMessageTable(boolean dropExistingTable) {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = config.getConnection();
            stmt = connection.createStatement();
            if (dropExistingTable) {
                stmt.executeUpdate("DROP TABLE IF EXISTS circle_message");
            }
            String sql = "CREATE TABLE IF NOT EXISTS circle_message (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "circle_id INTEGER NOT NULL, " +
                    "sender_user_id TEXT NOT NULL, " +
                    "content TEXT, " +
                    "message_type TEXT, " +
                    "timestamp DATETIME" +
                    ")";
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("创建 circle_message 表时错误: " + e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    // ------------------------------
    // Circle 相关操作
    // ------------------------------
    @Override
    public boolean createCircle(Circle circle) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "INSERT INTO circle (name, description, owner_user_id, creation_time) VALUES (?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, circle.getName());
            stmt.setString(2, circle.getDescription());
            stmt.setString(3, circle.getOwnerUserId());
            stmt.setTimestamp(4, new Timestamp(circle.getCreationTime().getTime()));
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    circle.setId(rs.getInt(1));
                }
                rs.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean updateCircle(Circle circle) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "UPDATE circle SET name = ?, description = ?, owner_user_id = ?, creation_time = ? WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, circle.getName());
            stmt.setString(2, circle.getDescription());
            stmt.setString(3, circle.getOwnerUserId());
            stmt.setTimestamp(4, new Timestamp(circle.getCreationTime().getTime()));
            stmt.setInt(5, circle.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean deleteCircle(int circleId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "DELETE FROM circle WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, circleId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public Circle getCircleById(int circleId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Circle circle = null;
        try {
            connection = config.getConnection();
            String sql = "SELECT * FROM circle WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, circleId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                circle = new Circle();
                circle.setId(rs.getInt("id"));
                circle.setName(rs.getString("name"));
                circle.setDescription(rs.getString("description"));
                circle.setOwnerUserId(rs.getString("owner_user_id"));
                circle.setCreationTime(rs.getTimestamp("creation_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return circle;
    }
    @Override
    public List<Circle> getAllCircles() {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Circle> circles = new ArrayList<>();
        try {
            connection = config.getConnection();
            String sql = "SELECT * FROM circle";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Circle circle = new Circle();
                circle.setId(rs.getInt("id"));
                circle.setName(rs.getString("name"));
                circle.setDescription(rs.getString("description"));
                circle.setOwnerUserId(rs.getString("owner_user_id"));
                circle.setCreationTime(rs.getTimestamp("creation_time"));
                circles.add(circle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return circles;
    }
    // ------------------------------
    // CircleMember 相关操作
    // ------------------------------
    @Override
    public boolean addCircleMember(CircleMember member) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "INSERT INTO circle_member (circle_id, user_id, join_time, role) VALUES (?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, member.getCircleId());
            stmt.setString(2, member.getUserId());
            stmt.setTimestamp(3, new Timestamp(member.getJoinTime().getTime()));
            stmt.setString(4, member.getRole());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    member.setId(rs.getInt(1));
                }
                rs.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean updateCircleMember(CircleMember member) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "UPDATE circle_member SET circle_id = ?, user_id = ?, join_time = ?, role = ? WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, member.getCircleId());
            stmt.setString(2, member.getUserId());
            stmt.setTimestamp(3, new Timestamp(member.getJoinTime().getTime()));
            stmt.setString(4, member.getRole());
            stmt.setInt(5, member.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean removeCircleMember(int memberId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "DELETE FROM circle_member WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, memberId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public List<CircleMember> getMembersByCircleId(int circleId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CircleMember> members = new ArrayList<>();
        try {
            connection = config.getConnection();
            String sql = "SELECT * FROM circle_member WHERE circle_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, circleId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                CircleMember member = new CircleMember();
                member.setId(rs.getInt("id"));
                member.setCircleId(rs.getInt("circle_id"));
                member.setUserId(rs.getString("user_id"));
                member.setJoinTime(rs.getTimestamp("join_time"));
                member.setRole(rs.getString("role"));
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return members;
    }
    // ------------------------------
    // CircleMessage 相关操作
    // ------------------------------
    @Override
    public boolean addCircleMessage(CircleMessage message) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "INSERT INTO circle_message (circle_id, sender_user_id, content, message_type, timestamp) VALUES (?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, message.getCircleId());
            stmt.setString(2, message.getSenderUserId());
            stmt.setString(3, message.getContent());
            stmt.setString(4, message.getMessageType());
            stmt.setTimestamp(5, new Timestamp(message.getTimestamp().getTime()));
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    message.setId(rs.getInt(1));
                }
                rs.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean updateCircleMessage(CircleMessage message) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "UPDATE circle_message SET circle_id = ?, sender_user_id = ?, content = ?, message_type = ?, timestamp = ? WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, message.getCircleId());
            stmt.setString(2, message.getSenderUserId());
            stmt.setString(3, message.getContent());
            stmt.setString(4, message.getMessageType());
            stmt.setTimestamp(5, new Timestamp(message.getTimestamp().getTime()));
            stmt.setInt(6, message.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean deleteCircleMessage(int messageId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "DELETE FROM circle_message WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, messageId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public List<CircleMessage> getMessagesByCircleId(int circleId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CircleMessage> messages = new ArrayList<>();
        try {
            connection = config.getConnection();
            String sql = "SELECT * FROM circle_message WHERE circle_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, circleId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                CircleMessage message = new CircleMessage();
                message.setId(rs.getInt("id"));
                message.setCircleId(rs.getInt("circle_id"));
                message.setSenderUserId(rs.getString("sender_user_id"));
                message.setContent(rs.getString("content"));
                message.setMessageType(rs.getString("message_type"));
                message.setTimestamp(rs.getTimestamp("timestamp"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }
}