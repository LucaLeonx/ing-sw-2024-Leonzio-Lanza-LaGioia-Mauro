package it.polimi.ingsw.test.server.rmi;

import it.polimi.ingsw.controller.servercontroller.Controller;
import it.polimi.ingsw.controller.servercontroller.Lobby;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;


public class TestClientRMI {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        System.out.println("RMI registry bindings: ");
        String[] e = registry.list();
        for (String s : e) System.out.println(s);
        Controller testController = (Controller) registry.lookup("codex_naturalis_server");

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
                testController.addLobby(lobbyname, num);
                System.out.println("These are the registered lobbies");
                List<LobbyInfo> lobbies = testController.getLobbies();
                for(LobbyInfo l : lobbies){
                    System.out.println(l);
                }
            } else if (input.equals("2")) {
                System.out.println("Give you name");
                String userName = stdin.nextLine();
                System.out.println("Give the lobby id where you want to enter: ");
                int lobbyid = stdin.nextInt();
                testController.joinLobby(lobbyid);
            } else if (input.equals("3")) {
                testController.getLobbies().forEach(System.out::println);
            }

        }

        System.out.println(testController.getLobbies().stream().map(LobbyInfo::name).toList());
    }
}
