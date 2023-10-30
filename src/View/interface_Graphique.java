package View;

import Modeles.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class interface_Graphique extends JFrame implements ModelObserver {
    private String idClient;
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
    DefaultTableModel tabModel;

    private ArrayList<WindowCloseListener> windowCloseListeners = new ArrayList<>();

    public void addWindowCloseListener(WindowCloseListener listener) {
        windowCloseListeners.add(listener);
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
            nouveauClientCheckBox.setEnabled(false);
        }
    }

    public interface_Graphique(String ipServeur, int portServeur) {
        try {
            sService = TCP.createClientSocket(ipServeur, portServeur);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tabModel = new DefaultTableModel();
        tabModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Rendre toutes les cellules non modifiables
                return false;
            }
        };
        tabModel.addColumn("Articles");
        tabModel.addColumn("Prix à l'unité");
        tabModel.addColumn("Quantité");

        panier.setModel(tabModel);
        tabModel.setRowCount(10);
        setBouton(0);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fermerApplication();
            }
        });
    }
    @Override
    public void updateView(Model model) {
        setBouton(model.getIslogged());
        dialogueErreur("",model.getMessage());
        article.setText(model.getArticleEnCours().getIntitule());
        prixALUnite.setText(String.valueOf(model.getArticleEnCours().getPrix()));
        stock.setText(String.valueOf(model.getArticleEnCours().getStock()));
        Icon im = new ImageIcon("src\\images\\"+model.getArticleEnCours().getImage());
        image.setIcon(im);
    }
    @Override
    public void updateViewConsult(Model model) {
        article.setText(model.getArticleEnCours().getIntitule());
        prixALUnite.setText(String.valueOf(model.getArticleEnCours().getPrix()));
        stock.setText(String.valueOf(model.getArticleEnCours().getStock()));
        Icon im = new ImageIcon("src\\images\\"+model.getArticleEnCours().getImage());
        image.setIcon(im);
    }
    @Override
    public void updateViewLogout(Model model) {
        setBouton(model.getIslogged());
        stock.setText("");
        prixALUnite.setText("");
        article.setText("");
        Quantite.setValue(0);
        NomTextfield.setText("");
        MdpTextfield.setText("");
        image.setIcon(null);
    }
    @Override
    public void updateViewPanier(Model model) {
        dialogueErreur("",model.getMessage());
        tabModel.setRowCount(0);
        for (Article article : model.getPanier().getArticles())
        {
            tabModel.addRow(new String[]{article.getIntitule(),String.valueOf(article.getPrix()),String.valueOf(article.getStock())});
        }
        totalApayer.setText(String.valueOf(model.getPanier().getTotalCaddie()));
    }
    public void fermerApplication() {
        for (WindowCloseListener listener : windowCloseListeners) {
            listener.onWindowClose();
        }
    }
    public JCheckBox getNouveauClientCheckBox(){
        return nouveauClientCheckBox;
    }
    public Socket getsService() {
        return sService;
    }

    public String getMdpTextfield() {
        return MdpTextfield.getText();
    }

    public String getNomTextfield() {
        return NomTextfield.getText();
    }
    public Component getPanel_principal() {
        return panel_principal;
    }
    public int getQuantite()
    {
        return (int)Quantite.getValue();
    }
    public JTable getPanierTable() {
        return panier;
    }

    public void addloginButtonListener(ActionListener listener){
        loginButton.addActionListener(listener);
    }

    public void addBDroiteListener(ActionListener l) {
        BDroite.addActionListener(l);
    }
    public void addBGaucheListener(ActionListener l) {
        BGauche.addActionListener(l);
    }

    public void addAcheterListener(ActionListener l) {
        acheterButton.addActionListener(l);
    }
    public void addSupprimerListener(ActionListener l) {
        supprimerArticleButton.addActionListener(l);
    }
    public void addViderPanierListener(ActionListener l) {
        viderLePanierButton.addActionListener(l);
    }
    public void addConfirmerListener(ActionListener l) {
        confirmerAchatButton.addActionListener(l);
    }
    public void addLogoutListener(ActionListener l) {
        logoutButton.addActionListener(l);
    }

}