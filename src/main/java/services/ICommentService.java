package services;

import entities.Article;
import entities.Comment;
import entities.User;

import java.util.List;

public interface ICommentService {
    void addComment(Comment comment, User user, Article article);
    void deleteComment(int id);
    void updateComment(Comment comment);
    List<Comment> readAllComments();
    List<Comment> readCommentsByArticleId(int id);
}

