package models;

public abstract class Utente {
    private final String cf;
    private final String nome;
    private final String cognome;
    private final String Email;

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return Email;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCf() {
        return cf;
    }

    public Utente(String cf, String nome, String cognome, String email) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        Email = email;
    }

    abstract Utente createUtente(String cf, String nome, String cognome, String email);
}
