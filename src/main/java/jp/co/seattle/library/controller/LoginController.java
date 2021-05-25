package jp.co.seattle.library.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.dto.FortuneInfo;
import jp.co.seattle.library.dto.UserInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.UsersService;

/**
 * ログインコントローラー
 */
@Controller /** APIの入り口 */
public class LoginController {
    final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private BooksService booksService;
    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String first(Model model) {
        return "login"; //jspファイル名
    }

    /**
     * ログイン処理
     *
     * @param email メールアドレス
     * @param password パスワード
     * @param model
     * @return　ホーム画面に遷移
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {

        // TODO 下記のコメントアウトを外してサービスクラスを使用してください。
        UserInfo selectedUserInfo = usersService.selectUserInfo(email, password);

        // TODO パスワードとメールアドレスの組み合わせ存在チェック実装
        if (selectedUserInfo == null) {
            model.addAttribute("error_noAccount", "アカウントが存在しません。");
            return "login";
        }

        //userIdに紐づく「今日の運勢」情報をfortuneテーブルから取得
        FortuneInfo fortuneInfo = usersService.getFortune(selectedUserInfo.getUserId());
        //取得した日付が現在の日付と同じかチェック
        Date dateObj = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setLenient(false);
        //現在日YをYYYMMDDに変換
        String today = df.format(dateObj);
        //最終ログイン日と現在日が異なる場合、if文の処理を行う。
        if (!today.equals(fortuneInfo.getLastLoginDay())) {
            //運勢をアップデートする
            usersService.updateFortune(selectedUserInfo.getUserId(), today);
            //最新の運勢情報を取得
            model.addAttribute("fortuneInfo", usersService.getFortune(selectedUserInfo.getUserId()));
        } else {
            model.addAttribute("fortuneInfo", fortuneInfo);
        }
        // 本の情報を取得して画面側に渡す
        model.addAttribute("userId", selectedUserInfo.getUserId());
        model.addAttribute("bookList", booksService.getBookList());
        return "home";

    }
}