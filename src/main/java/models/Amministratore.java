package models;

public class Amministratore extends Utente {
    public Amministratore(String cf, String nome, String cognome, String email) {
        super(cf, nome, cognome, email);
    }

    @Override
    Utente createUtente(String cf, String nome, String cognome, String email) {
        return new Amministratore(cf, nome, cognome, email);
    }
}
