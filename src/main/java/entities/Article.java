package entities;

import java.time.LocalDateTime;

public class Article {

    private int id;
    private String title, content, image;
    private LocalDateTime created_at;
    private int totalReactions;
    private int nbComments;
    private int nbShares;

    public int getNbShares() {
        return nbShares;
    }

    public void setNbShares(int nbShares) {
        this.nbShares = nbShares;
    }

    public Article() {

    }

    public int getTotalReactions() {
        return totalReactions;
    }

    public void setTotalReactions(int totalReactions) {
        this.totalReactions = totalReactions;
    }

    public int getNbComments() {
        return nbComments;
    }

    public void setNbComments(int nbComments) {
        this.nbComments = nbComments;
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
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", created_at=" + created_at +
                '}';
    }


}


