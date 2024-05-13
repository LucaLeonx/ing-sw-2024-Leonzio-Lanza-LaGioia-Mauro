package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;
import it.polimi.ingsw.controller.socket.SocketClient;

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
        Registry registry = LocateRegistry.getRegistry();
        System.out.println("RMI registry bindings: ");
        String[] e = registry.list();
        for (String s : e) System.out.println(s);
        String remoteObjectName = "Codex_Naturalis_server";
        ClientController controller = new RMIClientController();
        int tempCode = controller.register("Luca");
        controller.login("Luca", tempCode);

        String input;
        while (true){
            System.out.println("\nGive you action \n1. For add lobby\n2. to add enter in a Lobby\n3. For show the avilable lobbies\nEnter 'exit' for exit");
            Scanner stdin = new Scanner(System.in);
            input = stdin.nextLine();

            if(input.equals("exit")){break;}

            if(input.equals("1")){
                System.out.println("Give the lobby name");
                String lobbyname = stdin.nextLine();
                System.out.println("Give the number of player needed");
                int num = stdin.nextInt();
                System.out.println("Give your name");
                controller.createLobby(lobbyname,4);
                System.out.println("These are the registered lobbies");
                List<LobbyInfo> lobbies = controller.getLobbyList();
                for(LobbyInfo l : lobbies){
                    System.out.println(l);
                }
            } else if (input.equals("2")) {
                System.out.println("Give you name");
                String userName = stdin.nextLine();
                System.out.println("Give the lobby id where you want to enter: ");
                int lobbyid = stdin.nextInt();
                controller.joinLobby(lobbyid);
            } else if (input.equals("3")) {
                controller.getLobbyList().forEach(System.out::println);
            }

        }
    }

    private static void startSocket() throws IOException, NotBoundException {
        SocketClient client = new SocketClient(6660);

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
