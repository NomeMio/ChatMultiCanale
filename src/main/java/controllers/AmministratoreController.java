package controllers;

import Exceptions.DbProblemEception;
import beans.ProgettoBean;
import dao.AmministratorDao;
import enge.AmministratoreSingleton;
import models.Amministratore;
import models.Progetto;
import utils.CostumLogger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class AmministratoreController {
        public String getAmCf(){
            Amministratore am=AmministratoreSingleton.getAmministrator();
            return am!=null?am.getCf():null;
        }

        public ProgettoBean[] getProgetti() throws DbProblemEception {
            AmministratorDao dao=AmministratorDao.getDao();
            try {
                ArrayList<Progetto> progetti=dao.vediProgetti();
                ProgettoBean[] progettiBeans=new ProgettoBean[progetti.size()];
                Iterator<Progetto> it=progetti.iterator();
                int contatore=0;
                while(it.hasNext()){
                    Progetto progetto=it.next();
                    progettiBeans[contatore]=new ProgettoBean(progetto.getNome(),progetto.getCapoProgetto().getCf(),progetto.getCapoProgetto().getCognome(),progetto.getCapoProgetto().getNome(),progetto.getData().toString());
                    contatore++;
                }
                return progettiBeans;
            } catch (SQLException e) {
                CostumLogger.getInstance().logError(e);
                throw new DbProblemEception();
            }
        }
}
