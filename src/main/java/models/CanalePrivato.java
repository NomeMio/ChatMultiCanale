package models;

import java.sql.Date;

public class CanalePrivato extends Canale{
    private Messaggio messaggioInizializzante;
    private Lavoratore[] lavoratori;

    public Lavoratore[] getLavoratori() {
        return lavoratori;
    }

    public Messaggio getMessaggioInizializzante() {
        return messaggioInizializzante;
    }

    public CanalePrivato(String nome, String progetto, CanaliTypes tipo, Date dataCreazione, Messaggio mess, Lavoratore[] lavoratori) {
        super(nome, progetto, tipo, dataCreazione);
        this.messaggioInizializzante=mess;
        this.lavoratori=lavoratori;
    }
}

