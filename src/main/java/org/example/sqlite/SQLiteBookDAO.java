package org.example.sqlite;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.example.Book;
import org.example.BookContent;
import org.example.BookDAO;
import org.example.BookReview;
import org.example.DatabaseConfig;
public class SQLiteBookDAO implements BookDAO {
    private DatabaseConfig config = new SQLiteDatabaseConfig();
    @Override
    public boolean addBook(Book book) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "INSERT INTO books (isbn, title, author, publisher, published_year) VALUES (?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getPublisher());
            stmt.setInt(5, book.getPublishedYear());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
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
    public boolean updateBook(Book book) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "UPDATE books SET isbn = ?, title = ?, author = ?, publisher = ?, published_year = ? WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getPublisher());
            stmt.setInt(5, book.getPublishedYear());
            stmt.setInt(6, book.getId());
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
    public boolean deleteBook(int bookId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "DELETE FROM books WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, bookId);
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
    public Book getBookById(int bookId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Book book = null;
        try {
            connection = config.getConnection();
            String sql = "SELECT * FROM books WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, bookId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublishedYear(rs.getInt("published_year"));
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
        return book;
    }
    @Override
    public List<Book> getAllBooks() {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        try {
            connection = config.getConnection();
            String sql = "SELECT * FROM books";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublishedYear(rs.getInt("published_year"));
                books.add(book);
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
        return books;
    }
    @Override
    public boolean createTable(boolean dropExistingTable) {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = config.getConnection();
            stmt = connection.createStatement();
            if (dropExistingTable) {
                String dropSql = "DROP TABLE IF EXISTS books";
                stmt.executeUpdate(dropSql);
            }
            String createSql = "CREATE TABLE IF NOT EXISTS books (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "isbn TEXT NOT NULL UNIQUE, " +
                    "title TEXT NOT NULL, " +
                    "author TEXT NOT NULL, " +
                    "publisher TEXT, " +
                    "published_year INTEGER" +
                    ")";
            stmt.executeUpdate(createSql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating books table: " + e.getMessage());
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
    // ======================
    // BookContent 相关方法
    // ======================
    /**
     * 创建用于存储书籍内容的表 book_contents
     * 表结构：content_id (主键)，book_id，summary，full_text
     */
    public boolean createBookContentTable(boolean dropExistingTable) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            if (dropExistingTable) {
                String dropQuery = "DROP TABLE IF EXISTS book_contents";
                statement.executeUpdate(dropQuery);
            }
            String createQuery = "CREATE TABLE IF NOT EXISTS book_contents (" +
                    "content_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "book_id INTEGER NOT NULL, " +
                    "summary TEXT, " +
                    "full_text TEXT, " +
                    "FOREIGN KEY(book_id) REFERENCES books(id)" +
                    ")";
            statement.executeUpdate(createQuery);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
    /**
     * 插入书籍内容记录（书籍摘要与全文）
     */
    public boolean addBookContent(BookContent content) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "INSERT INTO book_contents (book_id, summary, full_text) VALUES (?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, content.getBookId());
            stmt.setString(2, content.getSummary());
            stmt.setString(3, content.getFullText());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 更新书籍内容记录
     */
    public boolean updateBookContent(BookContent content) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "UPDATE book_contents SET summary = ?, full_text = ? WHERE book_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, content.getSummary());
            stmt.setString(2, content.getFullText());
            stmt.setInt(3, content.getBookId());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 根据书籍ID获取书籍内容记录
     */
    public BookContent getBookContentByBookId(int bookId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BookContent content = null;
        try {
            connection = config.getConnection();
            String sql = "SELECT * FROM book_contents WHERE book_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, bookId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int contentId = rs.getInt("content_id");
                String summary = rs.getString("summary");
                String fullText = rs.getString("full_text");
                content = new BookContent(contentId, bookId, summary, fullText, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return content;
    }
    // ======================
    // BookReview 相关方法
    // ======================
    /**
     * 创建用于存储书籍评论的表 book_reviews
     * 表结构：review_id (主键)，book_id，user_id，review_text，rating，review_date
     */
    public boolean createBookReviewTable(boolean dropExistingTable) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            if (dropExistingTable) {
                String dropQuery = "DROP TABLE IF EXISTS book_reviews";
                statement.executeUpdate(dropQuery);
            }
            String createQuery = "CREATE TABLE IF NOT EXISTS book_reviews (" +
                    "review_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "book_id INTEGER NOT NULL, " +
                    "user_id TEXT NOT NULL, " +
                    "review_text TEXT, " +
                    "rating INTEGER, " +
                    "review_date DATETIME, " +
                    "FOREIGN KEY(book_id) REFERENCES books(id)" +
                    ")";
            statement.executeUpdate(createQuery);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
    /**
     * 插入书籍评论记录
     */
    public boolean addBookReview(BookReview review) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = config.getConnection();
            String sql = "INSERT INTO book_reviews (book_id, user_id, review_text, rating, review_date) VALUES (?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, review.getBookId());
            stmt.setString(2, review.getUserId());
            stmt.setString(3, review.getReviewText());
            stmt.setInt(4, review.getRating());
            stmt.setTimestamp(5, new Timestamp(review.getReviewDate().getTime()));
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 根据书籍ID获取所有关联的书籍评论
     */
    public List<BookReview> getBookReviewsByBookId(int bookId) {
        List<BookReview> reviews = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = config.getConnection();
            String sql = "SELECT * FROM book_reviews WHERE book_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, bookId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int reviewId = rs.getInt("review_id");
                String userId = rs.getString("user_id");
                String reviewText = rs.getString("review_text");
                int rating = rs.getInt("rating");
                Timestamp timestamp = rs.getTimestamp("review_date");
                java.util.Date reviewDate = new java.util.Date(timestamp.getTime());
                BookReview review = new BookReview(reviewId, bookId, userId, reviewText, rating, reviewDate);
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return reviews;
    }
    // ======================
    // 网络导入相关方法
    // ======================
    /**
     * 通过指定 URL 从网络导入书籍全文内容
     * 返回所读取的全文字符串
     */
    public String fetchFullTextFromURL(String urlStr) {
        StringBuilder fullText = new StringBuilder();
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            // 使用 UTF-8 编码读取网络输入流
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                fullText.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fullText.toString();
    }
}