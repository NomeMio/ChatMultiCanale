package dao;

import Exceptions.LoginFallitoException;
import beans.CredentialBean;
import dao.dbInteraction.ConnectionSIngleton;
import models.Amministratore;
import models.Lavoratore;
import models.Utente;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class LoginDao {
    private LoginDao(){

    }
    public static LoginDao getDao() {
        if(sing==null) sing=new LoginDao();
        return sing;
    }
    public Utente login(CredentialBean bean) throws SQLException, LoginFallitoException {
        CallableStatement test = preapareStatement(bean);
        String result=test.getString(StaticNames.numeriResult);
        if(result.equals("0")){
            throw new LoginFallitoException();
        }else{
            String cf=test.getString(StaticNames.cfResult);
            String nome=test.getString(StaticNames.nomeResult);
            String cognome=test.getString(StaticNames.cognomeResult);
            String email=test.getString(StaticNames.emailResult);
            if(result.equals("1")){
                return new Lavoratore(cf,nome,cognome,email);
            } else if (result.equals("2")) {
                return new Amministratore(cf,nome,cognome,email);
            }
            return new Utente(cf,nome,cognome,email);
        }

    }

    private static CallableStatement preapareStatement(CredentialBean bean) throws SQLException {
        Connection connection= ConnectionSIngleton.getConnessione();
        CallableStatement test=connection.prepareCall("{call login(?,?,?,?)}");
        test.setString(1, bean.getUserName());
        test.setString(2, bean.getPassword());
        test.registerOutParameter(StaticNames.numeriResult, Types.NUMERIC);
        test.registerOutParameter(StaticNames.cfResult,Types.VARCHAR);
        test.registerOutParameter(StaticNames.nomeResult, Types.VARCHAR);
        test.registerOutParameter(StaticNames.cognomeResult,Types.VARCHAR);
        test.registerOutParameter(StaticNames.emailResult,Types.VARCHAR);
        test.execute();
        return test;
    }

    private static LoginDao sing;
}
