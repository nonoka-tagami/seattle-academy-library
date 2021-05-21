package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.UserInfo;

@Configuration
public class UserCountRowMapper implements RowMapper<UserInfo> {

    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Query結果（ResultSet rs）を、オブジェクトに格納する実装
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(rs.getInt("id"));
        userInfo.setEmail(rs.getString("email"));
        userInfo.setPassword(rs.getString("password"));
        return userInfo;
    }

}