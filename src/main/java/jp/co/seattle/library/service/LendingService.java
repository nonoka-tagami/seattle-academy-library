package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Handles requests for the application home page.
 */
@Service
public class LendingService {
    final static Logger logger = LoggerFactory.getLogger(LendingService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍の貸出しステータスを変更する
     *
     * @param bookId 
     */
    public void lendBook(int bookId) {
        String str = "INSERT INTO lendingBook(booksId,reg_date,upd_date) VALUES(" + bookId + ",sysdate(),sysdate());";
        jdbcTemplate.update(str);
    }

    public int countBookId(int bookId) {
        String sql = "SELECT count(*) FROM lendingBook WHERE booksId=" + bookId + ";";
        int count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count;
    }

    public void returnBook(int bookId) {
        String sql = "delete from lendingBook where booksId=" + bookId + ";";
        jdbcTemplate.update(sql);
    }
}
