import dao.LavoratoreDao;
import dao.dbInteraction.ConnectionSIngleton;
import dao.dbInteraction.PermessiEnum;
import models.Canale;
import models.Messaggio;
import org.junit.Test;
import org.mariadb.jdbc.Connection;

import java.lang.reflect.Member;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestStoredProceduresLavoratore {
    @Test
    public void provaLetturaMEssaggi() throws SQLException {
        ConnectionSIngleton.changePermissionLevel(PermessiEnum.LAVORATORE);
        Connection connection=ConnectionSIngleton.getConnessione();
        CallableStatement s=connection.prepareCall("{call letturaMessaggiPrecedenti(?,?)}");
        s.setInt("quantita",4);
        s.setInt("idPrimoMessaggio",5);
        boolean result=s.execute();
        ResultSet ses=s.getResultSet();
        while(result) {
            while (ses.next()) {
                System.out.println(ses.getString(1));
            }
            result=s.getMoreResults();
            ses.close();
            ses=s.getResultSet();
        }
    }

    @Test
    public void testDaoMessaggi() throws SQLException {
        ConnectionSIngleton.changePermissionLevel(PermessiEnum.LAVORATORE);
        LavoratoreDao dao=LavoratoreDao.getDao();
        ArrayList<Messaggio> messaggi=dao.leggiMessaggi(new Canale("test","progetto1"));
        System.out.println(messaggi.get(0).getTesto());
    }
}
