package dao;

import Exceptions.DbProblemEception;
import dao.dbInteraction.ConnectionSIngleton;
import models.*;
import utils.EnvCostants;

import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class LavoratoreDao {
    private static LavoratoreDao sing;

    public static LavoratoreDao getDao() {
        if (sing == null) sing = new LavoratoreDao();
        return sing;
    }

    private LavoratoreDao(){}


    // ---------------------------Procedura listaProgettiPerLavoratori  --------------------------------



    public ArrayList<Progetto> listaProgetti(Lavoratore lavoratore) throws SQLException, DbProblemEception {
        CallableStatement statement=listaProgettiPrepare(lavoratore);
        boolean result = executeStatment(statement);
        if (result) {
            return listaProgettiExecute(statement);
        }
        return new ArrayList<>();
    }

    private CallableStatement listaProgettiPrepare(Lavoratore lavoratore) throws DbProblemEception {
        org.mariadb.jdbc.Connection connection = getConncetion();
        CallableStatement statement = null;
        try{
            statement = connection.prepareCall("{call listaProgettiPerLavoratori(?)}");
            statement.setString(StaticNames.cfLavoratore, lavoratore.getCf());
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return statement;
    }

    private ArrayList<Progetto> listaProgettiExecute(CallableStatement statement) throws DbProblemEception {
        ArrayList<Progetto> progetti = new ArrayList<>();
        try{

            ResultSet reslutSet = statement.getResultSet();
            while (reslutSet.next()) {
                Lavoratore lav = reslutSet.getString(StaticNames.cfCapo) == null ? new Lavoratore("", "", "") : new Lavoratore(reslutSet.getString(StaticNames.cfCapo), "", "");
                progetti.add(Progetto.getProgetto(reslutSet.getString(StaticNames.nomeProgetto), reslutSet.getDate(StaticNames.dataProgetto), lav));
            }
            return progetti;
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return progetti;
    }

    // ---------------------------Procedura listaCanaliPubliciLavoratore  --------------------------------


    public ArrayList<Canale> listaCanaliPublici(Progetto progetto, String cfL) throws SQLException, DbProblemEception {
        CallableStatement statement=listaCanaliPubliciPrepare(progetto,cfL);
        boolean result = executeStatment(statement);
        if (result) {
            return listaCanaliPubliciExecure(statement);
        }
        return new ArrayList<>();
    }

    private  ArrayList<Canale> listaCanaliPubliciExecure(CallableStatement statement) throws DbProblemEception {
        ArrayList<Canale> canali = new ArrayList<>();
        try{

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Canale canale = new Canale(resultSet.getString(StaticNames.nomeCanale), resultSet.getString(StaticNames.nomeProgetto), CanaliTypes.valueOf(resultSet.getString(StaticNames.tipoCanale)), resultSet.getDate(StaticNames.dataCanale));
                canali.add(canale);
            }
            return canali;
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return canali;
    }

    private CallableStatement listaCanaliPubliciPrepare(Progetto progetto, String cfL) throws DbProblemEception {
        org.mariadb.jdbc.Connection connection = getConncetion();
        CallableStatement statement = null;
        try{
            statement = connection.prepareCall("{call listaCanaliPubliciLavoratore(?,?)}");
            statement.setString(StaticNames.nomeProgetto, progetto.getNome());
            statement.setString(StaticNames.cfLavoratore, cfL);
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return statement;
    }



    // ---------------------------Procedura listaCanaliPrivatiLavoratore  --------------------------------

    public ArrayList<CanalePrivato> listaCanaliPrivati(Canale canale,String cfl) throws  DbProblemEception {
        CallableStatement statement=listaCanaliPrivatiPrepare(canale,cfl);
        boolean result = executeStatment(statement);
        if(result) {
                return listaCanaliPivatiFetch(statement);
        }
        return new ArrayList<>();
    }

    private CallableStatement listaCanaliPrivatiPrepare(Canale canale,String cfl) throws DbProblemEception {
        org.mariadb.jdbc.Connection connection = getConncetion();
        CallableStatement s = null;
        try{
            s= connection.prepareCall("{call listaCanaliPrivatiLavoratore(?,?,?)}");
            s.setString(StaticNames.cfLavoratore,cfl);
            s.setString(StaticNames.nomeProgetto, canale.getProgetto());
            s.setString(StaticNames.nomeCanale,canale.getNome());
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return s;
    }

    private ArrayList<CanalePrivato> listaCanaliPivatiFetch(CallableStatement statement) throws DbProblemEception {
        ArrayList<CanalePrivato> canali=new ArrayList<>();
        try{
            ResultSet ses = statement.getResultSet();
            while(ses.next()){
                Lavoratore primo=new Lavoratore(ses.getString(StaticNames.cfLavoratore),ses.getString(StaticNames.nomeLavoratore),ses.getString(StaticNames.cognomeLavoratore),ses.getString(StaticNames.emailLavoratore));
                Lavoratore secondo=new Lavoratore(ses.getString(StaticNames.cfLavoratoreAltro),ses.getString(StaticNames.nomeLavoratoreAltro),ses.getString(StaticNames.cognomeLavoratoreAltro),ses.getString(StaticNames.emailLavoratoreAltro));
                Canale origine=new Canale(ses.getString(StaticNames.nomeCanaleOrigine),ses.getString(StaticNames.nomeProgettoOrigine));
                Messaggio messaggioInizializzante=new Messaggio(ses.getInt(StaticNames.idMessaggio),ses.getInt(StaticNames.isMessaggioPrecedente),primo,ses.getString(StaticNames.testoMessaggio),ses.getDate(StaticNames.dataCanale),origine);
                int idCitato = ses.getInt(StaticNames.idMessaggioRisposta);
                if (idCitato != 0) {
                    Lavoratore autoreCitato = new Lavoratore(ses.getString(StaticNames.cfLavoratoreRisposta), ses.getString(StaticNames.nomeLavoratoreRisposta), ses.getString(StaticNames.cognomeLavoratoreRisposta), ses.getString(StaticNames.emailLavoratoreRisposta));
                    Messaggio messCItato = new Messaggio(ses.getInt(StaticNames.idMessaggioRisposta), ses.getInt(StaticNames.isMessaggioPrecedenteRisposta), autoreCitato, ses.getString(StaticNames.testoMessaggioRisposta), ses.getTimestamp(StaticNames.timeMessaggioRispotsa), origine);
                    messaggioInizializzante.setCitato(messCItato);
                }
                CanalePrivato canalePrivato=new CanalePrivato(ses.getString(StaticNames.nomeCanale),ses.getString(StaticNames.nomeProgetto),CanaliTypes.PRIVATO,ses.getDate(StaticNames.dataCanale),messaggioInizializzante,new Lavoratore[]{primo,secondo});
                canali.add(canalePrivato);
            }
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return canali;
    }

    // ---------------------------Procedura letturaUltimiMessaggiCanale--------------------------------



    public ArrayList<Messaggio> leggiMessaggi(Canale canale) throws  DbProblemEception {
        CallableStatement s=leggiMessaggiPrepare(canale);
        return fetchMessagi(s);
    }

    private CallableStatement leggiMessaggiPrepare(Canale canale) throws DbProblemEception {
        org.mariadb.jdbc.Connection connection = getConncetion();
        CallableStatement s = null;
        try{
            s = connection.prepareCall("{call letturaUltimiMessaggiCanale(?,?,?)}");
            s.setInt(StaticNames.quantitaM, EnvCostants.MESSAGE_FOR_PAGE);
            s.setString(StaticNames.nomeProgetto, canale.getProgetto());
            s.setString(StaticNames.nomeCanale, canale.getNome());
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return s;
    }


    private ArrayList<Messaggio> fetchMessagi(CallableStatement s) throws DbProblemEception {
        ArrayList<Messaggio> messaggi = new ArrayList<>();
        boolean result = executeStatment(s);
        try{
            ResultSet ses = s.getResultSet();
            while (result) {
                if (ses.next()) {
                    Lavoratore autore = new Lavoratore(ses.getString(StaticNames.cfLavoratore), ses.getString(StaticNames.nomeLavoratore), ses.getString(StaticNames.cognomeLavoratore), ses.getString(StaticNames.emailLavoratore));
                    Canale canale = new Canale(ses.getString(StaticNames.nomeCanale), ses.getString(StaticNames.nomeProgetto));
                    Messaggio mess = new Messaggio(ses.getInt(StaticNames.idMessaggio), ses.getInt(StaticNames.isMessaggioPrecedente), autore, ses.getString(StaticNames.testoMessaggio), ses.getTimestamp(StaticNames.timeMessaggio), canale);
                    int idCitato = ses.getInt(StaticNames.idMessaggioRisposta);
                    if (idCitato != 0) {
                        Lavoratore autoreCitato = new Lavoratore(ses.getString(StaticNames.cfLavoratoreRisposta), ses.getString(StaticNames.nomeLavoratoreRisposta), ses.getString(StaticNames.cognomeLavoratoreRisposta), ses.getString(StaticNames.emailLavoratoreRisposta));
                        Messaggio messCItato = new Messaggio(ses.getInt(StaticNames.idMessaggioRisposta), ses.getInt(StaticNames.isMessaggioPrecedenteRisposta), autoreCitato, ses.getString(StaticNames.testoMessaggioRisposta), ses.getTimestamp(StaticNames.timeMessaggioRispotsa), canale);
                        mess.setCitato(messCItato);
                    }
                    messaggi.add(mess);
                } else {
                    return messaggi;
                }
                result = s.getMoreResults();
                ses.close();
                ses = s.getResultSet();
            }

        }catch (SQLException e){
                hanleSqlError(e);
        }
        return messaggi;
    }


    // ---------------------------Procedura leggiMessaggiPrecedenti--------------------------------

    public ArrayList<Messaggio> leggiMessaggiPrecedenti(int ultimoMEssaggioLetto) throws DbProblemEception {
        CallableStatement s=leggiMessaggiPrecedentiPrepare(ultimoMEssaggioLetto);
        return fetchMessagi(s);
    }

    private CallableStatement leggiMessaggiPrecedentiPrepare(int ultimoMEssaggioLetto) throws DbProblemEception {
        org.mariadb.jdbc.Connection connection = getConncetion();
        CallableStatement s = null;
        try{
            s = connection.prepareCall("{call letturaMessaggiPrecedenti(?,?)}");
            s.setInt(StaticNames.quantitaM, EnvCostants.MESSAGE_FOR_PAGE);
            s.setInt(StaticNames.idUltimoMessaggioLetto, ultimoMEssaggioLetto);
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return s;
    }


    // ---------------------------Procedura inserisciMessaggio--------------------------------
    public void inserisciMessaggio(Messaggio messaggio,int messagioRispostas) throws  DbProblemEception {
        CallableStatement s=inserisciMessaggioPrepare(messaggio,messagioRispostas);
        executeStatment(s) ;
    }

    private CallableStatement inserisciMessaggioPrepare(Messaggio messaggio,int messagioRispostas) throws DbProblemEception {
        org.mariadb.jdbc.Connection connection = getConncetion();
        CallableStatement s = null;
        try{
            s = connection.prepareCall("{call inserisciMessaggio(?,?,?,?,?)}");
            s.setString(StaticNames.cfLavoratore,messaggio.getAutore().getCf());
            s.setString(StaticNames.nomeCanale,messaggio.getNameCanale());
            s.setString(StaticNames.nomeProgetto,messaggio.getNameProgetto());
            s.setString(StaticNames.testoMessaggio,messaggio.getTesto());
            s.setInt(StaticNames.idMessaggioRisposta,messagioRispostas);
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return s;
    }

    // ---------------------------Procedura inserisciNuovoCanalePrivato--------------------------------

    public void creaNuovoCanalePrivato(Messaggio messaggio,Lavoratore lav) throws  DbProblemEception {
            CallableStatement statement=inserisciNuovoCanalePrivatoPrpare(messaggio,lav);
            executeStatment(statement);
    }

    public CallableStatement inserisciNuovoCanalePrivatoPrpare(Messaggio messaggio,Lavoratore lav) throws DbProblemEception {
        org.mariadb.jdbc.Connection connection = getConncetion();
        CallableStatement s=null;
        try{
                s = connection.prepareCall("{call inserisciNuovoCanalePrivato(?,?)}");
                s.setInt(StaticNames.idMessaggio,messaggio.getId());
                s.setString(StaticNames.cfLavoratore,lav.getCf());
        } catch (SQLException e) {
            hanleSqlError(e);
        }
        return s;
    }





    private org.mariadb.jdbc.Connection getConncetion() throws DbProblemEception {
        try {
            return ConnectionSIngleton.getConnessione();
        } catch (SQLException e) {
            hanleSqlError(e);
        }
        return null;
    }

    private boolean executeStatment(CallableStatement c) throws DbProblemEception {
        try {
            return c.execute();
        } catch (SQLException e) {
            hanleSqlError(e);
        }
        return false;
    }

    private void hanleSqlError(SQLException e) throws DbProblemEception {
        if(Objects.equals(e.getSQLState(), "45001"))throw new DbProblemEception(e.getMessage());
        throw new DbProblemEception(e.getMessage());
    }

}
