package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This panel lets players decide if they want to register to the Server or they already have credentials to login.
 */
public class ChooseLoginPanel extends StandardPanel {
    public final Color beige = new Color(255,240,219); // RGB values
    public ChooseLoginPanel(){
        buildPanel();
    }

    private void buildPanel(){
        this.setLayout(new GridBagLayout());
        JLabel incipitTextArea = new JLabel("Choose your authentication:\n");
        incipitTextArea.setBackground(beige);
        incipitTextArea.setHorizontalAlignment(SwingConstants.CENTER);
        incipitTextArea.setOpaque(true);
        incipitTextArea.setFont(new Font("Arial", Font.BOLD, 20));  // Font ingrandito

        JButton login= new JButton("Login");
        JButton register= new JButton("Register");
        ButtonStyle(login);
        ButtonStyle(register);

        JTextArea connectionTextArea = new JTextArea();

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
                connectionTextArea.append("Login selected\n");
                MainWindow.goToWindow("loginPanel");
            }
        }
        );

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
                connectionTextArea.append("Register selected\n");
                MainWindow.goToWindow("insertNamePanel");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        add(incipitTextArea, gbc);

        gbc.gridy = 1;
        add(login, gbc);

        gbc.gridy = 2;
        add(register, gbc);

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = null;
        try {

            image = new ImageIcon(this.getClass().getResource("/other_images/codex_game.jpg")).getImage();
        }
        catch (Exception e){
            System.out.println("Path non rilevato");
        }
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

}
