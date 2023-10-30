package Controllers;

import Modeles.Model;
import Modeles.WindowCloseListener;
import View.interface_Graphique;

public class Controller implements WindowCloseListener {
    private Model model;
    private interface_Graphique view;

    public Controller(Model model, interface_Graphique view) {
        this.model = model;
        this.view = view;
        view.addloginButtonListener(new LoginController(model, view));
        view.addBDroiteListener(new BDroiteController(model,view));
        view.addBGaucheListener(new BGaucheController(model,view));
        view.addAcheterListener(new AcheterController(model,view));
        view.addSupprimerListener(new SupprimerController(model,view));
        view.addViderPanierListener(new ViderPanierController(model,view));
        view.addConfirmerListener(new ConfirmerController(model,view));
        view.addLogoutListener(new LogoutController(model,view));
    }

    @Override
    public void onWindowClose() {
        model.fermerApp();
    }
}
