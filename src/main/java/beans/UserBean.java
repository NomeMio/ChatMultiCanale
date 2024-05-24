package beans;

import utils.Ruoli;

public class UserBean {
    private final String nome;
    private final String cognome;
    private final String emial;
    private final Ruoli ruolo;

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmial() {
        return emial;
    }

    public String getRuolo() {
        return ruolo == Ruoli.Amministratore ? "Amministratore" : "Lavoratore";
    }

    public UserBean(String nome, String cognome, String emial, Ruoli ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.emial = emial;
        this.ruolo = ruolo;
    }
}
