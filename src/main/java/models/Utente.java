package models;

public abstract class Utente {
    private final String cf;
    private final String nome;
    private final String cognome;
    private final String email;

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCf() {
        return cf;
    }

    protected Utente(String cf, String nome, String cognome, String email) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }
    protected Utente(String cf, String nome, String cognome) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.email=null;
    }

}
