package entities;

import java.time.LocalDateTime;

public class Article {

    private int id, likes;
    private String title, content, image;
    private LocalDateTime created_at;

    public Article(String title, String content, String imagePath, int likes, LocalDateTime createdAt) {
        this.title=title;
        this.content=content;
        this.image=imagePath;
        this.likes=likes;
        this.created_at=createdAt;

    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Article(int id, String title, String content, String image, LocalDateTime created_at) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.created_at = created_at;
    }
    public Article(String title, String content, String image, LocalDateTime created_at) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.created_at = created_at;
    }
    public Article(int id, int likes, String title, String content, String image, LocalDateTime created_at) {
        this.id = id;
        this.likes = likes;
        this.title = title;
        this.content = content;
        this.image = image;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", likes=" + likes +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
