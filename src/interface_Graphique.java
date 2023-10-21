import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

import static java.lang.System.exit;

public class interface_Graphique extends JFrame {
    private int ligneSelectionne = -1;
    private String idClient;
    boolean logged;
    float totalCaddie= 0.0F;
    int NumArticle=0;
    private Socket sService;
    private JTextField NomTextfield ;
    private JTextField MdpTextfield;
    private JButton loginButton;
    private JButton logoutButton;
    private JCheckBox nouveauClientCheckBox;
    private JFormattedTextField article;
    private JFormattedTextField prixALUnite;
    private JFormattedTextField stock;
    private JSpinner Quantite;
    private JButton BDroite;
    private JButton BGauche;
    private JButton supprimerArticleButton;
    private JButton confirmerAchatButton;
    private JButton viderLePanierButton;
    private JTable panier;
    private JPanel panel_principal;
    private JFormattedTextField totalApayer;
    private JButton acheterButton;
    private JLabel image;
    private JPanel image1;
    void videTablePanier(){
        DefaultTableModel model = (DefaultTableModel) panier.getModel();
        model.setRowCount(0);
        totalCaddie = 0.0F;
        totalApayer.setText(Float.toString(totalCaddie));
    }
    void fermerApplication()
    {
        if(totalCaddie>0)
        {
            try {
                TCP.send(sService, ("CANCEL_ALL/#)").getBytes());
                System.out.println("est passé le send");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            String reponse;
            try {
                reponse=TCP.receive(sService);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
           videTablePanier();


        }
        try {
            TCP.send(sService, ("LOGOUT/#)").getBytes());
            System.out.println("est passé le send");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        String reponse ;
        try {

            reponse=TCP.receive(sService);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("la rep "+reponse);

        exit(0);

    }
    int mettreAJourArticle(String c) {
        int a = 1;
        String[] stri, str;
        stri = c.split("\\$(?=\\d)");

        for (int i = 0; i < stri.length; i++) {
            stri[i] = stri[i].replace("$", ""); // Supprimer les signes dollar dans chaque partie
            System.out.println("stri : " + stri[i]);
        }
        String[] subParts = stri[0].split("/");
        for (int j = 0; j < subParts.length; j++) {
            System.out.println("  subPart : " + subParts[j]);
        }
        if (subParts[0].equals("CADDIE")) {
            int nbr = Integer.parseInt(subParts[1]);
            DefaultTableModel model = (DefaultTableModel) panier.getModel();
            model.setRowCount(0);
            String id, intitule, prix1, stock1;
            totalCaddie = 0.0F;
            System.out.println("reponse dans achat " + c);
            for (int i = 0; i < nbr; i++) {
                str = stri[i + 1].split("/");
                for (int j = 0; j < str.length; j++) {
                    System.out.println("  subPart : " + str[j]);
                }

                id = str[1];
                intitule = str[2];
                prix1 = str[3];
                stock1 = str[4];
                model.addRow(new String[]{intitule, prix1, stock1});
                prix1 = str[3].replace(",", ".");
                totalCaddie += Float.parseFloat(prix1) * Integer.parseInt(stock1);
            }
            totalApayer.setText(Float.toString(totalCaddie));
        }
        else
            a = -1;
        return a;
    }

    public void dialogueErreur(String titre, String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    public void setBouton(int val){
        if(val==0){
            loginButton.setEnabled(true);
            logoutButton.setEnabled(false);
            Quantite.setEnabled(false);
            BDroite.setEnabled(false);
            BGauche.setEnabled(false);
            supprimerArticleButton.setEnabled(false);
            confirmerAchatButton.setEnabled(false);
            viderLePanierButton.setEnabled(false);
            acheterButton.setEnabled(false);
        }else {
            loginButton.setEnabled(false);
            logoutButton.setEnabled(true);
            Quantite.setEnabled(true);
            BDroite.setEnabled(true);
            BGauche.setEnabled(true);
            supprimerArticleButton.setEnabled(true);
            confirmerAchatButton.setEnabled(true);
            viderLePanierButton.setEnabled(true);
            acheterButton.setEnabled(true);
        }
    }
    public int NouvCli(){
        if(nouveauClientCheckBox.isSelected()==false){
            return 0;
        }
        else return 1;
    }

    public interface_Graphique(String ipServeur, int portServeur) {

        //initComponents();
        try {
            System.out.println("ipServeur "+ipServeur+ " portServeur = "+portServeur);
            sService = TCP.createClientSocket(ipServeur, portServeur);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Articles");
        model.addColumn("Prix à l'unité");
        model.addColumn("Quantité");

        panier.setModel(model);
        model.setRowCount(10);
        setBouton(0);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(NomTextfield.getText().equals("")||MdpTextfield.getText().equals(""))
                {
                    dialogueErreur("Erreur","les champs doivent être complétés");
                    NomTextfield.setText("");
                    MdpTextfield.setText("");
                } else if ((NomTextfield.getText().length()>=20)||(MdpTextfield.getText().length()>=20)) {
                    dialogueErreur("Erreur","les champs ne peuvent pas dépasser 20 caractères");
                    NomTextfield.setText("");
                    MdpTextfield.setText("");
                }
                else
                {
                    try {
                        System.out.println("nouveau client : "+NouvCli());
                        TCP.send(sService, ("LOGIN/"+NomTextfield.getText()+"/"+MdpTextfield.getText()+"/"+NouvCli()+"/#)").getBytes());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    String reponse ;
                    try {
                        reponse=TCP.receive(sService);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println("la rep "+reponse);
                    String[] str=reponse.split("/");
                    String st = str[0];
                    System.out.println(st);
                    if(st.equals("LOGIN"))
                    {
                        if(str[1].equals("1"))
                        {
                            setBouton(1);
                            logged=true;
                            dialogueErreur("LOGIN", str[2]);
                            idClient = str[3];
                            try {
                                TCP.send(sService, "CONSULT/1/\0#)".getBytes());
                                System.out.println("send envoyé avec la consult ");
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            String reponse1;
                            try {
                                reponse1=TCP.receive(sService);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            System.out.println("(CLIENT) reponse "+reponse1);
                            String [] stri;
                            stri=reponse1.split("/");
                            System.out.println("Passe dans le if "+reponse1);
                            if(stri[0].equals("CONSULT"))
                            {

                                String id,intitule,nomImage;
                                int sto;
                                float prix;
                                id=stri[1];
                                NumArticle= Integer.parseInt(stri[1]);
                                intitule=stri[2];
                                article.setText(intitule);
                                prixALUnite.setText(stri[3]);
                                sto= Integer.parseInt(stri[4]);
                                stock.setText(String.valueOf(sto));
                                nomImage=stri[5];
                                System.out.println("image "+nomImage);
                                Icon im = new ImageIcon("src\\images\\"+nomImage);
                                image.setIcon(im);
                            }
                        }
                        else {
                            dialogueErreur("erreur", str[2]);
                            logged =false;
                        }
                    }


                }
                }

        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(totalCaddie>0)
                {
                    try {
                        TCP.send(sService, ("CANCEL_ALL/#)").getBytes());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    String reponse;
                    try {
                        reponse=TCP.receive(sService);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    String [] str=reponse.split("/");
                    if( mettreAJourArticle(reponse)==1)
                    {
                        dialogueErreur("Supp", "supression réussie");
                    }
                    else
                        dialogueErreur("er", "erreur : suppression impossible");


                }
                try {
                    TCP.send(sService, ("LOGOUT/#)").getBytes());
                    System.out.println("est passé le send");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                String reponse ;
                try {

                    reponse=TCP.receive(sService);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("la rep "+reponse);
                String[] str=reponse.split("/");

                setBouton(0);
                stock.setText("");
                prixALUnite.setText("");
                article.setText("");
                Quantite.setValue(0);
                NomTextfield.setText("");
                MdpTextfield.setText("");
                image.setIcon(null);

            }
        });

        BDroite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(NumArticle<21)
                {
                    NumArticle++;
                }
                try {
                    TCP.send(sService, ("CONSULT/"+NumArticle+"/#)").getBytes());
                    System.out.println("est passé le send");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                String reponse;
                try {
                    reponse=TCP.receive(sService);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("(CLIENT) reponse "+reponse);
                String [] stri;
                stri=reponse.split("/");
                if(stri[0].equals("CONSULT"))
                {

                    String id,intitule,nomImage;
                    int sto;
                    float prix;
                    id=stri[1];
                    NumArticle= Integer.parseInt(stri[1]);
                    intitule=stri[2];
                    article.setText(intitule);
                    //prix= Float.parseFloat(stri[3]);
                    prixALUnite.setText(stri[3]);
                    sto= Integer.parseInt(stri[4]);
                    stock.setText(String.valueOf(sto));
                    nomImage=stri[5];
                    System.out.println("image "+nomImage);
                    Icon im = new ImageIcon("src\\images\\" + nomImage);
                    image.setIcon(im);


                }

                else {
                    dialogueErreur("erreur", stri[1]);
                    logged = false;
                }

            }
        });

        BGauche.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(NumArticle>1)
                {
                    NumArticle--;
                }
                try {
                    TCP.send(sService, ("CONSULT/"+NumArticle+"/#)").getBytes());
                    System.out.println("est passé le send");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                String reponse;
                try {
                    reponse=TCP.receive(sService);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("(CLIENT) reponse "+reponse);
                String [] stri;
                stri=reponse.split("/");
                System.out.println("Passe dans le if "+reponse);
                if(stri[0].equals("CONSULT"))
                {

                    String id,intitule,nomImage;
                    int sto;
                    float prix;
                    id=stri[1];
                    NumArticle= Integer.parseInt(stri[1]);
                    intitule=stri[2];
                    article.setText(intitule);
                    //prix= Float.parseFloat(stri[3]);
                    prixALUnite.setText(stri[3]);
                    sto= Integer.parseInt(stri[4]);
                    stock.setText(String.valueOf(sto));
                    nomImage=stri[5];
                    System.out.println("image "+nomImage);
                    Icon im = new ImageIcon("src\\images\\" + nomImage);
                    image.setIcon(im);


                }

                else {
                    dialogueErreur("erreur", stri[1]);
                    logged = false;
                }

            }
        });

        acheterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if((int) Quantite.getValue()==0)
                {
                    dialogueErreur("", "veuillez selectionner une quantite valide");
                }
                else {
                    try {
                        TCP.send(sService, ("ACHAT/"+NumArticle+"/"+(int)Quantite.getValue()+"/#)").getBytes());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    String reponse;
                    try {
                        reponse=TCP.receive(sService);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if( mettreAJourArticle(reponse)==1)
                    {
                        dialogueErreur("achat","achat réussi");
                    }
                    else
                        dialogueErreur("achat", "erreur : achat impossible");
                }
            }
        });
        panier.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    ligneSelectionne = panier.getSelectedRow();
                    if (ligneSelectionne != -1) {
                        System.out.println("Ligne sélectionnée : " + ligneSelectionne);
                    }
                }
            }
        });

        supprimerArticleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ligneSelectionne != -1) {
                    try {
                        TCP.send(sService, ("CANCEL/" + ligneSelectionne + "/#)").getBytes());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    String reponse;
                    try {
                        reponse = TCP.receive(sService);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if( mettreAJourArticle(reponse)==1)
                    {
                        dialogueErreur("Supp", "supression réussie");
                    }
                    else
                        dialogueErreur("er", "erreur : suppression impossible");
                }
                else
                    dialogueErreur("Suppression", "aucun article selectionne");
            }
        });
        viderLePanierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    TCP.send(sService, ("CANCEL_ALL" +"/#)").getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                String reponse;
                try {
                    reponse = TCP.receive(sService);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("la reponse : " + reponse);
                String[] stri, str;
                stri = reponse.split("\\$(?=\\d)");

                for (int i = 0; i < stri.length; i++) {
                    stri[i] = stri[i].replace("$", ""); // Supprimer les signes dollar dans chaque partie
                    System.out.println("stri : " + stri[i]);
                }
                String[] subParts = stri[0].split("/");
                for (int j = 0; j < subParts.length; j++) {
                    System.out.println("  subPart : " + subParts[j]);
                }
                if (subParts[0].equals("CADDIE")) {
                   videTablePanier();
                } else {
                    dialogueErreur("Suppression", "aucun article selectionne");
                }
            }
        });
        confirmerAchatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(totalCaddie>0.0F)
                {
                    String caddie = Float.toString(totalCaddie);
                    caddie = caddie.replace(".", ",");

                    try {
                        TCP.send(sService, ("CONFIRMER/"+idClient+"/"+caddie +"/#)").getBytes());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    String reponse;
                    try {
                        reponse = TCP.receive(sService);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    String[] str = reponse.split("/");
                    if(str[0].equals("CONFIRMER"))
                    {
                        dialogueErreur("Facture","Facture n°"+str[1] +" créé ");
                        videTablePanier();
                    }
                }
                else
                {
                    dialogueErreur("Paiement","Veuillez ajouter des articles au panier");
                }


            }
        });
    }

    public static void main(String[] args) {
        //FlatDarculaLaf.setup();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maraicher en ligne");
            interface_Graphique app = new interface_Graphique("192.168.47.128", 50000);
            frame.add(app.panel_principal);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Empêche la fermeture par défaut
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    app.fermerApplication(); // Appel de la méthode pour fermer l'application
                }
            });
            frame.setVisible(true);
        });
    }

}