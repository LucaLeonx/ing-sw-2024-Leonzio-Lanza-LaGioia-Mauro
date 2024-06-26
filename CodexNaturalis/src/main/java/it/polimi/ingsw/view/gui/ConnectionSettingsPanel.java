package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.controller.clientcontroller.ConnectionSettings;

public class ConnectionSettingsPanel extends JPanel {

    private final Map<String, String> formValues;
    public ConnectionSettingsPanel(){
        this.formValues = new HashMap<>();
        buildPanel();
    }

    private void buildPanel() {
        GroupLayout groupLayout = new GroupLayout(this);
        this.setLayout(groupLayout);

        JLabel serverAddress = new JLabel("Server address: ");
        JLabel RMIPort = new JLabel("RMI port: ");
        JLabel socketPort = new JLabel("Socket port: ");

        JTextField serverAddressField = new JTextField();
        JTextField RMIPortField = new JTextField();
        JTextField socketPortField = new JTextField();

        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        GroupLayout.SequentialGroup serverLine = groupLayout.createSequentialGroup();
        serverLine.addComponent(serverAddress).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        serverLine.addComponent(serverAddressField);


    }

    private void addFormFieldToGroup(GroupLayout groupLayout, String label, String identifier){
        JLabel fieldLabel = new JLabel(label + " ");
        JTextField field = new JTextField();
        groupLayout.add

    }
}
