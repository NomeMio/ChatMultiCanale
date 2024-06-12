package dao;

import dao.dbInteraction.ConnectionSIngleton;
import dao.dbInteraction.PermessiEnum;
import models.*;
import utils.CostumLogger;
import utils.EnvCostants;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LavoratoreDao {
    private static LavoratoreDao sing;
    public static LavoratoreDao getDao(){
        if(sing==null) sing=new LavoratoreDao();
        return sing;
    }

    public ArrayList<Progetto> listaProgetti(Lavoratore lavoratore) throws SQLException {
        Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement statement = connection.prepareCall("{call listaProgettiPerLavoratori(?)}");
        statement.setString(StaticNames.cfLavoratore,lavoratore.getCf());
        boolean result=statement.execute();
        if (result){
            ArrayList<Progetto> progetti=new ArrayList<>();
            ResultSet reslutSet=statement.getResultSet();
            while (reslutSet.next()) {
                Lavoratore lav=reslutSet.getString(StaticNames.cfCapo)==null?new Lavoratore("","",""): new Lavoratore(reslutSet.getString(StaticNames.cfCapo),"","");
                progetti.add(Progetto.getProgetto(reslutSet.getString(StaticNames.nomeProgetto),reslutSet.getDate(StaticNames.dataProgetto),lav));
            }
            return progetti;
        }

        return null;

    }

    public ArrayList<Canale> listaCanaliPublici(Progetto progetto,String cfL) throws SQLException {
        Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement statement = connection.prepareCall("{call listaCanaliPubliciLavoratore(?,?)}");
        statement.setString(StaticNames.nomeProgetto, progetto.getNome());
        statement.setString(StaticNames.cfLavoratore,cfL);
        boolean result=statement.execute();
        if(result){
            ArrayList<Canale> canali=new ArrayList<>();
            ResultSet resultSet=statement.getResultSet();
            while (resultSet.next()){
                Canale canale=new Canale(resultSet.getString(StaticNames.nomeCanale),progetto.getNome(), CanaliTypes.valueOf(resultSet.getString(StaticNames.tipoCanale)),resultSet.getDate(StaticNames.dataCanale));
                canali.add(canale);
            }
            return canali;
        }
        return null;

    }

    public ArrayList<Messaggio> leggiMessaggi(Canale canale) throws SQLException {
        org.mariadb.jdbc.Connection connection=ConnectionSIngleton.getConnessione();
        ArrayList<Messaggio> messaggi=new ArrayList<>();
        CallableStatement s=connection.prepareCall("{call letturaUltimiMessaggiCanale(?,?,?)}");
        s.setInt(StaticNames.quantitaM, EnvCostants.MESSAGE_FOR_PAGE);
        s.setString(StaticNames.nomeProgetto,canale.getProgetto());
        s.setString(StaticNames.nomeCanale, canale.getNome());
        boolean result=s.execute();
        ResultSet ses=s.getResultSet();
        while(result) {
            if(ses.next()){
                Lavoratore autore=new Lavoratore(ses.getString(StaticNames.cfLavoratore),ses.getString(StaticNames.nomeLavoratore),ses.getString(StaticNames.cognomeLavoratore),ses.getString(StaticNames.emailLavoratore));
                Messaggio mess=new Messaggio(ses.getInt(StaticNames.idMessaggio),ses.getInt(StaticNames.isMessaggioPrecedente),autore,ses.getString(StaticNames.testoMessaggio),ses.getTimestamp(StaticNames.timeMessaggio),canale);
                if(ses.next()){
                    Lavoratore autoreCitato=new Lavoratore(ses.getString(StaticNames.cfLavoratore),ses.getString(StaticNames.nomeLavoratore),ses.getString(StaticNames.cognomeLavoratore),ses.getString(StaticNames.emailLavoratore));
                    Messaggio messCItato=new Messaggio(ses.getInt(StaticNames.idMessaggio),ses.getInt(StaticNames.isMessaggioPrecedente),autoreCitato,ses.getString(StaticNames.testoMessaggio),ses.getTimestamp(StaticNames.timeMessaggio),canale);
                    mess.setCitato(messCItato);
                }
                messaggi.add(mess);
            }else {
                return messaggi;
            }
            result=s.getMoreResults();
            ses.close();
            ses=s.getResultSet();
        }
        return messaggi;
    }
    public ArrayList<Messaggio> leggiMessaggiPrecedenti(int ultimoMEssaggioLetto) throws SQLException {
        org.mariadb.jdbc.Connection connection=ConnectionSIngleton.getConnessione();
        ArrayList<Messaggio> messaggi=new ArrayList<>();
        CallableStatement s=connection.prepareCall("{call letturaMessaggiPrecedenti(?,?)}");
        s.setInt(StaticNames.quantitaM, EnvCostants.MESSAGE_FOR_PAGE);
        s.setInt(StaticNames.idUltimoMessaggioLetto, ultimoMEssaggioLetto);
        boolean result=s.execute();
        ResultSet ses=s.getResultSet();
        while(result) {
            if(ses.next()){
                Lavoratore autore=new Lavoratore(ses.getString(StaticNames.cfLavoratore),ses.getString(StaticNames.nomeLavoratore),ses.getString(StaticNames.cognomeLavoratore),ses.getString(StaticNames.emailLavoratore));
                Canale canale=new Canale(ses.getString(StaticNames.nomeCanale),ses.getString(StaticNames.nomeProgetto));
                Messaggio mess=new Messaggio(ses.getInt(StaticNames.idMessaggio),ses.getInt(StaticNames.isMessaggioPrecedente),autore,ses.getString(StaticNames.testoMessaggio),ses.getTimestamp(StaticNames.timeMessaggio),canale);
                if(ses.next()){
                    Lavoratore autoreCitato=new Lavoratore(ses.getString(StaticNames.cfLavoratore),ses.getString(StaticNames.nomeLavoratore),ses.getString(StaticNames.cognomeLavoratore),ses.getString(StaticNames.emailLavoratore));
                    canale=new Canale(ses.getString(StaticNames.nomeCanale),ses.getString(StaticNames.nomeProgetto));
                    Messaggio messCItato=new Messaggio(ses.getInt(StaticNames.idMessaggio),ses.getInt(StaticNames.isMessaggioPrecedente),autoreCitato,ses.getString(StaticNames.testoMessaggio),ses.getTimestamp(StaticNames.timeMessaggio),canale);
                    mess.setCitato(messCItato);
                }
                messaggi.add(mess);
            }else {
                return messaggi;
            }
            result=s.getMoreResults();
            ses.close();
            ses=s.getResultSet();
        }
        return messaggi;
    }
}
