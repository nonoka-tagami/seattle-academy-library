package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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


@Controller
public class AddBooksBulkController {

    final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    @RequestMapping(value = "/addBookBulk", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String bulk(Model model) {
        return "addBooksBulk";
    }

    /**
     * 書籍情報を一括登録する
     * @param locale ロケール情報
     * @param file csvファイル
     * @param model モデル
     * @return 遷移先画面
     */
    @Transactional
    @RequestMapping(value = "/bulkRegist", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String insertBook(Locale locale,
            @RequestParam("upload_csv") MultipartFile csvFile,
            Model model) {

        logger.info("Welcome bulk! The client locale is {}.", locale);

        List<String[]> lines = new ArrayList<String[]>();
        List<String> errorList = new ArrayList<String>();

        String line;
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), "UTF-8"));
            while ((line = buf.readLine()) != null) {
                String[] data;
                data = new String[6];
                int i = 0;

                for (String str : line.split(",")) {
                    data[i++] = str;
                }
                lines.add(data);

                String errorDateIsbn = lines.size() + "行目の出版日は半角数字のYYYYMMDD形式で入力してください。ISBNは１０桁か１３桁の半角数字で入力してください。";
                String errorDate = lines.size() + "行目の出版日は半角数字のYYYYMMDD形式で入力してください。";
                String errorIsbn = lines.size() + "行目のISBNは１０桁か１３桁の半角数字で入力してください。";
                

                try {
                    DateFormat df = new SimpleDateFormat("yyyyMMdd");
                    df.setLenient(false);
                    df.parse(data[3]);
                } catch (ParseException p) { 
                    boolean isValidIsbn1 = data[4].matches("[0-9]{10}|[0-9]{13}");
                    if (!isValidIsbn1) {
                        errorList.add(errorDateIsbn);
                    } else {
                        errorList.add(errorDate);
                    }
                      
                }
                boolean isValidIsbn2 = data[4].matches("[0-9]{10}|[0-9]{13}");
                if (!isValidIsbn2) {
                    errorList.add(errorIsbn);
                    continue;
                }

            }
            buf.close();
        } catch (IOException e) {
            model.addAttribute("fileError", "csvファイルを読み込めません。");
            return "addbookBulk";
        }

        if (errorList.size() != 0) {
            model.addAttribute("list", errorList);
            return "addBooksBulk";
        }
        
        for (int i = 0; i <= lines.size(); i++) {
            BookDetailsInfo bookInfo = new BookDetailsInfo();
            bookInfo.setTitle(lines.get(i)[0]);
            bookInfo.setAuthor(lines.get(i)[1]);
            bookInfo.setPublisher(lines.get(i)[2]);
            bookInfo.setPublishDate(lines.get(i)[3]);
            bookInfo.setIsbn(lines.get(i)[4]);
            bookInfo.setDescription(lines.get(i)[5]);

            booksService.registBook(bookInfo);
    }
        model.addAttribute("addBooksBulk", "登録完了");
        return "addBooksBulk";
    }

}
