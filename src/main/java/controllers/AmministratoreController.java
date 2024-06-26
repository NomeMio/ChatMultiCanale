package controllers;

import Exceptions.DbProblemEception;
import beans.CandidatiCapoProgettoBean;
import beans.ProgettoBean;
import dao.AmministratorDao;
import enge.AmministratoreSingleton;
import models.Amministratore;
import models.CandidatoCapoProgetto;
import models.Lavoratore;
import models.Progetto;
import utils.CostumLogger;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class AmministratoreController {
        public String getAmCf(){
            Amministratore am=AmministratoreSingleton.getAmministrator();
            return am!=null?am.getCf():null;
        }

        public ProgettoBean[] getProgetti() throws DbProblemEception {
            AmministratorDao dao=AmministratorDao.getDao();
            try {
                ArrayList<Progetto> progetti=dao.vediProgetti();
                return getProgettoBeans(progetti);
            } catch (SQLException e) {
                CostumLogger.getInstance().logError(e);
                throw new DbProblemEception();
            }
        }
        public ProgettoBean[] getProgettiSenzaCapo() throws DbProblemEception {
            AmministratorDao dao=AmministratorDao.getDao();
            try {
                ArrayList<Progetto> progetti=dao.vediProgettiSenzaCapo();
                return getProgettoBeans(progetti);
            } catch (SQLException e) {
                CostumLogger.getInstance().logError(e);
                throw new DbProblemEception();
            }
        }

    private  ProgettoBean[] getProgettoBeans(ArrayList<Progetto> progetti) {
        ProgettoBean[] progettiBeans=new ProgettoBean[progetti.size()];
        Iterator<Progetto> it= progetti.iterator();
        int contatore=0;
        while(it.hasNext()){
            Progetto progetto=it.next();
            progettiBeans[contatore]=new ProgettoBean(progetto.getNome(),progetto.getCapoProgetto().getCf(),progetto.getCapoProgetto().getCognome(),progetto.getCapoProgetto().getNome(),progetto.getData().toString());
            contatore++;
        }
        return progettiBeans;
    }

    public  CandidatiCapoProgettoBean[] getCandidati(ProgettoBean bean) throws SQLException {
        AmministratorDao dao=AmministratorDao.getDao();
        ArrayList<CandidatoCapoProgetto> candidatoCapoProgettos=dao.listaCandidatiPerCapoProgetto(bean);
        int shize=candidatoCapoProgettos.size();
        CandidatiCapoProgettoBean[] beans= new CandidatiCapoProgettoBean[shize];
        Iterator<CandidatoCapoProgetto> it=candidatoCapoProgettos.iterator();
        int contatore=0;
        while(it.hasNext()){
            CandidatoCapoProgetto candidato=it.next();
            beans[contatore]=new CandidatiCapoProgettoBean(candidato.getCf(),candidato.getName(),candidato.getCognome(),candidato.getNumeroDiProgettiInCuiECapo());
            contatore++;
        }
        return beans;
    }

    public void assegnaCapoProgetto(ProgettoBean pBean,CandidatiCapoProgettoBean cBean) throws DbProblemEception {
            Progetto progetto=new Progetto(pBean.getNome(), Date.valueOf(pBean.getData()));
            Lavoratore lav=new Lavoratore(cBean.getCf(),cBean.getName(),cBean.getCognome() );
            AmministratorDao dao=new AmministratorDao();
        assert AmministratoreSingleton.getAmministrator() != null;
        try {
            dao.impostaNuovoCapoProgetto(progetto,lav,AmministratoreSingleton.getAmministrator());
        } catch (SQLException e) {
            if(Objects.equals(e.getSQLState(), "45001"))throw new DbProblemEception(e.getMessage());
            else CostumLogger.getInstance().logError(e);
            throw new DbProblemEception();
        }

    }


}
