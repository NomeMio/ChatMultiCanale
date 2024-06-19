package dao;

import Exceptions.DbProblemEception;
import dao.dbInteraction.ConnectionSIngleton;
import models.*;
import utils.EnvCostants;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LavoratoreDao {
    private static LavoratoreDao sing;

    public static LavoratoreDao getDao() {
        if (sing == null) sing = new LavoratoreDao();
        return sing;
    }

    private LavoratoreDao(){}

    public ArrayList<Progetto> listaProgetti(Lavoratore lavoratore) throws SQLException {
        Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement statement = connection.prepareCall("{call listaProgettiPerLavoratori(?)}");
        statement.setString(StaticNames.cfLavoratore, lavoratore.getCf());
        boolean result = statement.execute();
        if (result) {
            ArrayList<Progetto> progetti = new ArrayList<>();
            ResultSet reslutSet = statement.getResultSet();
            while (reslutSet.next()) {
                Lavoratore lav = reslutSet.getString(StaticNames.cfCapo) == null ? new Lavoratore("", "", "") : new Lavoratore(reslutSet.getString(StaticNames.cfCapo), "", "");
                progetti.add(Progetto.getProgetto(reslutSet.getString(StaticNames.nomeProgetto), reslutSet.getDate(StaticNames.dataProgetto), lav));
            }
            return progetti;
        }

        return null;

    }

    public ArrayList<Canale> listaCanaliPublici(Progetto progetto, String cfL) throws SQLException {
        Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement statement = connection.prepareCall("{call listaCanaliPubliciLavoratore(?,?)}");
        statement.setString(StaticNames.nomeProgetto, progetto.getNome());
        statement.setString(StaticNames.cfLavoratore, cfL);
        boolean result = statement.execute();
        if (result) {
            ArrayList<Canale> canali = new ArrayList<>();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Canale canale = new Canale(resultSet.getString(StaticNames.nomeCanale), progetto.getNome(), CanaliTypes.valueOf(resultSet.getString(StaticNames.tipoCanale)), resultSet.getDate(StaticNames.dataCanale));
                canali.add(canale);
            }
            return canali;
        }
        return null;

    }

    public ArrayList<CanalePrivato> listaCanaliPrivati(Canale canale,String cfl) throws SQLException {
        Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement statement = connection.prepareCall("{call listaCanaliPrivatiLavoratore(?,?,?)}");
        statement.setString(StaticNames.cfLavoratore,cfl);
        statement.setString(StaticNames.nomeProgetto, canale.getProgetto());
        statement.setString(StaticNames.nomeCanale,canale.getNome());
        ArrayList<CanalePrivato> canali=new ArrayList<>();
        boolean result = statement.execute();
        if(result) {
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
        }
        return canali;
    }

    public ArrayList<Messaggio> leggiMessaggi(Canale canale) throws SQLException {
        org.mariadb.jdbc.Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement s = connection.prepareCall("{call letturaUltimiMessaggiCanale(?,?,?)}");
        s.setInt(StaticNames.quantitaM, EnvCostants.MESSAGE_FOR_PAGE);
        s.setString(StaticNames.nomeProgetto, canale.getProgetto());
        s.setString(StaticNames.nomeCanale, canale.getNome());
        return fetchMessagi(s);
    }


    private ArrayList<Messaggio> fetchMessagi(CallableStatement s) throws SQLException {
        ArrayList<Messaggio> messaggi = new ArrayList<>();
        boolean result = s.execute();
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
        return messaggi;

    }

    public ArrayList<Messaggio> leggiMessaggiPrecedenti(int ultimoMEssaggioLetto) throws SQLException {
        org.mariadb.jdbc.Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement s = connection.prepareCall("{call letturaMessaggiPrecedenti(?,?)}");
        s.setInt(StaticNames.quantitaM, EnvCostants.MESSAGE_FOR_PAGE);
        s.setInt(StaticNames.idUltimoMessaggioLetto, ultimoMEssaggioLetto);
        return fetchMessagi(s);
    }

    public void inserisciMessaggio(Messaggio messaggio,int messagioRispostas) throws SQLException, DbProblemEception {
        org.mariadb.jdbc.Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement s = connection.prepareCall("{call inserisciMessaggio(?,?,?,?,?)}");
        s.setString(StaticNames.cfLavoratore,messaggio.getAutore().getCf());
        s.setString(StaticNames.nomeCanale,messaggio.getNameCanale());
        s.setString(StaticNames.nomeProgetto,messaggio.getNameProgetto());
        s.setString(StaticNames.testoMessaggio,messaggio.getTesto());
        s.setInt(StaticNames.idMessaggioRisposta,messagioRispostas);
        s.execute();//TODO MAYBE need to be checked
    }


}
