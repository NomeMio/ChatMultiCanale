package beans;

import models.Lavoratore;

public class CanalePrivatoBean extends CanaleBean{
    private MessaggioBean inizializante;
    private UserBean[] partecipanti;

    public MessaggioBean getInizializante() {
        return inizializante;
    }

    public UserBean[] getPartecipanti() {
        return partecipanti;
    }

    public CanalePrivatoBean(String nome, String progetto, String data, String tipo, MessaggioBean messaggio, UserBean[] partecipanti) {
        super(nome, progetto, data, tipo);
        this.inizializante=messaggio;
        this.partecipanti=partecipanti;
    }
}
