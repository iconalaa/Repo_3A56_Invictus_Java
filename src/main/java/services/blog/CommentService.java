package services.blog;

import controllers.user.SessionManager;
import entities.Article;
import entities.Comment;
import entities.User;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommentService {

    private final Connection connection;
    private int iduser= SessionManager.getLoggedInUser().getUser_id();

    public CommentService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    public void addComment(Comment comment, User user, Article article) throws SQLException {
        String sql = "INSERT INTO comment (article_id, id_user_id, content, created_at) VALUES (?, ?, ?, ?)";
        int usertest=iduser;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, article.getId());
            statement.setInt(2, usertest);
            statement.setString(3, comment.getContent());
            statement.setObject(4, comment.getCreated_at());
            statement.executeUpdate();
            System.out.println("Comment added successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to add comment: " + e.getMessage());
            throw e;
        }
    }

    public void deleteComment(int id) throws SQLException {
        String sql = "DELETE FROM comment WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Comment deleted successfully.");
            } else {
                System.out.println("Comment not found with ID: " + id);
            }
        }
    }

    public void updateComment(Comment comment, int id) throws SQLException {
        String sql = "UPDATE comment SET content = ?, created_at = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, comment.getContent());
            statement.setObject(2, comment.getCreated_at());
            statement.setInt(3, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Comment with ID " + id + " updated successfully.");
            } else {
                System.out.println("Comment with ID " + id + " not found, update failed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to update comment with ID " + id + ": " + e.getMessage());
            throw e;
        }
    }

    public List<Comment> getAllComments() throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.id, c.content, c.created_at, " +
                "u.id AS user_id, u.email AS user_email, u.roles AS user_roles, " +
                "u.name AS user_name, u.lastname AS user_lastname, u.date_birth AS user_birth_date, " +
                "u.gender AS user_gender, u.brochure_filename AS user_brochure_filename, " +
                "a.id AS article_id, a.title AS article_title, a.content AS article_content, " +
                "a.image AS article_image, a.created_at AS article_created_at " +
                "FROM comment c " +
                "INNER JOIN user u ON c.id_user_id = u.id " +
                "INNER JOIN article a ON c.article_id = a.id";
        try (PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("user_email"),
                        rs.getString("user_roles").split(","),
                        rs.getString("user_name"),
                        rs.getString("user_lastname"),
                        rs.getDate("user_birth_date").toLocalDate(),
                        rs.getString("user_gender"),
                        rs.getString("user_brochure_filename")
                );
                Article article = new Article(
                        rs.getInt("article_id"),
                        rs.getString("article_title"),
                        rs.getString("article_content"),
                        rs.getString("article_image"),
                        rs.getTimestamp("article_created_at").toLocalDateTime()
                );
                Comment comment = new Comment(
                        rs.getInt("id"),
                        user,
                        article,
                        rs.getString("content"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                comments.add(comment);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommentService.class.getName()).log(Level.SEVERE, "Error while retrieving comments", ex);
        }
        return comments;
    }

    public List<Comment> getCommentsByArticleId(int articleId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.id, c.content, c.created_at, " +
                "u.id AS user_id, u.email AS user_email, u.roles AS user_roles, " +
                "u.name AS user_name, u.lastname AS user_lastname, u.date_birth AS user_birth_date, " +
                "u.gender AS user_gender, u.brochure_filename AS user_brochure_filename " +
                "FROM comment c " +
                "INNER JOIN user u ON c.id_user_id = u.id " +
                "WHERE c.article_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, articleId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("user_email"),
                        rs.getString("user_roles").split(","),
                        rs.getString("user_name"),
                        rs.getString("user_lastname"),
                        rs.getDate("user_birth_date").toLocalDate(),
                        rs.getString("user_gender"),
                        rs.getString("user_brochure_filename")
                );
                Comment comment = new Comment(
                        rs.getInt("id"),
                        user,
                        null, // Article information is not needed here
                        rs.getString("content"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                comments.add(comment);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommentService.class.getName()).log(Level.SEVERE, "Error while retrieving comments by article ID", ex);
        }
        return comments;
    }
}