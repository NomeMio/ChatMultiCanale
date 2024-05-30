package models;

import java.sql.Date;

public class Canale {
    private String nome;
    private String progetto;
   private CanaliTypes tipo;
    private Date dataCreazione;

    public String getNome() {
        return nome;
    }

    public String getProgetto() {
        return progetto;
    }

    public CanaliTypes getTipo() {
        return tipo;
    }

    public String getTypeToString(){
        switch (tipo){
            case PRIVATO -> {
                return "privato";
            }
            case PUBLICO -> {
                return "publico";
            }
        }
        return "?";
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public Canale(String nome, String progetto, CanaliTypes tipo, Date dataCreazione) {
        this.nome = nome;
        this.progetto = progetto;
        this.tipo = tipo;
        this.dataCreazione = dataCreazione;
    }
}
