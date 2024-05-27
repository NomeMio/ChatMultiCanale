package beans;

public class CandidatiCapoProgettoBean {
    private String cf;
    private String name;
    private String cognome;
    private int numeroPromozioni;

    public String getCf() {
        return cf;
    }

    public String getName() {
        return name;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNumeroPromozioni() {
        return String.valueOf(numeroPromozioni);
    }

    public CandidatiCapoProgettoBean(String cf, String name, String cognome, int numeroDiProgettiInCuiECapo) {
        this.cf=cf;
        this.name=name;
        this.cognome=cognome;
        this.numeroPromozioni=numeroDiProgettiInCuiECapo;
    }
}
