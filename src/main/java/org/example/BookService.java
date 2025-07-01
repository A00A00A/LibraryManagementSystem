package org.example;
import java.util.List;
public class BookService {
    private BookDAO bookDao;
    public BookService(BookDAO bookDao) {
        this.bookDao = bookDao;
    }
    // 书籍基本信息管理方法
    public boolean addBook(Book book) {
        return bookDao.addBook(book);
    }
    public boolean updateBook(Book book) {
        return bookDao.updateBook(book);
    }
    public boolean deleteBook(int bookId) {
        return bookDao.deleteBook(bookId);
    }
    public Book getBookById(int bookId) {
        return bookDao.getBookById(bookId);
    }
    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }
    public boolean createTable(boolean dropExistingTable) {
        return bookDao.createTable(dropExistingTable);
    }
    // 书籍内容管理方法
    public boolean createBookContentTable(boolean dropExistingTable) {
        return bookDao.createBookContentTable(dropExistingTable);
    }
    public boolean addBookContent(BookContent content) {
        return bookDao.addBookContent(content);
    }
    public boolean updateBookContent(BookContent content) {
        return bookDao.updateBookContent(content);
    }
    public BookContent getBookContentByBookId(int bookId) {
        return bookDao.getBookContentByBookId(bookId);
    }
    // 书评管理方法
    public boolean createBookReviewTable(boolean dropExistingTable) {
        return bookDao.createBookReviewTable(dropExistingTable);
    }
    public boolean addBookReview(BookReview review) {
        return bookDao.addBookReview(review);
    }
    public List<BookReview> getBookReviewsByBookId(int bookId) {
        return bookDao.getBookReviewsByBookId(bookId);
    }
    // 网络导入书籍全文数据的方法
    public String fetchFullTextFromURL(String url) {
        return bookDao.fetchFullTextFromURL(url);
    }
}