package dao;


import beans.ProgettoBean;
import dao.dbInteraction.ConnectionSIngleton;
import models.Amministratore;
import models.CandidatoCapoProgetto;
import models.Lavoratore;
import models.Progetto;

import java.sql.*;
import java.util.ArrayList;

public class AmministratorDao {
    public AmministratorDao(){

    }
    private static AmministratorDao sing;
    public static AmministratorDao getDao(){
            if(sing==null) sing=new AmministratorDao();
            return sing;
    }

    public ArrayList<Progetto> vediProgetti() throws SQLException {
        Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement statement = connection.prepareCall("{call listProgetti()}");
        boolean result=statement.execute();
        ArrayList<Progetto> progetti=new ArrayList<>();
        if(result){
            ResultSet reslutSet=statement.getResultSet();
            while (reslutSet.next()) {
                Lavoratore lav=reslutSet.getString(StaticNames.cfCapo)==null?new Lavoratore("","",""): new Lavoratore(reslutSet.getString(StaticNames.cfCapo),reslutSet.getString(StaticNames.nomeCapo),reslutSet.getString(StaticNames.cognomeCapo));
                progetti.add(Progetto.getProgetto(reslutSet.getString(StaticNames.nomeProgetto),reslutSet.getDate(StaticNames.dataProgetto),lav));
            }
            return progetti;
        }else{
            return new ArrayList<>();
        }


    }

    public ArrayList<Progetto> vediProgettiSenzaCapo() throws SQLException {
        Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement statement = connection.prepareCall("{call listProgettiSenzaCapo()}");
        boolean result=statement.execute();
        ArrayList<Progetto> progetti=new ArrayList<>();
        if(result){
            ResultSet reslutSet=statement.getResultSet();
            while (reslutSet.next()) {
                progetti.add(Progetto.getProgetto(reslutSet.getString(StaticNames.nomeProgetto),reslutSet.getDate(StaticNames.dataProgetto),new Lavoratore("","","")));
            }
            return progetti;
        }else{
            return new ArrayList<>();
        }
    }

    public  ArrayList<CandidatoCapoProgetto> listaCandidatiPerCapoProgetto(ProgettoBean progetto) throws SQLException {
        ArrayList<CandidatoCapoProgetto> lista=new ArrayList<>();
        Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement statement = connection.prepareCall("{call listaLavoratoriPerPromozione(?)}");
        statement.setString(1, progetto.getNome());
        boolean result=statement.execute();

        if(result){
            ResultSet resultSet=statement.getResultSet();
            while(resultSet.next()){
                lista.add(new CandidatoCapoProgetto(new Lavoratore(resultSet.getString(StaticNames.cfLavoratore),resultSet.getString(StaticNames.nomeLavoratore),resultSet.getString(StaticNames.cognomeLavoratore)),resultSet.getInt("numeroCapoProgetto")));
            }
        }
        return lista;
    }

    public void impostaNuovoCapoProgetto(Progetto progetto, Lavoratore lavoratore, Amministratore am) throws SQLException {
        Connection connection = ConnectionSIngleton.getConnessione();
        CallableStatement statement = connection.prepareCall("{call inserisciCapoProgetto(?,?,?)}");
        statement.setString(StaticNames.cfLavoratore,lavoratore.getCf());
        statement.setString(StaticNames.cfAmministratore,am.getCf());
        statement.setString(StaticNames.nomeProgetto, progetto.getNome());
        boolean result=statement.execute();

        if(result){
            System.out.println("risultato ottenuto");
        }

    }



}

