package models;


import java.util.Date;

public class Messaggio {
    public Messaggio(int id,int precedente,Messaggio citato, Lavoratore autore, String testo, Date d, Canale canale) {
        this.citato = citato;
        this.autore = autore;
        this.testo = testo;
        this.data = d;
        this.canale = canale;
        this.id=id;
        this.precedente=precedente;
    }

    public Messaggio getCitato() {
        return citato;
    }

    public Lavoratore getAutore() {
        return autore;
    }

    public String getTesto() {
        return testo;
    }

    public Date getData() {
        return data;
    }

    public String getNameCanale(){
    return      canale.getNome();
    }
    public String getNameProgetto(){
        return canale.getProgetto();
    }

    public int getId() {
        return id;
    }

    public void setCitato(Messaggio citato) {
        this.citato = citato;
    }

    private int id,precedente;
    private Messaggio citato;
    private  Lavoratore autore;
    private  String testo;
    private Date data;
    private Canale canale;
    public Messaggio(int id,int precedente, Lavoratore autore, String testo, Date d, Canale canale) {
        this.citato = null;
        this.autore = autore;
        this.testo = testo;
        this.data = d;
        this.canale = canale;
        this.id=id;
        this.precedente=precedente;
    }
    public int getPrecedente() {
        return precedente;
    }
}
