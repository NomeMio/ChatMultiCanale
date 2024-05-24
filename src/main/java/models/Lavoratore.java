package models;

public class Lavoratore extends Utente {
    public Lavoratore(String cf, String nome, String cognome, String email) {
        super(cf, nome, cognome, email);
    }

    @Override
    Utente createUtente(String cf, String nome, String cognome, String email) {
        return new Lavoratore(cf, nome, cognome, email);
    }

}
