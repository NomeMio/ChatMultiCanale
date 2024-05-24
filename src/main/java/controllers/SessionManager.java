package controllers;

import Exceptions.LoginFallitoException;
import beans.CredentialBean;
import beans.UserBean;
import dao.LoginDao;
import enge.AmministratoreSingleton;
import enge.LavoratoreSIngleton;
import models.Amministratore;
import models.Lavoratore;
import models.Utente;
import utils.CostumLogger;
import utils.Ruoli;

import java.sql.SQLException;

public class SessionManager {
    public UserBean login(CredentialBean credenziali) throws LoginFallitoException {
        LoginDao dao=LoginDao.getDao();
        Utente utente;
        try{
            utente=dao.login(credenziali);
        } catch (SQLException e) {
            CostumLogger.getInstance().logError(e);
            throw new LoginFallitoException();
        }
        if(utente instanceof Amministratore e){
            AmministratoreSingleton.createSingleton(e);
        }else if(utente instanceof Lavoratore e){
            LavoratoreSIngleton.createSingleton(e);
        }
        return new UserBean(utente.getNome(),utente.getCognome(),utente.getEmail(),utente instanceof Amministratore? Ruoli.Amministratore:Ruoli.Lavoratore);
    }
    public UserBean logOut(){

    }
}