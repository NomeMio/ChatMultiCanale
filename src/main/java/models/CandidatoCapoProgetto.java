package models;

public class CandidatoCapoProgetto {
   private Lavoratore lavoratore;
   private int numeroDiProgettiInCuiECapo;

    public CandidatoCapoProgetto(Lavoratore lavoratore, int numeroDiProgettiInCuiECapo) {
        this.lavoratore = lavoratore;
        this.numeroDiProgettiInCuiECapo = numeroDiProgettiInCuiECapo;
    }

    public int getNumeroDiProgettiInCuiECapo() {
        return numeroDiProgettiInCuiECapo;
    }
    public String getCf(){
        return lavoratore.getCf();
    }
    public String getName(){
        return lavoratore.getNome();
    }
    public String getCognome(){
        return  lavoratore.getCognome();
    }
}
