package org.example;
import java.util.Date;
/**
 * BookReview 类用于记录用户对书籍的评论以及读书心得体会。
 * 每个 BookReview 对象表示一条用户书评记录，包含评论内容、评分等信息。
 */
public class BookReview {
    private int reviewId; // 书评的唯一标识
    private int bookId; // 外键，关联的书籍 ID
    private String userId; // 发表评论的用户 ID，可以根据需要设计为 username 或用户对象的引用
    private String reviewText; // 用户的书评或心得体会内容
    private int rating; // 用户评分（例如 1-5 分）
    private Date reviewDate; // 书评的发布时间
    // 默认构造函数
    public BookReview() {
    }
    // 全参数构造函数（带 reviewId，适用于查询时的数据封装）
    public BookReview(int reviewId, int bookId, String userId, String reviewText, int rating, Date reviewDate) {
        this.reviewId = reviewId;
        this.bookId = bookId;
        this.userId = userId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }
    // 无 reviewId 构造函数（用于新增书评时）
    public BookReview(int bookId, String userId, String reviewText, int rating, Date reviewDate) {
        this(0, bookId, userId, reviewText, rating, reviewDate);
    }
    // Getter 与 Setter 方法
    public int getReviewId() {
        return reviewId;
    }
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    public int getBookId() {
        return bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getReviewText() {
        return reviewText;
    }
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public Date getReviewDate() {
        return reviewDate;
    }
    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
    @Override
    public String toString() {
        return "BookReview{" +
                "reviewId=" + reviewId +
                ", bookId=" + bookId +
                ", userId='" + userId + '\'' +
                ", reviewText='" + reviewText + '\'' +
                ", rating=" + rating +
                ", reviewDate=" + reviewDate +
                '}';
    }
}