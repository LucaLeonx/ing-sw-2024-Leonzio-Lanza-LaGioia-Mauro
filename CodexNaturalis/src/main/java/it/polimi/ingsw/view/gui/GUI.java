package it.polimi.ingsw.view.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI {
    public static void createAndShowGUI()
    {
        JFrame window = new JFrame("Box Layout");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents(window);

        JFrame window2 = new JFrame("Second Frame");
        window2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents(window2);

        window.setSize(600, 600);
        window2.setSize(600, 800);
        window.setVisible(true);
        window2.setVisible(true);
    }

    public static void addComponents(JFrame frmame){
        panel.setLayout(new BoxLayout(panel.getParent(), BoxLayout.Y_AXIS));

        addButton(panel, "Socket connection");
        addButton(panel, "RMI connection");
    }

    public static void addButton(JFrame frame, String name){
        JButton button = new JButton(name);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //OptionPane.showMessageDialog(this, window);
            }

        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::createAndShowGUI);

    }

}

