package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.ConnectionDefaultSettings;
import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;
import it.polimi.ingsw.controller.socket.SocketClient;
import it.polimi.ingsw.model.InvalidOperationException;

import javax.naming.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppClient {
    public static void main(String[] args) throws NamingException, IOException, NotBoundException {

        System.out.println("Welcome to Codex Naturalis!!\n");

        Scanner scanner = new Scanner(System.in);
        String input;
        do{
            System.out.println("Please choose between RMI and Socket connection");
            System.out.println("1. RMI");
            System.out.println("2. Socket");
            input = scanner.nextLine();
            if(input.equals("1")){
                startRMI();
            } else if (input.equals("2")) {
                startSocket();
            }else {
                System.out.println("Character non valid!! Please try again ");
            }
        }while(!input.equals("1") && !input.equals("2"));

    }

    private static void startRMI() throws IOException, NotBoundException {
        Scanner stdin = new Scanner(System.in);
        ClientController controller = new RMIClientController();

        System.out.println("Choose action: ");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print(">> ");

        String username = "";
        int tempCode = 0;

        switch (stdin.nextInt()){
            case 2 -> {
                boolean validUsername = false;
                while(!validUsername){
                    System.out.print("Type your username: ");
                    try {
                        stdin.nextLine();
                        username = stdin.nextLine().trim();
                        tempCode = controller.register(username);
                        System.out.println("Successful registration");
                        System.out.println("Your temporary code is: " + tempCode);
                        System.out.println("Logging in");
                        controller.login(username, tempCode);
                        validUsername = true;
                    } catch (InvalidOperationException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            default -> {
                boolean validLogin = false;
                while(!validLogin){
                    stdin.nextLine();
                    System.out.print("Type your username: ");
                    username = stdin.nextLine().trim();
                    System.out.print("Type your temporary code: ");
                    tempCode = stdin.nextInt();
                    try{
                        controller.login(username, tempCode);
                        System.out.println("Successful login as " + username);
                        validLogin = true;
                    } catch (InvalidOperationException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }


        String input;
        while (true){
            System.out.println("\nGive you action \n1. For add lobby\n2. to add enter in a Lobby\n3. For show the avilable lobbies\nEnter 'exit' for exit");

            input = stdin.nextLine();

            if(input.equals("exit")){
                controller.logout();
                break;
            }

            if(input.equals("1")){
                System.out.println("Give the lobby name");
                String lobbyname = stdin.nextLine();
                System.out.println("Give the number of player needed");
                int num = stdin.nextInt();
                try {
                    controller.createLobby(lobbyname, num);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
                System.out.println("These are the registered lobbies");
                List<LobbyInfo> lobbies = controller.getLobbyList();
                for(LobbyInfo l : lobbies){
                    System.out.println(l);
                }
            } else if (input.equals("2")) {
                System.out.println("Give the lobby id where you want to enter: ");
                int lobbyid = stdin.nextInt();
                try {
                    controller.joinLobby(lobbyid);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            } else if (input.equals("3")) {
                controller.getLobbyList().forEach(System.out::println);
            }

        }
    }

    private static void startSocket() throws IOException, NotBoundException {
        SocketClient client = new SocketClient(ConnectionDefaultSettings.SocketServerPort);

        try{
            Message msg = new Message(MessageType.CREATE_LOBBY,
                    new LobbyInfo(1,"Seconda Lobby di Prova","Luca",new ArrayList<>(),4,0));
            client.startClientConnection();

            System.out.println("Type 'send' for send an info, Type 'quit' for close the client ");
            Scanner scanner = new Scanner(System.in);
            while(true) {
                String line = scanner.nextLine();
                if(line.equals("quit")) {break; }
                else if (line.equals("send")) {
                    client.sendMessage((Object) msg);
                }
            }
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}
