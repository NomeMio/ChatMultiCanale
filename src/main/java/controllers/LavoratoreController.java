package controllers;

import Exceptions.DbProblemEception;
import beans.*;
import dao.LavoratoreDao;
import enge.AmministratoreSingleton;
import enge.LavoratoreSIngleton;
import models.*;
import utils.CostumLogger;
import utils.PrinterCostum;
import utils.Ruoli;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class LavoratoreController {
    public String getLavCf(){
        Lavoratore lv= LavoratoreSIngleton.getLavoratore();
        return lv!=null?lv.getCf():null;
    }

    public ProgettoBean[] getProgetti() throws DbProblemEception {
        LavoratoreDao dao=LavoratoreDao.getDao();
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
    public CanaleBean[] getCanaliPubliciLavoratore(ProgettoBean bean) throws DbProblemEception {
        LavoratoreDao dao=LavoratoreDao.getDao();
        ArrayList<Canale> canali;
        try {
            canali = dao.listaCanaliPublici(new Progetto(bean.getNome(), Date.valueOf(bean.getData()),new Lavoratore(bean.getCfCapo(), "","")),LavoratoreSIngleton.getLavoratore().getCf());
        } catch (SQLException e) {
            CostumLogger.getInstance().logError(e);
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

    public MessaggioBean[] getMessaggi(CanaleBean canale) throws DbProblemEception {
            LavoratoreDao dao=LavoratoreDao.getDao();
        try {
            return convertiMessaggiToBeanArray(dao.leggiMessaggi(new Canale(canale.getNome(), canale.getProgetto())));
        } catch (SQLException e) {
            if(Objects.equals(e.getSQLState(), "45001")){
                throw new DbProblemEception(e.getMessage());
            }else{
                throw new DbProblemEception();
            }
        }
    }

    public MessaggioBean[] getMessaggiPrecedenti(int idUltimoLetto) throws DbProblemEception {
        LavoratoreDao dao=LavoratoreDao.getDao();
        try {
            return convertiMessaggiToBeanArray(dao.leggiMessaggiPrecedenti(idUltimoLetto));
        } catch (SQLException e) {
            if(Objects.equals(e.getSQLState(), "45001")){
                throw new DbProblemEception(e.getMessage());
            }else{
                throw new DbProblemEception();
            }
        }
    }

    private MessaggioBean[] convertiMessaggiToBeanArray(ArrayList<Messaggio> messaggi){
        int dim=messaggi.size();
        int contatore=0;
        MessaggioBean[] beans=new MessaggioBean[dim];
        for(Messaggio messaggio:messaggi){
            beans[contatore]=convertiMessaggiotoBean(messaggio);
            if(messaggio.getCitato()!=null){
                beans[contatore].setCitato(convertiMessaggiotoBean(messaggio.getCitato()));
            }
                    contatore++;
        }
        return beans;

    }

    private MessaggioBean convertiMessaggiotoBean(Messaggio messaggio){
        return new MessaggioBean(messaggio.getTesto(),messaggio.getAutore().getEmail(),messaggio.getAutore().getNome(),messaggio.getAutore().getCognome(),messaggio.getData().toString(),messaggio.getNameCanale(),messaggio.getNameProgetto(),messaggio.getId(), messaggio.getPrecedente());
    }

    public CanalePrivatoBean[] getCanaliPrivati(CanaleBean canaleOrigine, String cf) throws DbProblemEception {
        LavoratoreDao dao=LavoratoreDao.getDao();
        ArrayList<CanalePrivato> cannali;
        try {
            cannali = dao.listaCanaliPrivati(new Canale(canaleOrigine.getNome(), canaleOrigine.getProgetto()),cf);
        } catch (SQLException e) {
            throw new DbProblemEception(e.getMessage());
        }
        CanalePrivatoBean[] beans=new CanalePrivatoBean[cannali.size()];
        int contatore=0;
        for(CanalePrivato canale:cannali){
                MessaggioBean messaggio=convertiMessaggiotoBean(canale.getMessaggioInizializzante());
                UserBean[] utenti=new UserBean[]{convertiLavoratoreToBean(canale.getLavoratori()[0]),convertiLavoratoreToBean(canale.getLavoratori()[1])};
                beans[contatore]=new CanalePrivatoBean(canale.getNome(),canale.getProgetto(),canale.getDataCreazione().toString(), canale.getTypeToString(),messaggio,utenti);
                contatore++;
        }
        return beans;


    }

    public void inserisciMessaggio(MessaggioBean messaggioBean,MessaggioBean messaggioPerRisposta) throws DbProblemEception {
            Lavoratore lavoratore=LavoratoreSIngleton.getLavoratore();
            Messaggio messaggio=new Messaggio(lavoratore,messaggioBean.getTesto(),new Canale(messaggioBean.getNomeCanale(),messaggioBean.getNomeProgetto()));
            LavoratoreDao dao=LavoratoreDao.getDao();
        try {
            dao.inserisciMessaggio(messaggio,messaggioPerRisposta.getIdMess());
        } catch (SQLException e) {
            if(e.getSQLState()=="45001")throw new DbProblemEception(e.getMessage());
            throw new DbProblemEception(e.getMessage());
        }
    }

    public void inserisciMessaggio(MessaggioBean messaggioBean) throws DbProblemEception {
        Lavoratore lavoratore=LavoratoreSIngleton.getLavoratore();
        Messaggio messaggio=new Messaggio(lavoratore,messaggioBean.getTesto(),new Canale(messaggioBean.getNomeCanale(),messaggioBean.getNomeProgetto()));
        LavoratoreDao dao=LavoratoreDao.getDao();
        try {
            dao.inserisciMessaggio(messaggio,0);
        } catch (SQLException e) {
            if(e.getSQLState()=="45001")throw new DbProblemEception(e.getMessage());
            throw new DbProblemEception(e.getMessage());
        }
    }




    private UserBean convertiLavoratoreToBean(Lavoratore lav){
        return new UserBean(lav.getNome(),lav.getCognome(),lav.getEmail(), Ruoli.Lavoratore);
    }

}
