package models;

public class Lavoratore extends Utente {
    public Lavoratore(String cf, String nome, String cognome, String email) {
        super(cf, nome, cognome, email);
    }
    public Lavoratore(String cf, String nome, String cognome) {
        super(cf, nome, cognome);
    }



}
