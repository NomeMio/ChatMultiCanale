package beans;

public class ProgettoBean {
    private String nome;
    private String data;
    private String nomeCapo;
    private String cognomeCapo;
    private String cfCapo;

    public ProgettoBean(String nome, String cfCapo, String cognomeCapo, String nomeCapo, String c) {
        this.nome = nome;
        this.cfCapo = cfCapo;
        this.cognomeCapo = cognomeCapo;
        this.nomeCapo = nomeCapo;
        this.data = c;
    }
    public ProgettoBean(String nome,  String data,String cfCapo) {
        this.nome = nome;
        this.cfCapo = "";
        this.cognomeCapo = "";
        this.nomeCapo = "";
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeCapo() {
        return nomeCapo;
    }

    public String getCognomeCapo() {
        return cognomeCapo;
    }

    public String getCfCapo() {
        return cfCapo;
    }
}
