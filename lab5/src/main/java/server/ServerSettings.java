package server;

public class ServerSettings {
    private String ip;
    private int port;
    private boolean logging;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }

    public int getPort() {
        return port;
    }

    public void setLogging(String logging) {
        this.logging = Boolean.parseBoolean(logging);
    }

    public boolean getLogging() {
        return logging;
    }

}
