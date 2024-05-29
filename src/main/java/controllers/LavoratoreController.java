package controllers;

import Exceptions.DbProblemEception;
import beans.ProgettoBean;
import dao.LavoratoreDao;
import enge.AmministratoreSingleton;
import enge.LavoratoreSIngleton;
import models.Amministratore;
import models.Lavoratore;
import models.Progetto;
import utils.CostumLogger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class LavoratoreController {
    public String getLavCf(){
        Lavoratore lv= LavoratoreSIngleton.getLavoratore();
        return lv!=null?lv.getCf():null;
    }

    public ProgettoBean[] getProgetti() throws DbProblemEception {
        LavoratoreDao dao=new LavoratoreDao();
        assert LavoratoreSIngleton.getLavoratore() != null;
        ArrayList<Progetto> progetti;
        try {
             progetti=dao.listaProgetti(LavoratoreSIngleton.getLavoratore())
        } catch (SQLException e) {
            CostumLogger.getInstance().logError(e);
            throw new DbProblemEception();
        }
        int contatore=0;
        ProgettoBean[] beans=new ProgettoBean[progetti.size()];
        for (Progetto progetto : progetti) {
            beans[contatore]=new ProgettoBean(pr)
        }
    }
}
