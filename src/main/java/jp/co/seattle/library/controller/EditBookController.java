package jp.co.seattle.library.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

@Controller //APIの入り口
public class EditBookController {
    final static Logger logger = LoggerFactory.getLogger(EditBookController.class);

    @Autowired
    private BooksService booksService;
    @Autowired
    private ThumbnailService thumbnailService;

    @Transactional
    @RequestMapping(value = "/editBook", method = RequestMethod.POST)
    public String editBookId(
            Locale locale,
            @RequestParam("bookId") int bookId,
            Model model) { 
            BookDetailsInfo bookDetailsInfo = booksService.getBookInfo(bookId);
            model.addAttribute("bookDetailsInfo", bookDetailsInfo);
        return "editBook";
    }

    @Transactional
    @RequestMapping(value = "/updateBook", method = RequestMethod.POST)
    public String editBook(
            Locale locale,
            @RequestParam("bookId") int bookId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("publish_date") String publishDate,
            @RequestParam("thumbnail") MultipartFile file,
            @RequestParam("isbn") String isbn,
            Model model) {
        logger.info("Welcome edit! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setBookId(bookId);
        bookInfo.setTitle(title);
        bookInfo.setDescription(description);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setIsbn(isbn);

        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            df.setLenient(false);
            df.parse(publishDate);
        } catch (ParseException p) {
            model.addAttribute("errorEditMessage", "ISBNの桁数または半角英数字が正しくありません。出版日は半角英数字YYYYMMDD形式で入力してください。");
            model.addAttribute("bookDetailsInfo", bookInfo);
            return "editBook";
        }
        boolean isValidIsbn = isbn.matches("[0-9]{10}?$||[0-9]{13}?$");
        if (!isValidIsbn) {
            model.addAttribute("errorEditMessage", "ISBNの桁数または半角英数字が正しくありません。出版日は半角英数字YYYYMMDD形式で入力してください。");
            model.addAttribute("bookDetailsInfo", bookInfo);
            return "editBook";
        }

        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "addBook";
            }
        }

        booksService.editBook(bookInfo);

        BookDetailsInfo bookDetailsInfo = booksService.getBookInfo(bookId);

        model.addAttribute("resultMessage", "編集完了");
        model.addAttribute("bookDetailsInfo", bookDetailsInfo);
        return "details";
    }
}
