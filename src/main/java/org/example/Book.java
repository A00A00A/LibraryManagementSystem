package org.example;
public class Book {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int publishedYear;
    // 默认构造函数
    public Book() {
    }
    // 全参数构造函数（包含 id，用于查询时的数据封装）
    public Book(int id, String isbn, String title, String author, String publisher, int publishedYear) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
    }
    // 无 id 参数的构造函数（用于新增书籍时）
    public Book(String isbn, String title, String author, String publisher, int publishedYear) {
        this(0, isbn, title, author, publisher, publishedYear);
    }
    // Getter 与 Setter 方法
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public int getPublishedYear() {
        return publishedYear;
    }
    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishedYear=" + publishedYear +
                '}';
    }
}