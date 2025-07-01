package org.example.sqlite;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.example.Book;
import org.example.BookContent;
import org.example.BookDAO;
import org.example.BookReview;
import org.example.DatabaseConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class SQLiteBookDAOTest {
    private BookDAO bookDao; // BookDAO 对象，用于执行书籍数据的操作
    private DatabaseConfig config = new SQLiteDatabaseConfig(); // 获取数据库配置对象
    @Before
    public void setUp() {
        // 每个测试之前重新创建 books 表，保证测试环境干净
        bookDao = new SQLiteBookDAO();
        bookDao.createTable(true);
        // 创建书籍内容数据表
        bookDao.createBookContentTable(true);
        // 创建书评数据表
        bookDao.createBookReviewTable(true);
    }
    @After
    public void tearDown() {
        // 测试结束后的清理工作
    }
    /**
     * 测试创建 books 表是否成功
     */
    @Test
    public void testCreateTable() {
        // 重新创建表（传入 true 表示删除已存在的表然后创建）
        bookDao.createTable(true);
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            // 查询 sqlite_master 表以检查 books 表是否存在
            ResultSet resultSet = statement.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='books'");
            // 若查询结果存在，说明表已创建成功
            assertTrue("表 books 应该存在", resultSet.next());
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("在 testCreateTable 测试中发生异常");
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 测试添加书籍功能：添加后直接查询数据库验证数据是否正确写入
     */
    @Test
    public void testAddBook() {
        // 构造一本书籍对象
        Book book = new Book("9780134685991", "Effective Java", "Joshua Bloch", "Addison-Wesley", 2018);
        boolean added = bookDao.addBook(book);
        // 确保添加操作返回 true
        assertTrue("书籍应成功添加", added);
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            // 根据 ISBN 查询该书籍记录
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM books WHERE isbn='9780134685991'");
            // 查询结果必须存在
            assertTrue("查询到添加的书籍", resultSet.next());
            // 验证书名、作者、出版社、出版年份是否正确
            assertEquals("Effective Java", resultSet.getString("title"));
            assertEquals("Joshua Bloch", resultSet.getString("author"));
            assertEquals("Addison-Wesley", resultSet.getString("publisher"));
            assertEquals(2018, resultSet.getInt("published_year"));
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("在 testAddBook 测试中发生异常");
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 测试更新书籍信息：添加书籍后修改书名和出版社，然后验证更新是否成功
     */
    @Test
    public void testUpdateBook() {
        // 先添加书籍
        Book book = new Book("9780134685991", "Effective Java", "Joshua Bloch", "Addison-Wesley", 2018);
        boolean added = bookDao.addBook(book);
        assertTrue("更新测试前应先添加书籍", added);
        // 使用 getAllBooks() 获取所有书籍，并取第一个作为测试对象
        List<Book> books = bookDao.getAllBooks();
        assertFalse("getAllBooks() 返回的列表不应为空", books.isEmpty());
        Book fetchedBook = books.get(0);
        // 修改书名和出版社
        fetchedBook.setTitle("Effective Java - 3rd Edition");
        fetchedBook.setPublisher("Pearson");
        boolean updated = bookDao.updateBook(fetchedBook);
        assertTrue("书籍应成功更新", updated);
        // 通过 getBookById 查询更新后的书籍，验证信息是否正确更新
        Book updatedBook = bookDao.getBookById(fetchedBook.getId());
        assertNotNull("更新后的书籍不应为 null", updatedBook);
        assertEquals("Effective Java - 3rd Edition", updatedBook.getTitle());
        assertEquals("Pearson", updatedBook.getPublisher());
    }
    /**
     * 测试删除书籍功能：添加书籍后删除，再验证书籍是否正确移除
     */
    @Test
    public void testDeleteBook() {
        // 添加一本书用于删除测试
        Book book = new Book("9780134685991", "Effective Java", "Joshua Bloch", "Addison-Wesley", 2018);
        boolean added = bookDao.addBook(book);
        assertTrue("用于删除测试的书籍应成功添加", added);
        // 通过 getAllBooks() 获取书籍，获取目标书籍的 ID
        List<Book> books = bookDao.getAllBooks();
        assertFalse("书籍列表不应为空", books.isEmpty());
        Book fetchedBook = books.get(0);
        // 执行删除操作
        boolean deleted = bookDao.deleteBook(fetchedBook.getId());
        assertTrue("书籍应成功删除", deleted);
        // 通过 getBookById 验证书籍是否已被删除（应返回 null）
        Book deletedBook = bookDao.getBookById(fetchedBook.getId());
        assertNull("删除的书籍应无法检索到", deletedBook);
    }
    /**
     * 测试通过书籍 ID 获取书籍信息的功能
     */
    @Test
    public void testGetBookById() {
        // 添加一本用于测试的书籍
        Book book = new Book("9780134685991", "Effective Java", "Joshua Bloch", "Addison-Wesley", 2018);
        boolean added = bookDao.addBook(book);
        assertTrue("用于 getBookById 测试的书籍应成功添加", added);
        // 从 getAllBooks() 获取刚添加的书籍以便获取其 ID
        List<Book> books = bookDao.getAllBooks();
        assertFalse("书籍列表不应为空", books.isEmpty());
        Book fetchedBook = books.get(0);
        // 使用书籍 ID 查询书籍信息
        Book retrievedBook = bookDao.getBookById(fetchedBook.getId());
        assertNotNull("通过 ID 应能找到对应的书籍", retrievedBook);
        // 验证各项属性是否一致
        assertEquals(fetchedBook.getId(), retrievedBook.getId());
        assertEquals("9780134685991", retrievedBook.getIsbn());
        assertEquals("Effective Java", retrievedBook.getTitle());
        assertEquals("Joshua Bloch", retrievedBook.getAuthor());
        assertEquals("Addison-Wesley", retrievedBook.getPublisher());
        assertEquals(2018, retrievedBook.getPublishedYear());
    }
    /**
     * 测试获取所有书籍的功能：添加两本书后应返回包含这两本的列表
     */
    @Test
    public void testGetAllBooks() {
        // 构造两本不同的书籍对象
        Book book1 = new Book("9780134685991", "Effective Java", "Joshua Bloch", "Addison-Wesley", 2018);
        Book book2 = new Book("9780132350884", "Clean Code", "Robert C. Martin", "Prentice Hall", 2008);
        assertTrue("书籍1应成功添加", bookDao.addBook(book1));
        assertTrue("书籍2应成功添加", bookDao.addBook(book2));
        // 调用 getAllBooks 获取所有书籍
        List<Book> bookList = bookDao.getAllBooks();
        assertNotNull("getAllBooks() 返回的列表不应为 null", bookList);
        assertEquals("书籍列表中应有 2 本书", 2, bookList.size());
        // 遍历列表，根据 ISBN 判断两本书是否都存在
        boolean foundEffectiveJava = false, foundCleanCode = false;
        for (Book b : bookList) {
            if ("9780134685991".equals(b.getIsbn())) {
                foundEffectiveJava = true;
                assertEquals("Effective Java", b.getTitle());
                assertEquals("Joshua Bloch", b.getAuthor());
                assertEquals("Addison-Wesley", b.getPublisher());
                assertEquals(2018, b.getPublishedYear());
            } else if ("9780132350884".equals(b.getIsbn())) {
                foundCleanCode = true;
                assertEquals("Clean Code", b.getTitle());
                assertEquals("Robert C. Martin", b.getAuthor());
                assertEquals("Prentice Hall", b.getPublisher());
                assertEquals(2008, b.getPublishedYear());
            }
        }
        assertTrue("书籍列表中应包含 Effective Java", foundEffectiveJava);
        assertTrue("书籍列表中应包含 Clean Code", foundCleanCode);
    }
    /**
     * 测试创建 book_contents 表是否成功
     */
    @Test
    public void testCreateBookContentTable() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            // 查询 sqlite_master 表，检查是否存在 book_contents 表
            ResultSet rs = statement.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='book_contents'"
            );
            assertTrue("book_contents 表应已创建", rs.next());
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("testCreateBookContentTable 测试出现异常");
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 测试添加和获取书籍内容（BookContent）
     */
    @Test
    public void testAddAndGetBookContent() {
        // 定义测试书籍ID，此处假设书籍 ID 为 1
        int bookId = 1;
        String summary = "这是该书的摘要信息。";
        String fullText = "这是该书的完整内容，内容非常丰富。";
        // 构造 BookContent 对象
        BookContent content = new BookContent(bookId, summary, fullText);
        boolean added = bookDao.addBookContent(content);
        assertTrue("BookContent 插入应成功", added);
        // 通过 bookId 获取书籍内容记录
        BookContent retrievedContent = bookDao.getBookContentByBookId(bookId);
        assertNotNull("应根据 bookId 获取到对应的 BookContent", retrievedContent);
        assertEquals("摘要信息应匹配", summary, retrievedContent.getSummary());
        assertEquals("全文内容应匹配", fullText, retrievedContent.getFullText());
    }
    /**
     * 测试更新书籍内容记录（BookContent）
     */
    @Test
    public void testUpdateBookContent() {
        int bookId = 2;
        // 初始插入书籍内容
        BookContent content = new BookContent(bookId, "初始摘要", "初始全文内容。");
        boolean added = bookDao.addBookContent(content);
        assertTrue("初始添加 BookContent 应成功", added);
        // 更新摘要与全文内容
        content.setSummary("更新后的摘要");
        content.setFullText("更新后的完整内容。");
        boolean updated = bookDao.updateBookContent(content);
        assertTrue("BookContent 更新应成功", updated);
        BookContent updatedContent = bookDao.getBookContentByBookId(bookId);
        assertNotNull("更新后应能获取对应 BookContent", updatedContent);
        assertEquals("更新后的摘要应匹配", "更新后的摘要", updatedContent.getSummary());
        assertEquals("更新后的全文应匹配", "更新后的完整内容。", updatedContent.getFullText());
    }
    /**
     * 测试创建 book_reviews 表是否成功
     */
    @Test
    public void testCreateBookReviewTable() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = config.getConnection();
            statement = connection.createStatement();
            // 查询 sqlite_master 表，检查是否存在 book_reviews 表
            ResultSet rs = statement.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='book_reviews'"
            );
            assertTrue("book_reviews 表应已创建", rs.next());
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("testCreateBookReviewTable 测试出现异常");
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 测试添加书评记录并根据 bookId 获取书评列表
     */
    @Test
    public void testAddAndGetBookReview() {
        int bookId = 1;
        // 构造一个书评记录，使用当前时间作为评论日期
        BookReview review = new BookReview(bookId, "testUser", "这本书的内容很丰富，值得一读。", 5, new Date());
        boolean added = bookDao.addBookReview(review);
        assertTrue("BookReview 插入应成功", added);
        // 根据 bookId 获取所有书评记录
        List<BookReview> reviews = bookDao.getBookReviewsByBookId(bookId);
        assertNotNull("获取书评列表应不为空", reviews);
        assertTrue("书评列表至少包含一条记录", reviews.size() > 0);
        // 验证书评内容是否有“testUser”的记录，并检查详细信息
        boolean foundReview = false;
        for (BookReview r : reviews) {
            if ("testUser".equals(r.getUserId())) {
                foundReview = true;
                assertEquals("书评内容应匹配", "这本书的内容很丰富，值得一读。", r.getReviewText());
                assertEquals("评分应匹配", 5, r.getRating());
                break;
            }
        }
        assertTrue("应找到来自 testUser 的书评记录", foundReview);
    }
    /**
     * 测试通过网络导入书籍全文，本测试使用 http://www.example.com 检查返回内容中包含 "Example Domain"
     */
    @Test
    public void testFetchFullTextFromURL() {
        String urlStr = "http://www.example.com";
        String fullText = bookDao.fetchFullTextFromURL(urlStr);
        assertNotNull("从网络导入的全文内容不应为 null", fullText);
        // 验证返回的内容是否包含 "Example Domain"（example.com 的标准页面内容）
        assertTrue("网络返回的内容应包含 'Example Domain'", fullText.contains("Example Domain"));
    }
}