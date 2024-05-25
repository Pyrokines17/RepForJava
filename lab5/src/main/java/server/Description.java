package server;

public class Description {
    private String status;
    private String avaPath;

    public Description(String status, String avaPath) {
        this.status = status;
        this.avaPath = avaPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvaPath() {
        return avaPath;
    }

    public void setAvaPath(String avaPath) {
        this.avaPath = avaPath;
    }
}
