package org.example;
import java.util.List;
public interface BookDAO {
    // Book 基础信息的管理方法
    boolean addBook(Book book);
    boolean updateBook(Book book);
    boolean deleteBook(int bookId);
    Book getBookById(int bookId);
    List<Book> getAllBooks();
    boolean createTable(boolean dropExistingTable);
    // 管理书籍内容（全文与摘要）
    boolean addBookContent(BookContent content);
    boolean updateBookContent(BookContent content);
    BookContent getBookContentByBookId(int bookId);
    boolean createBookContentTable(boolean dropExistingTable);
    // 管理书籍书评（用户评论）
    boolean createBookReviewTable(boolean dropExistingTable);
    boolean addBookReview(BookReview review);
    List<BookReview> getBookReviewsByBookId(int bookId);
    // 通过网络导入书籍全文数据
    String fetchFullTextFromURL(String urlStr);
}