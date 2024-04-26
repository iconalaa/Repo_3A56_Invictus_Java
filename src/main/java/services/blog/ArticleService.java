package services.blog;



import entities.Article;
import services.IArticleService;
import utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticleService implements IArticleService<Article> {

    private final Connection connection;

    public ArticleService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void addArticle(Article article) {
        String query = "INSERT INTO article (title, content, image, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, article.getTitle());
            pst.setString(2, article.getContent());
            pst.setString(3, article.getImage());
            pst.setObject(4,article.getCreated_at());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ArticleService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteArticle(int id) {
        String query = "DELETE FROM article WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ArticleService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateArticle(Article article) {
        String query = "UPDATE article SET title = ?, content = ?, image = ?, created_at = ? WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, article.getTitle());
            pst.setString(2, article.getContent());
            pst.setString(3, article.getImage());
            pst.setObject(4, article.getCreated_at());
            pst.setInt(5, article.getId());
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ArticleService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Article> readAllArticles() {
        List<Article> list = new ArrayList<>();
        String query = "SELECT * FROM article";
        try (Statement st = connection.createStatement()) {
            try (ResultSet rs = st.executeQuery(query)) {
                while (rs.next()) {

                    Article article = new Article(rs.getInt("id"), rs.getString("title"), rs.getString("content"),
                            rs.getString("image"), rs.getTimestamp("created_at").toLocalDateTime());
                    list.add(article);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ArticleService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public Article readById(int id) {
        String query = "SELECT * FROM article WHERE id = ?";
        Article article = null;
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                LocalDate created_at = null;
                article = new Article( rs.getInt("id"),rs.getString("title"),rs.getString("content"),
                        rs.getString("image"),rs.getTimestamp("created_at").toLocalDateTime() );
            }
        } catch (SQLException ex) {
            Logger.getLogger(ArticleService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return article;
    }
}


