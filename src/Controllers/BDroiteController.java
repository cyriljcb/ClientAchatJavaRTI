package Controllers;

import Modeles.Model;
import View.interface_Graphique;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BDroiteController implements ActionListener {
    private Model model;
    private interface_Graphique view;
    public BDroiteController(Model model,interface_Graphique view)
    {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.consult(true);
        view.updateViewConsult(model);
    }


}
