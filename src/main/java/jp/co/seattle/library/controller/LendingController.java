package jp.co.seattle.library.controller;

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

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.LendingService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class LendingController {
    final static Logger logger = LoggerFactory.getLogger(LendingController.class);

        @Autowired
        private BooksService booksService;
        @Autowired
        private LendingService lendingService;

        /**
         * 対象書籍を貸し出す
         *
         * @param locale ロケール情報
         * @param bookId 書籍ID
         * @param model モデル情報
         * @return 遷移先画面名
         */
        @Transactional
        @RequestMapping(value = "/lendBook", method = RequestMethod.POST) //value＝actionで指定したパラメータ
        //RequestParamでname属性を取得
        public String lendBook(Model model,
                @RequestParam("bookId") int bookId,
                Locale locale) {
            logger.info("Welcome lending! The client locale is {}.", locale);

            lendingService.lendBook(bookId);
            
            int count = lendingService.countBookId(bookId);
            
            if(count == 0) {
                model.addAttribute("returnDisabled","disabled");
                model.addAttribute("lendingStatus", "貸出可");
            }else {
                model.addAttribute("lendDisabled","disabled");
                model.addAttribute("lendingStatus", "貸出不可");
            }
            model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
            return "details";
        }

        /**
         * 対象書籍を返却する
         *
         * @param locale ロケール情報
         * @param bookId 書籍ID
         * @param model モデル情報
         * @return 遷移先画面名
         */
        @Transactional
        @RequestMapping(value = "/returnBook", method = RequestMethod.POST)
        public String returnBook(Model model,
                @RequestParam("bookId") int bookId,
                Locale locale) {
            logger.info("Welcome return! The client locale is {}.", locale);

            lendingService.returnBook(bookId);

            int count = lendingService.countBookId(bookId);

            if (count == 0) {
                model.addAttribute("returnDisabled", "disabled");
                model.addAttribute("lendingStatus", "貸出可");
            } else {
                model.addAttribute("lendDisabled", "disabled");
                model.addAttribute("lendingStatus", "貸出不可");
            }
            model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
            return "details";
        }

    
}
