package beans;

public class MessaggioBean {
    private MessaggioBean citato;
    private String testo;

    public MessaggioBean(String testo,String nomeCanale, String nomeProgetto) {
        this.citato = null;
        this.testo = testo;
        this.emialAutore = null;
        this.nomeAutore = null;
        this.cognomeAutore = null;
        this.timestamp = null;
        this.nomeCanale = nomeCanale;
        this.nomeProgetto = nomeProgetto;
        this.idMess = 0;
        this.idPrecedente = 0;
    }

    public MessaggioBean getCitato() {
        return citato;
    }

    public String getTesto() {
        return testo;
    }

    public String getCognomeAutore() {
        return cognomeAutore;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getIdMess() {
        return idMess;
    }

    public int getIdPrecedente() {
        return idPrecedente;
    }

    public String getNomeProgetto() {
        return nomeProgetto;
    }

    public String getNomeCanale() {
        return nomeCanale;
    }

    public String getNomeAutore() {
        return nomeAutore;
    }

    public String getEmialAutore() {
        return emialAutore;
    }

    public void setCitato(MessaggioBean citato) {
        this.citato = citato;
    }

    public MessaggioBean(String testo, String emialAutore, String nomeAutore, String cognomeAutore, String timestamp, String nomeCanale, String nomeProgetto, int idMess, int idPrecedente) {
        this.citato = null;
        this.testo = testo;
        this.emialAutore = emialAutore;
        this.nomeAutore = nomeAutore;
        this.cognomeAutore = cognomeAutore;
        this.timestamp = timestamp;
        this.nomeCanale = nomeCanale;
        this.nomeProgetto = nomeProgetto;
        this.idMess = idMess;
        this.idPrecedente = idPrecedente;
    }

    private String emialAutore;
    private String nomeAutore;
    private String cognomeAutore;
    private String timestamp;
    private String nomeCanale;
    private String nomeProgetto;
    private int idMess;
    private int idPrecedente;

}
