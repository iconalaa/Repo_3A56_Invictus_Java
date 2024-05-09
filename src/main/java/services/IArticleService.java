package services;

import java.util.List;

public interface IArticleService<A> {




    void addArticle(A a);
    void deleteArticle(int id);
    void updateArticle(A a);
    List<A>readAllArticles();
    A readById(int id);
}

