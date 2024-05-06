package it.polimi.ingsw.networking;

import javax.naming.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class AppClient {
    public static void main(String[] args) throws NamingException, IOException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry();
        System.out.println("RMI registry bindings: ");
        String[] e = registry.list();
        for (String s : e) System.out.println(s);
        String remoteObjectName = "Codex_Naturalis_server";
        RMIController testController = (RMIController) registry.lookup("Codex_Naturalis_server");

        String input;
        while (true){
            System.out.println("\nGive you action \n1. For add lobby\n2. to add enter in a Lobby\n3. For show the avilable lobbies\nEnter 'exit' for exit");
            Scanner stdin = new Scanner(System.in);
            input = stdin.nextLine();

            if(input.equals("exit")){break;}

            if(input.equals("1")){
                System.out.println("Give the lobby name");
                String lobbyname = stdin.nextLine();
                //System.out.println("Give the number of player needed");
                //int num = stdin.nextInt();
                System.out.println("Give your name");
                testController.addLobby(stdin.nextLine(),lobbyname,4);
                System.out.println("These are the registered lobbies");
                List<Lobby> lobbies = testController.getLobbies();
                for(Lobby l : lobbies){
                    System.out.println(l);
                }
            } else if (input.equals("2")) {
                System.out.println("Give you name");
                String userName = stdin.nextLine();
                System.out.println("Give the lobby id where you want to enter: ");
                int lobbyid = stdin.nextInt();
                testController.addUserToLobby(lobbyid,userName);
            } else if (input.equals("3")) {
                testController.getLobbies().forEach(System.out::println);
            }

        }

        System.out.println(testController.getLobbiesNames());
    }
}