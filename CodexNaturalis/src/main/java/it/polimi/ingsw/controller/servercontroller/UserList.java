package it.polimi.ingsw.controller.servercontroller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UserList {
    private final ConcurrentMap<String, User> users;

    public UserList(List<User> users){
        this.users = new ConcurrentHashMap<>();
        for(User user : users){
            addUser(user);
        }
    }

    public UserList() {
        this(new ArrayList<>());
    }

    public void addUser(User user){
        this.users.put(user.getUsername(), user);
    }

    public void removeUser(String username){
        this.users.remove(username);
    }

    public void removeUser(User user){
        this.users.remove(user.getUsername());
    }

    public User getUserByUsername(String username){
        return this.users.get(username);
    }

    public boolean isUserRegistered(String username){
        return users.containsKey(username);
    }

    public Set<User> getUsers(){
        return new HashSet<>(users.values());
    }
}
