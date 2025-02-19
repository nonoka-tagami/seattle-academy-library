package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス

 * 
 *  booksテーブルに関する処理を実装する
 
 *
 */
/**
 * @author user
 *
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "select title,author,publisher,publish_date,id,thumbnail_url from books order by title asc",

                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT * FROM books where id ="
                + bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */

    public void registBook(BookDetailsInfo bookInfo) {

        String sql = "INSERT INTO books (title,description, author,publisher,publish_date,thumbnail_name,thumbnail_url,isbn,reg_date,upd_date) VALUES ('"
                + bookInfo.getTitle() + "','" + bookInfo.getDescription() + "','" + bookInfo.getAuthor() + "','"
                + bookInfo.getPublisher() + "','"
                + bookInfo.getPublishDate() + "','"
                + bookInfo.getThumbnailName() + "','"
                + bookInfo.getThumbnailUrl() + "','"
                + bookInfo.getIsbn() + "',"
                + "sysdate(),"
                + "sysdate());";

        jdbcTemplate.update(sql);
    }

    /**
     * 書籍を削除する
     *
     * @param bookId 
     */
    public void deleteBook(int bookid) {
        String sql = "delete from books where id=" + bookid;
        jdbcTemplate.update(sql);

    }

    /**
     * 書籍情報を変更する
     *
     * @param bookId 
     */
    public int latestBookId() {
        String sql = "select max(id) from books;";
        int bookId = jdbcTemplate.update(sql, Integer.class);
        return bookId;
    }

    /**
     * 書籍情報編集する
     *
     * @param bookInfo 
     */
    public void editBook(BookDetailsInfo bookInfo) {
        String sql = "update books set title='" + bookInfo.getTitle()
                + "',description='" + bookInfo.getDescription() + "',author='" + bookInfo.getAuthor() + "',publisher='"
                + bookInfo.getPublisher()
                + "',publish_date='" + bookInfo.getPublishDate() + "',thumbnail_url='" + bookInfo.getThumbnailUrl()
                + "',thumbnail_name ='" + bookInfo.getThumbnailName() + "',isbn='" + bookInfo.getIsbn()
                + "',UPD_DATE = sysdate() where id=" + bookInfo.getBookId() + ";";
        jdbcTemplate.update(sql);
    }
}
