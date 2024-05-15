package it.polimi.ingsw.test.server;

import it.polimi.ingsw.controller.clientcontroller.ConnectionDefaultSettings;
import it.polimi.ingsw.controller.clientcontroller.SocketClientController;
import it.polimi.ingsw.controller.socket.SocketClient;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;
import it.polimi.ingsw.model.InvalidOperationException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestSocketClient {
    public static void main(String[] args) throws IOException, NotBoundException {

        System.out.println("Welcome to Codex Naturalis!!\n");

        SocketClientController client = new SocketClientController();

        try{

            while(true) {
                System.out.println("Type 1 for register, Type 'quit' for close the client ");
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();

                int tempCode;
                String Username;

                if(line.equals("quit")) {break; }
                else if(line.equals("1")) {//Dovrei fare un try catch
                    System.out.println("Type your username");
                    try {
                        tempCode = client.register(scanner.nextLine());
                        System.out.println("Successful registration");
                        System.out.println("Your temporary code is: " + tempCode);
                        System.out.println("Logging in");
                        //client.login(username,tempcode)
                    }catch (InvalidOperationException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            client.closeSocket();
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}
