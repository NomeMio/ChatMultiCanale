package dao;

import dao.dbInteraction.ConnectionSIngleton;
import models.Lavoratore;
import models.Progetto;

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
}
