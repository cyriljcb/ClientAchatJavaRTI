package Controllers;

import Modeles.Model;
import View.interface_Graphique;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginController implements ActionListener {
    private Model model;
    private interface_Graphique view;

    public LoginController(Model model, interface_Graphique view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nom = view.getNomTextfield();
        String mdp = view.getMdpTextfield();
        boolean nouveauClient = view.getNouveauClientCheckBox().isSelected();
        int nvCli=0;
        if(nouveauClient)
            nvCli=1;

        System.out.println("nom : "+nom+" mdp :"+mdp);
       boolean v = model.login(view.getsService(),nom, mdp, nvCli);
        if(v)
            view.updateView(model);

    }
}