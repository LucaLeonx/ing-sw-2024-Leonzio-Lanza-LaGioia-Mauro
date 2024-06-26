package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.clientcontroller.ConnectionDefaultSettings;

public class ConnectionSettings {
    private String serverHost;
    private int RMIPort;
    private int socketPort;
    private static final String RMIServerName = ConnectionDefaultSettings.RMIServerName;

    public ConnectionSettings(String serverHost, int RMIPort, int socketPort) {
        this.serverHost = serverHost;
        this.RMIPort = RMIPort;
        this.socketPort = socketPort;
    }

    public ConnectionSettings() {
        this(ConnectionDefaultSettings.RMIRegistryHost, ConnectionDefaultSettings.RMIRegistryPort, ConnectionDefaultSettings.SocketServerPort);
    }

    public String getServerHost() {
        return serverHost;
    }

    public int getRMIPort(){
        return RMIPort;
    }

    public int getSocketPort(){
        return socketPort;
    }

    public String getRMIServerName(){
        return RMIServerName;
    }

    public void setServerHost(String serverHost){
        this.serverHost = serverHost;
    }

    public void setRMIPort(int RMIPort){
        this.RMIPort = RMIPort;
    }

    public void setSocketPort(int socketPort){
        this.socketPort = socketPort;
    }

    public static ConnectionSettings parseConnectionSettings(String[] args) {
        ConnectionSettings connectionSettings = new ConnectionSettings();

        for(int i = 0; i < args.length; i++){
            switch (args[i]) {
                case "-H", "--host" -> connectionSettings.setServerHost(args[i + 1]);
                case "-Rp", "--rmi-port" -> connectionSettings.setRMIPort(Integer.parseInt(args[i + 1]));
                case "-Sp", "--socket-port" -> connectionSettings.setSocketPort(Integer.parseInt(args[i + 1]));
            }
        }

        return connectionSettings;
    }
}
