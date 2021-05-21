package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class FortuneInfo {
    private int fortuneId;

    private String color;

    private String item;

    private String action;

    private String situation;

    private String fortune;

    private String lastLoginDay;

    public FortuneInfo() {

    }

    // コンストラクタ
    public FortuneInfo(int fortuneId, String color, String item, String action, String situation, String fortune,
            String lastLoginDay) {
        this.fortuneId = fortuneId;
        this.color = color;
        this.item = item;
        this.action = action;
        this.situation = situation;
        this.fortune = fortune;
        this.lastLoginDay = lastLoginDay;
    }
}
