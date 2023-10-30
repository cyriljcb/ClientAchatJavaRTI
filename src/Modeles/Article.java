package Modeles;

public class Article {
    private String intitule;
    private float prix;
    private int stock;
    private String image;

    public Article() {
        intitule = "";
        prix = 0.0f;
        stock = 0;
        image = "";
    }
    public Article(String intitule, float prix,int stock,String image)
    {
        this.intitule = intitule;
        this.prix = prix;
        this.stock = stock;
        this.image=image;
    }
    public Article(String intitule, float prix,int stock)
    {
        this.intitule = intitule;
        this.prix = prix;
        this.stock = stock;
    }
    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

