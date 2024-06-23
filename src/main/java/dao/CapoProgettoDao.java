package dao;

import Exceptions.DbProblemEception;
import dao.dbInteraction.ConnectionSIngleton;
import models.*;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class CapoProgettoDao {
    private static CapoProgettoDao sing;

    public static CapoProgettoDao getDao() {
        if (sing == null) sing = new CapoProgettoDao();
        return sing;
    }

    private CapoProgettoDao(){}







    // ---------------------------Procedura inserisciNuovoCanalePublico  --------------------------------



    public void creaNuovoCanalePublico(Lavoratore lav, Canale canale) throws DbProblemEception {
            CallableStatement s=creaNuovoCanalePublicoPrepare(lav,canale);
            executeStatment(s);
    }

    private  CallableStatement creaNuovoCanalePublicoPrepare(Lavoratore lav, Canale canale) throws DbProblemEception {
        org.mariadb.jdbc.Connection connection = getConncetion();
        CallableStatement s=null;
        try{
            s = connection.prepareCall("{call inserisciNuovoCanalePublico(?,?,?)}");
            s.setString(StaticNames.cfLavoratore,lav.getCf());
            s.setString(StaticNames.nomeCanale, canale.getNome());
            s.setString(StaticNames.nomeProgetto, canale.getProgetto());
        } catch (SQLException e) {
            hanleSqlError(e);
        }
        return s;
    }

    // ---------------------------Procedura listaCanaliPrivatiCapo  --------------------------------

    public ArrayList<CanalePrivato> listaCanaliPrivati(Canale canale, String cfl) throws  DbProblemEception {
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
            s= connection.prepareCall("{call listaCanaliPrivatiCapo(?,?,?)}");
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
                CanalePrivato canalePrivato=new CanalePrivato(ses.getString(StaticNames.nomeCanale),ses.getString(StaticNames.nomeProgetto), CanaliTypes.PRIVATO,ses.getDate(StaticNames.dataCanale),messaggioInizializzante,new Lavoratore[]{primo,secondo});
                canali.add(canalePrivato);
            }
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return canali;
    }


    // ---------------------------Procedura listaLavoratoriProgetto  --------------------------------

    public ArrayList<Lavoratore> listaCandidatiCanale(Canale canale) throws DbProblemEception {
            CallableStatement statement=listaCandidatiCanalePrere(canale);
            if(executeStatment(statement)) return listaCandidatiCanaleExecute(statement);
            return new ArrayList<>();
    }
    private CallableStatement listaCandidatiCanalePrere(Canale canale) throws DbProblemEception {
        org.mariadb.jdbc.Connection connection = getConncetion();
        CallableStatement s = null;
        try{
            s= connection.prepareCall("{call listaLavoratoriProgetto(?,?)}");
            s.setString(StaticNames.nomeProgetto, canale.getProgetto());
            s.setString(StaticNames.nomeCanale, canale.getNome());
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return s;
    }

    private ArrayList<Lavoratore> listaCandidatiCanaleExecute(CallableStatement statement) throws DbProblemEception {
        ArrayList<Lavoratore> lavoratores=new ArrayList<>();
        try{
            ResultSet ses = statement.getResultSet();
            while(ses.next()){
                lavoratores.add(new Lavoratore(ses.getString(StaticNames.cfLavoratore), ses.getString( StaticNames.nomeLavoratore),ses.getString( StaticNames.cognomeLavoratore),ses.getString( StaticNames.emailLavoratore)));
            }
        }catch (SQLException e){
            hanleSqlError(e);
        }
        return lavoratores;
    }


    // ---------------------------Procedura inserisciLavoratoreNelCanale  --------------------------------

    public void inserisciPartecipanteNelCanale(Lavoratore lavoratore,Canale canale) throws DbProblemEception {
        CallableStatement statement=inserisciPartecipanteNelCanalePrepare(lavoratore,canale);
        executeStatment(statement);
    }
    private CallableStatement inserisciPartecipanteNelCanalePrepare(Lavoratore lavoratore,Canale canale) throws DbProblemEception {
        org.mariadb.jdbc.Connection connection = getConncetion();
        CallableStatement s = null;
        try{
            s= connection.prepareCall("{call inserisciLavoratoreNelCanale(?,?,?)}");
            s.setString(StaticNames.nomeProgetto, canale.getProgetto());
            s.setString(StaticNames.nomeCanale, canale.getNome());
            s.setString(StaticNames.cfLavoratore,lavoratore.getCf());
        }catch (SQLException e){
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
