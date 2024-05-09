package entities;


import java.time.LocalDateTime;

public class Comment {

    private int id;
    private User user;
    private Article article;
    private String content;
    private LocalDateTime created_at;

    public Comment(int id, User user, Article article, String content, LocalDateTime created_at) {
        this.id = id;
        this.user = user;
        this.article = article;
        this.content = content;
        this.created_at = created_at;
    }

    public Comment(User user, Article article, String content, LocalDateTime created_at) {
        this.user = user;
        this.article = article;
        this.content = content;
        this.created_at = created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }


    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", article=" + article +
                ", content='" + content + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
