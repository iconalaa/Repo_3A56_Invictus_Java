package services;

import entities.Comment;

import java.util.List;

public interface ICommentService {
    void addComment(Comment comment);
    void deleteComment(int id);
    void updateComment(Comment comment);
    List<Comment> readAllComments();
    List<Comment> readCommentsByArticleId(int id);
}

