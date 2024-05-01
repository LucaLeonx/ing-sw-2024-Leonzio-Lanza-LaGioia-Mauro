package it.polimi.ingsw.view.gui;
import javax.swing.*;
import java.awt.*;


public class GUI {
    public static void createAndShowGUI()
    {
        JFrame window = new JFrame("Box Layout");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents(window);

        JFrame window2 = new JFrame("Box Layout");
        window2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents(window2);

        window.setSize(600, 600);
        window.setVisible(true);
        window2.setVisible(false);
    }

    public static void addComponents(JFrame frame){
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        addButton(frame, "Socket connection");
        addButton(frame, "RMI connection");
    }

    public static void addButton(JFrame frame, String name){
        JButton button = new JButton(name);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(button);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::createAndShowGUI);
    }

}

