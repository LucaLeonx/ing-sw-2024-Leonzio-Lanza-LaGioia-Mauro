package it.polimi.ingsw.test.server;

import it.polimi.ingsw.controller.ConnectionSettings;
import it.polimi.ingsw.controller.clientcontroller.SocketClientController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class TestSocketClient {
    public static void main(String[] args) throws IOException, NotBoundException {

        System.out.println("Welcome to Codex Naturalis!!\n");

        SocketClientController client = new SocketClientController(new ConnectionSettings());

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
