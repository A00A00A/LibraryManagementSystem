package org.example;
import java.util.ArrayList;
import java.util.List;
/**
 * BookContent 类用于存储书籍的全文、摘要信息，同时记录该书籍的所有用户评论/书评。
 * 功能描述：
 * 1. 存储和管理书籍的摘要及全文内容。
 * 2. 保存相关的用户书评，支持对书评数据的新增、删除和查询。
 */
public class BookContent {
    // 书籍内容记录的唯一标识（数据库自增主键）
    private int contentId;
    // 关联的书籍ID（外键，关联 Book 类）
    private int bookId;
    // 书籍的摘要或简介内容
    private String summary;
    // 书籍的全文内容
    private String fullText;
    // 书籍的用户书评列表（每条书评由 BookReview 表示）
    private List<BookReview> reviews;
    /**
     * 默认构造函数，初始化书评集合
     */
    public BookContent() {
        this.reviews = new ArrayList<>();
    }
    /**
     * 全参数构造函数，用于查询时封装数据
     * @param contentId 书籍内容记录的ID
     * @param bookId    关联的书籍ID
     * @param summary   书籍摘要
     * @param fullText  书籍全文内容
     * @param reviews   书籍对应的评论集合
     */
    public BookContent(int contentId, int bookId, String summary, String fullText, List<BookReview> reviews) {
        this.contentId = contentId;
        this.bookId = bookId;
        this.summary = summary;
        this.fullText = fullText;
        this.reviews = reviews != null ? reviews : new ArrayList<>();
    }
    /**
     * 构造函数，用于新增书籍内容时，不包含 contentId 和 reviews 参数
     * @param bookId   关联书籍ID
     * @param summary  书籍摘要
     * @param fullText 书籍全文内容
     */
    public BookContent(int bookId, String summary, String fullText) {
        this.bookId = bookId;
        this.summary = summary;
        this.fullText = fullText;
        this.reviews = new ArrayList<>();
    }
    // 以下是 Getter 和 Setter 方法
    public int getContentId() {
        return contentId;
    }
    public void setContentId(int contentId) {
        this.contentId = contentId;
    }
    public int getBookId() {
        return bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getFullText() {
        return fullText;
    }
    public void setFullText(String fullText) {
        this.fullText = fullText;
    }
    public List<BookReview> getReviews() {
        return reviews;
    }
    public void setReviews(List<BookReview> reviews) {
        this.reviews = reviews;
    }
    /**
     * 添加一条书评记录
     * @param review 待添加的书评对象
     */
    public void addReview(BookReview review) {
        if (review != null) {
            this.reviews.add(review);
        }
    }
    /**
     * 删除一条书评记录，通过书评ID删除
     * @param reviewId 要删除的书评ID
     * @return 如果删除成功返回 true，否则返回 false
     */
    public boolean removeReview(int reviewId) {
        for (BookReview review : reviews) {
            if (review.getReviewId() == reviewId) {
                return reviews.remove(review);
            }
        }
        return false;
    }
    /**
     * 根据用户ID获取该用户所有的书评记录
     * @param userId 用户ID（或用户名）
     * @return 返回该用户对应的所有书评
     */
    public List<BookReview> getReviewsByUser(String userId) {
        List<BookReview> userReviews = new ArrayList<>();
        for (BookReview review : reviews) {
            if (review.getUserId().equals(userId)) {
                userReviews.add(review);
            }
        }
        return userReviews;
    }
    @Override
    public String toString() {
        return "BookContent{" +
                "contentId=" + contentId +
                ", bookId=" + bookId +
                ", summary='" + summary + '\'' +
                ", fullText='"
                + (fullText != null ? (fullText.length() > 100 ? fullText.substring(0, 100) + "..." : fullText) : "")
                + '\'' +
                ", reviews=" + reviews +
                '}';
    }
}