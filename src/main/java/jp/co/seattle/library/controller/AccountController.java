package jp.co.seattle.library.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import jp.co.seattle.library.dto.FortuneInfo;
import jp.co.seattle.library.dto.UserInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.UsersService;

/**
 * アカウント作成コントローラー
 */
@Controller //APIの入り口
public class AccountController {
    final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private BooksService booksService;
    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/newAccount", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    public String createAccount(Model model) {
        return "createAccount";
    }

    /**
     * 新規アカウント作成
     *
     * @param email メールアドレス
     * @param password パスワード
     * @param passwordForCheck 確認用パスワード
     * @param model
     * @return　ホーム画面に遷移
     */
    @Transactional
    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public String createAccount(Locale locale,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("passwordForCheck") String passwordForCheck,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome createAccount! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);

        // TODO バリデーションチェック、パスワード一致チェック実装
        boolean isValidEmail = email.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+$");
        boolean isValidPW = password.matches("^[0-9a-zA-Z]+$");
        boolean isValidPwCheck = passwordForCheck.matches("^[0-9a-zA-Z]+$");

        if (!isValidEmail || !isValidPW || !isValidPwCheck) {
            model.addAttribute("errorMessage", "半角英数字を入力してください。");
            return "createAccount";
        }

        if (!password.equals(passwordForCheck)) {
            model.addAttribute("errorMessagePass", "パスワードが一致していません。");
            return "createAccount";
        }

        userInfo.setPassword(password);
        usersService.registUser(userInfo);

        //新規アカウントに自動採番されたuserIdを取得
        UserInfo selectedUserInfo = usersService.selectUserInfo(email, password);
        //現在日取得
        Date dateObj = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String today = df.format(dateObj);
        //「今日の運勢」生成
        usersService.creatFortune(selectedUserInfo.getUserId(), today);
        //「今日の運勢 取得
        FortuneInfo fortuneInfo = usersService.getFortune(selectedUserInfo.getUserId());

        model.addAttribute("fortuneInfo", fortuneInfo);
        model.addAttribute("bookList", booksService.getBookList());
        return "home";
    }

}
