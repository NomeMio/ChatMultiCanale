package dao;


import dao.dbInteraction.ConnectionSIngleton;
import io.quarkus.runtime.configuration.ArrayListFactory;
import models.Lavoratore;
import models.Progetto;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AmministratorDao {
    private AmministratorDao(){

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
                progetti.add(Progetto.getProgetto(reslutSet.getString(StaticNames.nomeProgetto),reslutSet.getDate(StaticNames.dataProgetto), new Lavoratore(StaticNames.cfCapo,StaticNames.nomeCapo,StaticNames.cognomeCapo)));
            }
            return progetti;
        }else{
            return new ArrayList<>();
        }


    }

}

