package dao.dbInteraction;

import org.mariadb.jdbc.Connection;
import utils.EnvCostants;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSIngleton {
    private static ConnectionSIngleton istanza;
    private Connection connessione;
    private static String connection_url;
    private static String user;
    private static String pass;
    private  static PermessiEnum ruolo;
    private  static  void setRole(PermessiEnum ruolo) {
        ConnectionSIngleton.ruolo=ruolo;
        switch (ruolo) {
            case LOGIN:
                user = System.getenv(EnvCostants.LOGIN_USER);
                pass = System.getenv(EnvCostants.LOGIN_PASSWORD);
                break;
            case AMMINISTRATORE:
                user=System.getenv(EnvCostants.AMMINISTRATORE_USER);
                pass=System.getenv(EnvCostants.AMMINISTRATORE_PASSWORD);
                break;
            case LAVORATORE:
                user=System.getenv(EnvCostants.LAVORATORE_USER);
                pass=System.getenv(EnvCostants.LAVORATORE_PASSWORD);
                break;
            case CAPOPROGETTO:
                user=System.getenv(EnvCostants.CAPO_USER);
                pass=System.getenv(EnvCostants.CAPO_PASSWORD);
                break;
            default:
        }
    }

    private ConnectionSIngleton() throws SQLException {
        connection_url = System.getenv(EnvCostants.ENV_URL);
        setRole(PermessiEnum.LOGIN);
        try {
            connessione = (Connection) DriverManager.getConnection(connection_url, user, pass);
        } catch (SQLException e) {
            System.out.println("Variabili d'ambiente non cofigurate oppure configurate male chiamare l'amministratore di sistema");
            throw e;
        }

    }

    public static  void changePermissionLevel(PermessiEnum e) throws SQLException {
        if(istanza==null){
            istanza=new ConnectionSIngleton();
        }
        if(ConnectionSIngleton.ruolo==e)return;
        setRole(e);
        istanza.connessione.close();
        istanza.connessione = (Connection) DriverManager.getConnection(connection_url, user, pass);

    }

    public static Connection getConnessione() throws SQLException {
        if (istanza == null) istanza = new ConnectionSIngleton();
        return istanza.connessione;
    }

}
