package Modeles;

import java.util.ArrayList;
import java.util.List;

public class PanierModel {
    private List<Article> articles;
    private float totalCaddie;
    private int numArticle;

    public PanierModel() {
        articles = new ArrayList<>();
        totalCaddie = 0.0f;
        numArticle = 0;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void addArticle(Article article) {
        articles.add(article);
    }

    public void removeArticle(Article article) {
        articles.remove(article);
    }
    public float getTotalCaddie() {
        return totalCaddie;
    }

    public void setTotalCaddie(float totalCaddie) {
        this.totalCaddie = totalCaddie;
    }

    public int getNumArticle() {
        return numArticle;
    }
    public void addNumArticle(boolean add)
    {
        if(add)
            numArticle++;
        else
            numArticle--;
    }
    public void setNumArticle(int numArticle) {
        this.numArticle = numArticle;
    }
    public void viderPanier() {
        articles.clear();
        totalCaddie = 0.0f;
    }
}



