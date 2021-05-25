package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import jp.co.seattle.library.dto.FortuneInfo;
import jp.co.seattle.library.dto.UserInfo;
import jp.co.seattle.library.rowMapper.FortuneInfoRowMapper;
import jp.co.seattle.library.rowMapper.UserCountRowMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
//APIの入り口 APIとは、他のソフトウェアが外部から自分のソフトウェアへアクセスし利用できるようにしたもの
//ソフトウェアコンポーネントが互いにやりとりするのに使用するインタフェースの仕様
public class UsersService {
    final static Logger logger = LoggerFactory.getLogger(UsersService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * ユーザー情報を登録する
     * @param userInfo ユーザー情報
     */
    public void registUser(UserInfo userInfo) {

        // SQL生成
        String sql = "INSERT INTO users (email, password,reg_date,upd_date) VALUES ('"
                + userInfo.getEmail()
                + "','"
                + userInfo.getPassword()
                + "',sysdate(),sysdate()" + ")";

        jdbcTemplate.update(sql);
    }

    /**
     * ユーザー情報取得
     * @param email メールアドレス
     * @param password パスワード
     * @return ユーザー情報
     */

    public UserInfo selectUserInfo(String email, String password) {
        // TODO SQL生成

        String sql = "SELECT id,email,password FROM users WHERE email ='" + email + "'AND password ='" + password + "'";
        try {
            UserInfo selectedUserInfo = jdbcTemplate.queryForObject(sql, new UserCountRowMapper());
            return selectedUserInfo;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * useIdに紐づく同日付の「今日の運勢」情報をfortuneテーブルから取得する。
     *@param fortuneInfo 
     *@return 運勢情報
     */
    public FortuneInfo getFortune(int userId) {
        String sql = "select id,"
                + "(select ob.object from fortune as f join objects as ob on f.color = ob.o_id where f.userid="
                + userId + " )as color,"
                + "( select ob.object from fortune as f join objects as ob on f.item = ob.o_id where f.userid="
                + userId + ")as item,"
                + "( select ob.object from fortune as f join objects as ob on f.action = ob.o_id where f.userid="
                + userId + " )as action,"
                + "( select ob.object from fortune as f join objects as ob on f.situation = ob.o_id where f.userid="
                + userId + " )as situation,"
                + "( select ob.object from fortune as f join objects as ob on f.fortune = ob.o_id where f.userid="
                + userId + " )as fortune,"
                + "last_login_day from fortune as f where userid=" + userId + ";";

        FortuneInfo fortuneInfo = jdbcTemplate.queryForObject(sql, new FortuneInfoRowMapper());
        return fortuneInfo;
    }

    /**
     *「今日の運勢」情報を作成
     *@param userId
     *@return 運勢情報
     */
    public void creatFortune(int userId, String lastLoginDay) {
        String sql = "insert into fortune(color,item,action,situation,fortune,userid,last_login_day)"
                + "values((select o_id from objects where category = 1 order by rand() limit 1),"
                + "(select o_id from objects where category = 2 order by rand() limit 1),"
                + "(select o_id from objects where category = 3 order by rand() limit 1),"
                + "(select o_id from objects where category = 4 order by rand() limit 1),"
                + "(select o_id from objects where category = 5 order by rand() limit 1),"
                + userId + ",'" + lastLoginDay + "');";

        jdbcTemplate.update(sql);
    }

    /**
     * useIdに紐づく「今日の運勢」情報をアップデートする。
     *@param fortuneInfo 
     */
    public void updateFortune(int userId, String today) {
        String sql = "UPDATE  fortune SET color=(select o_id from objects where category =1 order by rand() limit 1),"
                + "    item=(select o_id from objects where category =2 order by rand() limit 1),"
                + "    action=(select o_id from objects where category =3 order by rand() limit 1),"
                + "    situation=(select o_id from objects where category =4 order by rand() limit 1),"
                + "    fortune=(select o_id from objects where category =5 order by rand() limit 1),"
                + "    last_login_day=" + today + " WHERE userid=" + userId + ";";
        jdbcTemplate.update(sql);
    }

}
