package Modeles;

public class LoginModel {
    private boolean logged;
    private String idClient;

    public LoginModel() {
        logged = false;
        idClient = "";
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }
}
