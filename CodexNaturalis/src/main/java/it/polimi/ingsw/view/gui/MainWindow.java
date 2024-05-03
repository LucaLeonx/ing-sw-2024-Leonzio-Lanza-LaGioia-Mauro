package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

    private JPanel currentPanel;

    public MainWindow() {
        super("Main Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Imposta il pannello iniziale
        currentPanel = new StartPanel();
        setContentPane(currentPanel);

        setVisible(true);
    }

    // Metodo per cambiare il pannello attuale
    public void changePanel(JPanel newPanel) {
        setContentPane(newPanel);
        validate(); // Ricarica il contenuto del frame
    }

    // Pannello di avvio
    private class StartPanel extends JPanel implements ActionListener {
        public StartPanel() {
            JButton goToNextWindowButton = new JButton("Go to Next Window");
            goToNextWindowButton.addActionListener(this);
            add(goToNextWindowButton);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Cambia al pannello successivo quando il pulsante viene premuto
            changePanel(new NextWindowPanel());
        }
    }

    // Pannello successivo
    private class NextWindowPanel extends JPanel {
        public NextWindowPanel() {
            JButton goBackButton = new JButton("Go Back");
            goBackButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Torna al pannello precedente quando il pulsante viene premuto
                    changePanel(new StartPanel());
                }
            });
            add(goBackButton);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
