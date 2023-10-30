package Controllers;

import Modeles.Model;
import View.interface_Graphique;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmerController implements ActionListener {
    private Model model;
    private interface_Graphique view;
    public ConfirmerController(Model model, interface_Graphique view)
    {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.confirmer();
        view.updateViewPanier(model);
    }
}
