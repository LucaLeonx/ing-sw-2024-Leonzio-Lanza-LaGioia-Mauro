package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.model.InvalidOperationException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuthenticationManagerImpl extends UnicastRemoteObject implements AuthenticationManager {
    private final CoreServer internalServer;
    private final UserList userList;

    public AuthenticationManagerImpl(CoreServer internalServer, UserList userList) throws RemoteException {
        super();
        this.internalServer = internalServer;
        this.userList = userList;
    }

    @Override
    public int register(String username){
        synchronized (userList){
            if(userList.isUserRegistered(username)){
                throw new InvalidOperationException("Username " + username + " is already in use");
            }
        }

        User newUser = new User(username);
        int tempCode = newUser.generateNewPass();
        userList.addUser(newUser);
        return tempCode;
    }

    @Override
    public ServerController login(String username, int tempCode, NotificationSubscriber subscriber) throws RemoteException {

        User loginUser;

        synchronized (userList) {
            if (!userList.isUserRegistered(username)) {
                throw new InvalidOperationException("Wrong username or password");
            }

            loginUser = userList.getUserByUsername(username);
        }

        if(loginUser.checkPass(tempCode)){
            loginUser.setNotificationSubscriber(subscriber);
            return new AuthenticatedSession(loginUser, internalServer);
        } else {
            throw new InvalidOperationException("Wrong username or tempCode");
        }
    }
}
