package entities;


import java.time.LocalDateTime;

public class Comment {

    private int id;
    private final int article_id;
    private int id_user_id = 0;


    public Comment(int id, int article_id, int id_user_id, String content, LocalDateTime created_at) {
        this.id = id;
        this.article_id = article_id;
        this.id_user_id = id_user_id;
        this.content = content;
        this.created_at = created_at;
    }

    public void setId(int id) {
        this.id = id;
    }



    private final String content;
    private LocalDateTime created_at;

    public Comment(int article_id, int id_user_id, String content, LocalDateTime created_at) {
        this.id = 0; // Valeur par défaut
        this.id_user_id=id_user_id;
        this.article_id = article_id;
        this.content = content;
        this.created_at = created_at;
    }



    public Comment(int article_id, String content, LocalDateTime created_at) {
        this.article_id = article_id;
        this.content = content;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public int getArticle_id() {
        return article_id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Comment(int article_id, String content) {
        this.article_id = article_id;
        this.content = content;
    }

    public void setId_user_id(int id_user_id) {
        this.id_user_id = id_user_id;
    }

    public int getId_user_id() {
        return id_user_id;
    }
    @Override
    public String toString() {
        return "Comment{id=" + id +
                ", article_id=" + article_id +
                ", id_user_id=" + id_user_id +
                ", content='" + content + '\'' +
                ", created_at=" + created_at +
                '}';
    }


    public void setContent(String s) {
    }
}

