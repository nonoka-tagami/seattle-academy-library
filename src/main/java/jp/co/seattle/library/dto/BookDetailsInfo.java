package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍詳細情報格納DTO
 *
 */
@Configuration
@Data
public class BookDetailsInfo {

    private int bookId;

    private String title;
    
    private String description;

    private String author;

    private String publisher;

    private String publishDate;

    private String thumbnailUrl;
    
    private String isbn;

    private String thumbnailName;


    public BookDetailsInfo() {

    }

    public BookDetailsInfo(int bookId, String title,String description, String author, String publisher,
            String publish_date, String thumbnailUrl, String isbn, String thumbnailName) {
        this.bookId = bookId;
        this.title = title;
        this.description = description;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publish_date;
        this.thumbnailUrl = thumbnailUrl;
        this.isbn = isbn;
        this.thumbnailName = thumbnailName;
    }

}