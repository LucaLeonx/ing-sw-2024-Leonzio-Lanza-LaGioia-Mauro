package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.model.InvalidOperationException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuthenticationManagerImpl extends UnicastRemoteObject implements AuthenticationManager {

    private final FrontierServerLayer serverReference;
    private final UserList userList;

    public AuthenticationManagerImpl(FrontierServerLayer serverReference, UserList userList) throws RemoteException {
        super();
        this.serverReference = serverReference;
        this.userList = userList;
    }

    @Override
    public int register(String username){
        if(userList.isUserRegistered(username)){
            throw new InvalidOperationException("Username " + username + " is already in use");
        }

        User newUser = new User(username);
        int tempCode = newUser.generateNewPass();
        userList.addUser(newUser);
        return tempCode;
    }

    @Override
    public ServerController login(String username, int tempCode) throws RemoteException {
        if(!userList.isUserRegistered(username)){
            throw new InvalidOperationException("Wrong username or password");
        }

        User loggedUser = userList.getUserByUsername(username);
        if(loggedUser.checkPass(tempCode)){
            return new AuthenticatedSession(loggedUser, serverReference);
        } else {
            throw new InvalidOperationException("Wrong username or tempCode");
        }
    }
}
