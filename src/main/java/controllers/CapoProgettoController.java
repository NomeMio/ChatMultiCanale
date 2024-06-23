package controllers;

import Exceptions.DbProblemEception;
import beans.*;
import dao.CapoProgettoDao;
import dao.dbInteraction.ConnectionSIngleton;
import dao.dbInteraction.PermessiEnum;
import enge.LavoratoreSIngleton;
import models.Canale;
import models.CanalePrivato;
import models.Lavoratore;
import models.Messaggio;
import utils.Ruoli;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class CapoProgettoController {

    public CapoProgettoController(ProgettoBean bean) throws DbProblemEception {
        try{
            if(Objects.equals(bean.getCfCapo(), LavoratoreSIngleton.getLavoratore().getCf())) {
                ConnectionSIngleton.changePermissionLevel(PermessiEnum.CAPOPROGETTO);
            }else{
                ConnectionSIngleton.changePermissionLevel(PermessiEnum.LAVORATORE);
            }
        }catch (SQLException e){
            throw new DbProblemEception(e.getMessage());
        }
    }

    public CanalePrivatoBean[] getCanaliPrivati(CanaleBean canaleOrigine, String cf) throws DbProblemEception {
        CapoProgettoDao dao=CapoProgettoDao.getDao();
        ArrayList<CanalePrivato> cannali;
        cannali = dao.listaCanaliPrivati(new Canale(canaleOrigine.getNome(), canaleOrigine.getProgetto()),cf);
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

    public CandidatiCanale[] getCanditaiPerCanali(CanaleBean bean) throws DbProblemEception {
        CapoProgettoDao dao=CapoProgettoDao.getDao();
        ArrayList<Lavoratore> lavoratores=dao.listaCandidatiCanale(new Canale(bean.getNome(), bean.getProgetto()));
        CandidatiCanale[] beans=new CandidatiCanale[lavoratores.size()];
        int cont=0;
        for (Lavoratore a:lavoratores){
            beans[cont]=new CandidatiCanale(a.getCf(),a.getNome(),a.getCognome(),a.getEmail());
            cont++;
        }
        return beans;
    }

    public void inserisciLavoratoreNelCanale(CanaleBean canaleBean,CandidatiCanale candidatiCanale) throws DbProblemEception {
        Lavoratore lav=new Lavoratore(candidatiCanale.cf(),candidatiCanale.nome(),candidatiCanale.cognome());
        Canale canale=new Canale(canaleBean.getNome(),canaleBean.getProgetto());
        CapoProgettoDao.getDao().inserisciPartecipanteNelCanale(lav,canale);
    }


    private UserBean convertiLavoratoreToBean(Lavoratore lav){
        return new UserBean(lav.getNome(),lav.getCognome(),lav.getEmail(), Ruoli.Lavoratore);
    }
    private MessaggioBean convertiMessaggiotoBean(Messaggio messaggio){
        return new MessaggioBean(messaggio.getTesto(),messaggio.getAutore().getEmail(),messaggio.getAutore().getNome(),messaggio.getAutore().getCognome(),messaggio.getData().toString(),messaggio.getNameCanale(),messaggio.getNameProgetto(),messaggio.getId(), messaggio.getPrecedente());
    }

    public void inserisciNuovoCanale(CanaleBean bean) throws DbProblemEception {
        CapoProgettoDao dao=CapoProgettoDao.getDao();
        Lavoratore lavoratore= LavoratoreSIngleton.getLavoratore();
        dao.creaNuovoCanalePublico(lavoratore,new Canale(bean.getNome(), bean.getProgetto()));
    }
}
