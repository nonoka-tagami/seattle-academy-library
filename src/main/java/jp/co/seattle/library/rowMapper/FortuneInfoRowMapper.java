package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.FortuneInfo;

@Configuration
public class FortuneInfoRowMapper implements RowMapper<FortuneInfo> {
    @Override
    public FortuneInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Query結果（ResultSet rs）を、オブジェクトに格納する実装
        FortuneInfo fortuneInfo = new FortuneInfo();

        // bookInfoの項目と、取得した結果(rs)のカラムをマッピングする
        fortuneInfo.setFortuneId(rs.getInt("id"));
        fortuneInfo.setColor(rs.getString("color"));
        fortuneInfo.setItem(rs.getString("item"));
        fortuneInfo.setAction(rs.getString("action"));
        fortuneInfo.setSituation(rs.getString("situation"));
        fortuneInfo.setFortune(rs.getString("fortune"));
        fortuneInfo.setLastLoginDay(rs.getString("last_login_day"));
        return fortuneInfo;
    }
}
