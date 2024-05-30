package beans;

public class CanaleBean {
    public CanaleBean(String nome, String progetto, String data, String tipo) {
        this.nome = nome;
        this.progetto = progetto;
        this.data = data;
        this.tipo = tipo;
        this.partecipazione=true;
    }

    public boolean isPartecipazione() {
        return partecipazione;
    }

    public CanaleBean(String nome, String progetto, String data, String tipo, boolean partecipazione) {
        this.nome = nome;
        this.progetto = progetto;
        this.data = data;
        this.tipo = tipo;
        this.partecipazione=partecipazione;
    }
    boolean partecipazione;
    public String getNome() {
        return nome;
    }

    public String getProgetto() {
        return progetto;
    }

    public String getData() {
        return data;
    }

    public String getTipo() {
        return tipo;
    }

    private String nome,progetto,data,tipo;
}
