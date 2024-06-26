package it.polimi.ingsw.controller.clientcontroller;

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
}
