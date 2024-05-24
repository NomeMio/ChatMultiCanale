package beans;

public class CredentialBean {
    private String userName;
    private String Password;

    public String getPassword() {
        return Password;
    }

    public String getUserName() {
        return userName;
    }

    public CredentialBean(String password, String userName) {
        Password = password;
        this.userName = userName;
    }
}
