package controllers;

import Exceptions.DbProblemEception;
import beans.CanaleBean;
import beans.ProgettoBean;
import dao.LavoratoreDao;
import enge.AmministratoreSingleton;
import enge.LavoratoreSIngleton;
import models.Amministratore;
import models.Canale;
import models.Lavoratore;
import models.Progetto;
import utils.CostumLogger;

import java.sql.Date;
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
        ArrayList<Progetto> progetti=null;
        try {
             progetti=dao.listaProgetti(LavoratoreSIngleton.getLavoratore());
        } catch (SQLException e) {
            CostumLogger.getInstance().logError(e);
            throw new DbProblemEception();
        }
        int contatore=0;
        ProgettoBean[] beans=new ProgettoBean[progetti.size()];
        for (Progetto progetto : progetti) {

            beans[contatore]=new ProgettoBean(progetto.getNome(),progetto.getData().toString(),progetto.getCapoProgetto().getCf());
            contatore++;
        }
        return beans;
    }
    public CanaleBean[] getCanaliLavoratore(ProgettoBean bean) throws DbProblemEception {
        LavoratoreDao dao=new LavoratoreDao();
        ArrayList<Canale> canali;
        try {
            canali = dao.listaCanali(new Progetto(bean.getNome(), Date.valueOf(bean.getData()),new Lavoratore(bean.getCfCapo(), "","")),LavoratoreSIngleton.getLavoratore().getCf());
        } catch (SQLException e) {
            throw new DbProblemEception();
        }
        return convertCanaliListToArray(canali);
    }

    private CanaleBean[] convertCanaliListToArray(ArrayList<Canale> canali){
        int dim=canali.size();
        int contatore=0;
        CanaleBean[] beans=new CanaleBean[dim];
        for(Canale canale:canali){
            beans[contatore]=new CanaleBean(canale.getNome(),canale.getProgetto(),canale.getDataCreazione().toString(),canale.getTypeToString());
            contatore++;
        }
        return beans;

    }
}
