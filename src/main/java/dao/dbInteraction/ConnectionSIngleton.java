package dao.dbInteraction;

import org.mariadb.jdbc.Connection;
import utils.EnvCostants;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSIngleton {
    public static ConnectionSIngleton istanza;
    private Connection connessione;
    private static String connection_url;
    private static String user;
    private static String pass;

    private void setRole(PermessiEnum ruolo){
        switch (ruolo){
            case LOGIN:
                user = System.getenv(EnvCostants.LOGIN_USER);
                pass=System.getenv(EnvCostants.LOGIN_PASSWORD);
                break;
            case AMMINISTRATORE:
                break;
            case LAVORATORE:
                break;
            case CAPOPROGETTO:
                break;
            default:
        }
    }
    private ConnectionSIngleton() throws SQLException {
        connection_url = System.getenv(EnvCostants.ENV_URL);
        setRole(PermessiEnum.LOGIN);
        Connection connection = (Connection) DriverManager.getConnection(connection_url, user,"");

    }

    public void changeRoles(PermessiEnum e) throws SQLException {
        setRole(e);
        connessione.close();
        connessione=(Connection) DriverManager.getConnection(connection_url, user,"");
    }
    public static Connection getConnessione() throws SQLException {
        if(istanza==null) istanza=new ConnectionSIngleton();
        return istanza.connessione;
    }

}
