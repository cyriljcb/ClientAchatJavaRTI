import Controllers.Controller;
import Modeles.Model;
import View.interface_Graphique;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        Model model = new Model(); // Créez une instance du modèle
        interface_Graphique view = new interface_Graphique("192.168.47.128", 50000); // Créez une instance de la vue
        Controller controller = new Controller(model, view); // Créez une instance du contrôleur
        view.addWindowCloseListener(controller);

        JFrame frame = new JFrame("Maraicher en ligne");
        frame.add(view.getPanel_principal());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                view.fermerApplication();
            }
        });
        frame.setVisible(true);
    }

}