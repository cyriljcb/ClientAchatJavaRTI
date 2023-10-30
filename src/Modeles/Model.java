package Modeles;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Model {
    private PanierModel panier;
    private Socket sService;
    private LoginModel log;
    private List<ModelObserver> observers = new ArrayList<>();
    private Article art;
    private int islogged = 0;
    private String message;
    private int nbrArticle;
    public Model() {
        panier = new PanierModel();
        log = new LoginModel();
    }
    public PanierModel getPanier() {
        return panier;
    }


    public Article getArticleEnCours()
    {
        return art;
    }
    public int getIslogged()
    {
        return islogged;
    }
    public String getMessage()
    {
        return message;
    }
    public boolean login(Socket s, String nom, String mdp, int nouveauClient) {
        sService = s;
        boolean v = false;
        try {
            TCP.send(sService, ("LOGIN/" + nom + "/" + mdp + "/" + nouveauClient + "/#)").getBytes());
            String reponse;
            reponse = TCP.receive(sService);
            String[] str = reponse.split("/");
            String st = str[0];
            if (st.equals("LOGIN")) {
                if (str[1].equals("1")) {
                    islogged=1;
                    v = true;
                   message = str[2];

                    log.setIdClient(str[3]);
                    try {
                        TCP.send(sService, "CONSULT/1/\0#)".getBytes());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    String reponse1;
                    try {
                        reponse1 = TCP.receive(sService);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    String[] stri;
                    stri = reponse1.split("/");
                    if (stri[0].equals("CONSULT")) {
                        panier.addNumArticle(true);

                        art = new Article(stri[2],Float.parseFloat(stri[3].replace(",", ".")),Integer.parseInt(stri[4]),stri[5]);
                    }
                } else {
                   message ="probleme de lecture";
                    log.setLogged(false);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return v;
    }

    public void consult(boolean droite) {
        boolean v = false;
        if (droite) {
            if (panier.getNumArticle() < 21) {
                panier.addNumArticle(true);
                v = true;
            }
        } else {
            if (panier.getNumArticle() > 1) {
                panier.addNumArticle(false);
                v = true;
            }
        }
        if (v) {
            try {
                TCP.send(sService, ("CONSULT/" + panier.getNumArticle() + "/#)").getBytes());
                String reponse;
                reponse = TCP.receive(sService);
                String[] stri;
                stri = reponse.split("/");
                if (stri[0].equals("CONSULT")) {
                    art = new Article(stri[2],Float.parseFloat(stri[3].replace(",", ".")),Integer.parseInt(stri[4]),stri[5]);

                } else {
                    message = "erreur de chargement des produits";
                    log.setLogged(false);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public void acheter(int quant)
    {
        if(quant<=0)
        {
            message = "veuillez selectionner une quantité valide";
        }
        else {
            try {
                TCP.send(sService, ("ACHAT/"+panier.getNumArticle()+"/"+quant+"/#)").getBytes());
                String reponse;
                reponse=TCP.receive(sService);
                if( mettreAJourArticle(reponse)==1)
                {
                   message = "achat réussi";
                }
                else
                    message =  "erreur : achat impossible";
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    public int mettreAJourArticle(String c) {
        int a = 1;
        String[] stri, str;
        stri = c.split("\\$(?=\\d)");

        for (int i = 0; i < stri.length; i++) {
            stri[i] = stri[i].replace("$", ""); // Supprimer les signes dollar dans chaque partie
        }
        String[] subParts = stri[0].split("/");
        for (int j = 0; j < subParts.length; j++) {
            System.out.println("  subPart : " + subParts[j]);
        }
        if (subParts[0].equals("CADDIE")) {
            if (subParts.length > 1) {
                nbrArticle = Integer.parseInt(subParts[1]);
                panier.viderPanier();
                for (int i = 0; i < nbrArticle; i++) {
                    str = stri[i + 1].split("/");
                    Article article = new Article(str[2], Float.parseFloat(str[3].replace(",", ".")), Integer.parseInt(str[4]));
                    panier.addArticle(article);
                    panier.setTotalCaddie(panier.getTotalCaddie() + (article.getPrix() * article.getStock()));
                }
            }
        }
        else
            a = -1;
        return a;
    }
    public void supprimer(int ligneSelectionne)
    {
        if (ligneSelectionne != -1) {
            try {
                TCP.send(sService, ("CANCEL/" + ligneSelectionne + "/#)").getBytes());
                String reponse;
                reponse = TCP.receive(sService);
                if( mettreAJourArticle(reponse)==1)
                {
                    message= "supression réussie";
                }
                else
                    message=  "erreur : suppression impossible";
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else
            message= "aucun article selectionne";

    }
    public void viderLePanier()
    {
        try {
            TCP.send(sService, ("CANCEL_ALL" +"/#)").getBytes());
            String reponse;
            reponse = TCP.receive(sService);
            panier.viderPanier();
            message = "suppression réussie";
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void confirmer()
    {
        if(panier.getTotalCaddie()>0.0F)
        {
            try {
                TCP.send(sService, ("CONFIRMER/"+log.getIdClient()+"/"+String.valueOf(panier.getTotalCaddie()).replace(".", ",") +"/#)").getBytes());
                String reponse;
                reponse = TCP.receive(sService);
                String[] str = reponse.split("/");
                if(str[0].equals("CONFIRMER"))
                {
                    message =("Facture n°"+str[1] +" créé ");
                    panier.viderPanier();
                }
                else
                    message="problème lors de la création de la facture";
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
        else
        {
            message = "Veuillez ajouter des articles au panier";
        }
    }
    public void logout()
    {
        if(panier.getTotalCaddie()>0.0F)
        {
            try {
                TCP.send(sService, ("CANCEL_ALL/#)").getBytes());
                String reponse;
                reponse=TCP.receive(sService);
                String [] str=reponse.split("/");
                if( mettreAJourArticle(reponse)==1)
                {
                  message =  "supression réussie";
                  panier.viderPanier();
                }
                else
                    message = "suppression impossible";
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }



        }
        try {
            TCP.send(sService, ("LOGOUT/#)").getBytes());
            String reponse ;
            reponse=TCP.receive(sService);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void fermerApp()
    {
        if(panier.getTotalCaddie()>0.0F)
        {
            try {
                TCP.send(sService, ("CANCEL_ALL/#)").getBytes());
                String reponse;
                reponse=TCP.receive(sService);
                String [] str=reponse.split("/");
                if( mettreAJourArticle(reponse)==1)
                {
                    message =  "supression réussie";
                    panier.viderPanier();
                }
                else
                    message = "suppression impossible";
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(islogged==1)
        {
            try {
                TCP.send(sService, ("LOGOUT/#)").getBytes());
                String reponse ;
                reponse=TCP.receive(sService);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        exit(0);
    }
}

