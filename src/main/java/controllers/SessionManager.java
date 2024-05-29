package controllers;

import Exceptions.LoginFallitoException;
import beans.CredentialBean;
import beans.UserBean;
import dao.LoginDao;
import dao.dbInteraction.ConnectionSIngleton;
import dao.dbInteraction.PermessiEnum;
import enge.AmministratoreSingleton;
import enge.LavoratoreSIngleton;
import models.Amministratore;
import models.Lavoratore;
import models.Utente;
import utils.CostumLogger;
import utils.Ruoli;

import java.sql.Connection;
import java.sql.SQLException;

public class SessionManager {
    public UserBean login(CredentialBean credenziali) throws LoginFallitoException, SQLException {
        LoginDao dao = LoginDao.getDao();
        Utente utente;
        try {
            utente = dao.login(credenziali);
        } catch (SQLException e) {
            CostumLogger.getInstance().logError(e);
            throw e;
        }
        if (utente instanceof Amministratore e) {
            AmministratoreSingleton.createSingleton(e);
            ConnectionSIngleton.changePermissionLevel(PermessiEnum.AMMINISTRATORE);
        } else if (utente instanceof Lavoratore e) {
            LavoratoreSIngleton.createSingleton(e);
            ConnectionSIngleton.changePermissionLevel(PermessiEnum.LAVORATORE);
        }
        return new UserBean(utente.getNome(), utente.getCognome(), utente.getEmail(), utente instanceof Amministratore ? Ruoli.Amministratore : Ruoli.Lavoratore);
    }

    public void changePrmissioneLevel(PermessiEnum e){
        if(e.equals(PermessiEnum.AMMINISTRATORE))return;
        try {
            ConnectionSIngleton.changePermissionLevel(e);
        } catch (SQLException ex) {
            CostumLogger.getInstance().logError(ex);
        }
    }
    public void logOut() {
        AmministratoreSingleton.deleteSingelton();
        LavoratoreSIngleton.deleteSingelton();
    }

    public Ruoli getRole(){
            if(AmministratoreSingleton.getAmministrator()!=null)return Ruoli.Amministratore;
            if(LavoratoreSIngleton.getLavoratore()!=null)return Ruoli.Lavoratore;
            return null;
    }
}
