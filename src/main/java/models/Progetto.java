package models;

import java.sql.Date;

public class Progetto {
    private String nome;
    private Date data;
    private Lavoratore capoProgetto;

    public void setCapoProgetto(Lavoratore capoProgetto) {
        this.capoProgetto = capoProgetto;
    }

    public String getNome() {
        return nome;
    }

    public Date getData() {
        return data;
    }

    public Lavoratore getCapoProgetto() {
        return capoProgetto;
    }

    public Progetto(String name, Date data, Lavoratore capo) {
        this.nome = name;
        this.data = data;
        this.capoProgetto=capo;

    }
    public Progetto(String name, Date data) {
        this.nome = name;
        this.data = data;
        this.capoProgetto=null;

    }

    public static Progetto getProgetto(String name, Date data,Lavoratore capo) {
        return new Progetto(name, data,capo);
    }
    public static Progetto getProgetto(String name, Date data) {
        return new Progetto(name, data);
    }

}
