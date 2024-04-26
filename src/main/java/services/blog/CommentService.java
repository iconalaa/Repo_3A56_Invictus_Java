package services.blog;

import entities.Comment;
import services.ICommentService;
import utils.MyDataBase;

;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommentService implements ICommentService {

    private final Connection connection;

    public CommentService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void addComment(Comment comment) {
        String query = "INSERT INTO comment (article_id, content, created_at) " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, comment.getArticle_id());
            pst.setString(2, comment.getContent());
            pst.setObject(3, comment.getCreated_at());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CommentService.class.getName()).log(Level.SEVERE, "Error while adding comment", ex);
        }
    }

    @Override
    public void deleteComment(int id) {
        String query = "DELETE FROM comment WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CommentService.class.getName()).log(Level.SEVERE, "Error while deleting comment", ex);
        }
    }

    @Override
    public void updateComment(Comment comment) {
        String query = "UPDATE comment SET content = ?, created_at = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, comment.getContent());
            pst.setObject(2, comment.getCreated_at());
            pst.setInt(3, comment.getId());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CommentService.class.getName()).log(Level.SEVERE, "Error while updating comment", ex);
        }
    }

    @Override
    public List<Comment> readAllComments() {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT c.id, c.article_id, c.id_user_id, c.content, c.created_at " +
                "FROM comment c " +
                "INNER JOIN article a ON c.article_id = a.id";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Comment comment = new Comment(
                        rs.getInt("id"),
                        rs.getInt("article_id"),
                        rs.getInt("id_user_id"),
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

    @Override
    public List<Comment> readCommentsByArticleId(int id) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT c.id, c.article_id, c.id_user_id, c.content, c.created_at " +
                "FROM comment c " +
                "INNER JOIN article a ON c.article_id = a.id " +
                "WHERE c.article_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("created_at");
                    LocalDateTime createdAt = null;
                    if (timestamp != null) {
                        createdAt = timestamp.toLocalDateTime();
                    }
                    Comment comment = new Comment(
                            rs.getInt("id"),
                            rs.getInt("article_id"),
                            rs.getInt("id_user_id"),
                            rs.getString("content"),
                            createdAt
                    );
                    comments.add(comment);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommentService.class.getName()).log(Level.SEVERE, "Error while retrieving comments by article ID", ex);
        }
        return comments;
    }

}
