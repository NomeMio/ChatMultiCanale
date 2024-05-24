package models;

import java.sql.Date;

public class Progetto {
    private String nome;
    private Date data;

    private Progetto(String name, Date data) {
        this.nome = name;
        this.data = data;
    }

    public static Progetto getProgetto(String name, Date data) {
        return new Progetto(name, data);
    }
}
