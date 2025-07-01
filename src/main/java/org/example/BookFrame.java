package org.example;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
/**
 * BookFrame 类为图书管理系统提供书籍信息管理的图形界面，
 * 除了支持书籍的增删改查外，还能管理书籍内容（摘要与全文）和用户书评，
 * 并可通过网络导入书籍的全文数据。
 */
public class BookFrame extends JFrame {
    private JTable bookTable;
    private JButton addButton, updateButton, deleteButton, refreshButton;
    private JButton editContentButton, importFullTextButton, viewReviewsButton;
    // 非管理员专用按钮
    private JButton shareThoughtsButton;       // 学生专用
    private JButton recommendBookButton, commentOnReviewsButton; // 导师专用
    private JPanel buttonPanel;
    private JLabel messageLabel;
    private BookService bookService;
    private GlobalState globalState;
    /**
     * 构造方法，传入图书管理业务接口及全局状态对象
     * @param bookService 图书管理业务接口
     * @param globalState 全局状态对象，用于获取当前登录用户及角色信息
     */
    public BookFrame(BookService bookService, GlobalState globalState) {
        this.bookService = bookService;
        this.globalState = globalState;
        initializeComponents();
        initializeFrame();
    }
    // 在 BookFrame 类中，初始化组件时不根据信息隐藏任何组件，全部创建
    private void initializeComponents() {
        bookTable = new JTable();
        addButton = new JButton("添加书籍");
        updateButton = new JButton("修改书籍");
        deleteButton = new JButton("删除书籍");
        refreshButton = new JButton("刷新列表");
        editContentButton = new JButton("编辑内容");
        importFullTextButton = new JButton("导入全文");
        viewReviewsButton = new JButton("查看书评");
        // 创建非管理员专用按钮
        shareThoughtsButton = new JButton("分享读书心得");
        recommendBookButton = new JButton("推荐图书");
        commentOnReviewsButton = new JButton("点评书评");
        buttonPanel = new JPanel();
        // 将所有按钮一次性添加（默认全部可见，后续根据角色调整）
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(editContentButton);
        buttonPanel.add(importFullTextButton);
        buttonPanel.add(viewReviewsButton);
        buttonPanel.add(shareThoughtsButton);
        buttonPanel.add(recommendBookButton);
        buttonPanel.add(commentOnReviewsButton);
        messageLabel = new JLabel("欢迎使用图书管理系统", SwingConstants.CENTER);
    }
    // 初始化窗口属性与布局
    private void initializeFrame() {
        this.setTitle("图书管理系统");
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(bookTable), BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(messageLabel, BorderLayout.NORTH);
        loadTableData();
        setButtonListeners();
    }
    /**
     * 从业务层加载所有书籍的基础信息，并填充到表格中
     */
    private void loadTableData() {
        List<Book> books = bookService.getAllBooks();
        String[] columnNames = { "编号", "ISBN", "书名", "作者", "出版社", "出版年份" };
        Object[][] data = new Object[books.size()][6];
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            data[i][0] = b.getId();
            data[i][1] = b.getIsbn();
            data[i][2] = b.getTitle();
            data[i][3] = b.getAuthor();
            data[i][4] = b.getPublisher();
            data[i][5] = b.getPublishedYear();
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable.setModel(tableModel);
    }
    /**
     * 设置各按钮的事件监听器，处理书籍基本信息、内容和书评管理
     */
    private void setButtonListeners() {
        // 添加书籍按钮
        addButton.addActionListener(e -> {
            JTextField isbnField = new JTextField();
            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JTextField publisherField = new JTextField();
            JTextField yearField = new JTextField();
            Object[] fields = {
                    "ISBN：", isbnField,
                    "书名：", titleField,
                    "作者：", authorField,
                    "出版社：", publisherField,
                    "出版年份：", yearField
            };
            int option = JOptionPane.showConfirmDialog(BookFrame.this, fields, "添加新书籍", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String isbn = isbnField.getText().trim();
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String publisher = publisherField.getText().trim();
                int year;
                try {
                    year = Integer.parseInt(yearField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BookFrame.this, "出版年份必须为整数", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(BookFrame.this, "ISBN、书名和作者不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Book book = new Book(isbn, title, author, publisher, year);
                if (bookService.addBook(book)) {
                    messageLabel.setText("添加书籍成功");
                    loadTableData();
                } else {
                    messageLabel.setText("添加书籍失败");
                }
            }
        });
        // 修改书籍按钮
        updateButton.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(BookFrame.this, "请选择要修改的书籍", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) bookTable.getValueAt(row, 0);
            String isbn = (String) bookTable.getValueAt(row, 1);
            String title = (String) bookTable.getValueAt(row, 2);
            String author = (String) bookTable.getValueAt(row, 3);
            String publisher = (String) bookTable.getValueAt(row, 4);
            int year = (int) bookTable.getValueAt(row, 5);
            JTextField isbnField = new JTextField(isbn);
            JTextField titleField = new JTextField(title);
            JTextField authorField = new JTextField(author);
            JTextField publisherField = new JTextField(publisher);
            JTextField yearField = new JTextField(String.valueOf(year));
            Object[] fields = {
                    "ISBN：", isbnField,
                    "书名：", titleField,
                    "作者：", authorField,
                    "出版社：", publisherField,
                    "出版年份：", yearField
            };
            int option = JOptionPane.showConfirmDialog(BookFrame.this, fields, "修改书籍信息", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String newIsbn = isbnField.getText().trim();
                String newTitle = titleField.getText().trim();
                String newAuthor = authorField.getText().trim();
                String newPublisher = publisherField.getText().trim();
                int newYear;
                try {
                    newYear = Integer.parseInt(yearField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BookFrame.this, "出版年份必须为整数", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Book updatedBook = new Book(id, newIsbn, newTitle, newAuthor, newPublisher, newYear);
                if (bookService.updateBook(updatedBook)) {
                    messageLabel.setText("修改书籍成功");
                    loadTableData();
                } else {
                    messageLabel.setText("修改书籍失败");
                }
            }
        });
        // 删除书籍按钮
        deleteButton.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(BookFrame.this, "请选择要删除的书籍", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) bookTable.getValueAt(row, 0);
            int option = JOptionPane.showConfirmDialog(BookFrame.this, "确认删除选中的书籍？", "删除确认", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (bookService.deleteBook(id)) {
                    messageLabel.setText("删除书籍成功");
                    loadTableData();
                } else {
                    messageLabel.setText("删除书籍失败");
                }
            }
        });
        // 刷新列表按钮
        refreshButton.addActionListener(e -> {
            loadTableData();
            messageLabel.setText("列表已刷新");
        });
        // 编辑内容按钮：显示 BookContent 信息并允许修改摘要和全文
        editContentButton.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(BookFrame.this, "请选择要编辑内容的书籍", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int bookId = (int) bookTable.getValueAt(row, 0);
            // 获取当前书籍对应的 BookContent
            BookContent content = bookService.getBookContentByBookId(bookId);
            if (content == null) {
                // 如果没有内容记录，则创建一个空的 BookContent
                content = new BookContent(bookId, "", "");
            }
            JTextArea summaryArea = new JTextArea(content.getSummary(), 5, 20);
            JTextArea fullTextArea = new JTextArea(content.getFullText(), 10, 20);
            JScrollPane summaryScroll = new JScrollPane(summaryArea);
            JScrollPane fullTextScroll = new JScrollPane(fullTextArea);
            Object[] fields = {
                    "摘要：", summaryScroll,
                    "全文：", fullTextScroll
            };
            int option = JOptionPane.showConfirmDialog(BookFrame.this, fields, "编辑书籍内容", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                content.setSummary(summaryArea.getText());
                content.setFullText(fullTextArea.getText());
                // 如果已有记录则更新，否则添加新内容记录
                boolean result;
                if (content.getContentId() > 0) {
                    result = bookService.updateBookContent(content);
                } else {
                    result = bookService.addBookContent(content);
                }
                if (result) {
                    messageLabel.setText("更新书籍内容成功");
                } else {
                    messageLabel.setText("更新书籍内容失败");
                }
            }
        });
        // 导入全文按钮：用户输入 URL 后，通过网络获取全文数据并更新 BookContent
        importFullTextButton.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(BookFrame.this, "请选择要导入全文的书籍", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int bookId = (int) bookTable.getValueAt(row, 0);
            String url = JOptionPane.showInputDialog(BookFrame.this, "请输入全文数据的网络 URL：");
            if (url != null && !url.trim().isEmpty()) {
                String fullText = bookService.fetchFullTextFromURL(url.trim());
                if (fullText != null && !fullText.isEmpty()) {
                    // 获取现有的 BookContent，如果没有则创建一个新的对象
                    BookContent content = bookService.getBookContentByBookId(bookId);
                    if (content == null) {
                        content = new BookContent(bookId, "", fullText);
                        bookService.addBookContent(content);
                    } else {
                        content.setFullText(fullText);
                        bookService.updateBookContent(content);
                    }
                    messageLabel.setText("导入全文成功");
                } else {
                    messageLabel.setText("导入全文失败或返回空内容");
                }
            }
        });
        // 查看书评按钮：展示书籍所有书评
        viewReviewsButton.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(BookFrame.this, "请选择要查看书评的书籍", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int bookId = (int) bookTable.getValueAt(row, 0);
            List<BookReview> reviews = bookService.getBookReviewsByBookId(bookId);
            if (reviews == null || reviews.isEmpty()) {
                JOptionPane.showMessageDialog(BookFrame.this, "该书尚无书评", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (BookReview review : reviews) {
                sb.append("用户: ").append(review.getUserId()).append("\n");
                sb.append("评分: ").append(review.getRating()).append("\n");
                sb.append("评论: ").append(review.getReviewText()).append("\n");
                sb.append("时间: ").append(review.getReviewDate()).append("\n");
                sb.append("------------------------------\n");
            }
            JTextArea reviewArea = new JTextArea(sb.toString(), 15, 30);
            reviewArea.setEditable(false);
            JOptionPane.showMessageDialog(BookFrame.this, new JScrollPane(reviewArea), "书评列表",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        // “分享读书心得”按钮监听器（学生使用）
        shareThoughtsButton.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(BookFrame.this, "请选择要分享心得的书籍", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int bookId = (int) bookTable.getValueAt(row, 0);
            String comment = JOptionPane.showInputDialog(BookFrame.this, "请输入您的书评/心得：");
            if (comment != null && !comment.trim().isEmpty()) {
                // 从 GlobalState 获取真实的用户ID
                BookReview review = new BookReview(bookId, globalState.getUsername(), comment, 0, new java.util.Date());
                if (bookService.addBookReview(review)) {
                    messageLabel.setText("书评提交成功");
                } else {
                    messageLabel.setText("书评提交失败");
                }
            }
        });
        // “推荐图书”按钮监听器（导师使用）
        recommendBookButton.addActionListener(e -> {
            JTextField isbnField = new JTextField();
            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JTextField publisherField = new JTextField();
            JTextField yearField = new JTextField();
            Object[] fields = {
                    "ISBN:", isbnField,
                    "书名:", titleField,
                    "作者:", authorField,
                    "出版社:", publisherField,
                    "出版年份:", yearField
            };
            int option = JOptionPane.showConfirmDialog(BookFrame.this, fields, "推荐新书", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String isbn = isbnField.getText().trim();
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String publisher = publisherField.getText().trim();
                int year;
                try {
                    year = Integer.parseInt(yearField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BookFrame.this, "出版年份必须为整数", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(BookFrame.this, "ISBN、书名和作者不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Book book = new Book(isbn, title, author, publisher, year);
                if (bookService.addBook(book)) {
                    messageLabel.setText("图书推荐成功");
                    loadTableData();
                } else {
                    messageLabel.setText("图书推荐失败");
                }
            }
        });
        // “点评书评”按钮监听器（导师使用）
        commentOnReviewsButton.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(BookFrame.this, "请选择要点评的书籍", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int bookId = (int) bookTable.getValueAt(row, 0);
            // 获取该书的所有学生书评
            List<BookReview> studentReviews = bookService.getBookReviewsByBookId(bookId);
            if (studentReviews == null || studentReviews.isEmpty()) {
                JOptionPane.showMessageDialog(BookFrame.this, "该书暂无学生书评", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // 构建下拉选项（因为 BookReview 没有 getId() 方法，使用序号显示）
            String[] options = new String[studentReviews.size()];
            for (int i = 0; i < studentReviews.size(); i++) {
                BookReview br = studentReviews.get(i);
                String text = br.getReviewText();
                String truncated = text.length() > 20 ? text.substring(0, 20) + "..." : text;
                options[i] = "序号:" + (i + 1) + " | 用户:" + br.getUserId() + " | " + truncated;
            }
            // 提示导师选择一条书评进行点评
            String selectedOption = (String) JOptionPane.showInputDialog(
                    BookFrame.this,
                    "请选择要点评的学生书评：",
                    "选择书评",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (selectedOption == null) {
                return; // 用户取消选择
            }
            // 确定选择的下标
            int selectedIndex = -1;
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(selectedOption)) {
                    selectedIndex = i;
                    break;
                }
            }
            if (selectedIndex == -1) return;
            // 提示导师输入点评内容
            String tutorComment = JOptionPane.showInputDialog(BookFrame.this, "请输入您对该书评的点评：");
            if (tutorComment != null && !tutorComment.trim().isEmpty()) {
                // 生成点评文本，明确指出点评的是哪一条书评（使用序号）
                String combinedComment = "【点评对象: 书评序号:" + (selectedIndex + 1) + "】 导师点评：" + tutorComment;
                // 创建新的书评记录，标识当前导师为点评者，bookId 保持不变，评分暂设为 0，时间取当前时间
                BookReview tutorReview = new BookReview(bookId, globalState.getUsername(), combinedComment, 0, new Date());
                // 调用现有的 addBookReview 方法将点评记录保存
                if (bookService.addBookReview(tutorReview)) {
                    messageLabel.setText("点评提交成功");
                } else {
                    messageLabel.setText("点评提交失败");
                }
            }
        });
    }
    // 根据 GlobalState 更新各按钮显示状态
    private void updateRoleBasedButtons() {
        if (!globalState.isAuthenticated()) {
            // 用户未登录，隐藏所有功能按钮，只显示公共按钮
            addButton.setVisible(false);
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
            editContentButton.setVisible(false);
            importFullTextButton.setVisible(false);
            refreshButton.setVisible(true);
            viewReviewsButton.setVisible(true);
            shareThoughtsButton.setVisible(false);
            recommendBookButton.setVisible(false);
            commentOnReviewsButton.setVisible(false);
            messageLabel.setText("请先登录");
        } else {
            String role = globalState.getRole().toString();
            if (role.equalsIgnoreCase("ADMINISTRATOR")) {
                // 管理员显示全部功能按钮
                addButton.setVisible(true);
                updateButton.setVisible(true);
                deleteButton.setVisible(true);
                editContentButton.setVisible(true);
                importFullTextButton.setVisible(true);
                refreshButton.setVisible(true);
                viewReviewsButton.setVisible(true);
                shareThoughtsButton.setVisible(false);
                recommendBookButton.setVisible(false);
                commentOnReviewsButton.setVisible(false);
                messageLabel.setText("管理员模式");
            } else if (role.equalsIgnoreCase("TUTOR")) {
                // 导师：显示公共按钮以及导师专用按钮
                addButton.setVisible(false);
                updateButton.setVisible(false);
                deleteButton.setVisible(false);
                editContentButton.setVisible(false);
                importFullTextButton.setVisible(false);
                refreshButton.setVisible(true);
                viewReviewsButton.setVisible(true);
                recommendBookButton.setVisible(true);
                commentOnReviewsButton.setVisible(true);
                shareThoughtsButton.setVisible(true);
                messageLabel.setText("导师模式");
            } else if (role.equalsIgnoreCase("STUDENT")) {
                // 学生：显示公共按钮以及学生专用按钮
                addButton.setVisible(false);
                updateButton.setVisible(false);
                deleteButton.setVisible(false);
                editContentButton.setVisible(false);
                importFullTextButton.setVisible(false);
                refreshButton.setVisible(true);
                viewReviewsButton.setVisible(true);
                shareThoughtsButton.setVisible(true);
                recommendBookButton.setVisible(false);
                commentOnReviewsButton.setVisible(false);
                messageLabel.setText("学生模式");
            } else {
                // 如果角色未知，则只显示基本按钮
                addButton.setVisible(false);
                updateButton.setVisible(false);
                deleteButton.setVisible(false);
                editContentButton.setVisible(false);
                importFullTextButton.setVisible(false);
                refreshButton.setVisible(true);
                viewReviewsButton.setVisible(true);
                shareThoughtsButton.setVisible(false);
                recommendBookButton.setVisible(false);
                commentOnReviewsButton.setVisible(false);
                messageLabel.setText("角色未知");
            }
        }
    }
    // 重写 setVisible 方法，在每次窗口显示前调用 initializeComponents() 方法
    @Override
    public void setVisible(boolean b) {
        if (b) {
            updateRoleBasedButtons();
        }
        super.setVisible(b);
    }
}